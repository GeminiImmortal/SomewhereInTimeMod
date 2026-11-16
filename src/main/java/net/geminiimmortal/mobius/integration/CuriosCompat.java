package net.geminiimmortal.mobius.integration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.hook.CompatHooks;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.item.custom.Dieselytra;
import net.geminiimmortal.mobius.item.custom.ManaVial;
import net.geminiimmortal.mobius.item.render.DieselytraCurioRenderer;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CuriosCompat {

    public static void init() {
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CuriosCompat::attachCapabilities);

        CompatHooks.getCurioElytraStack = entity -> CuriosApi.getCuriosHelper()
                .findEquippedCurio(stack ->
                        stack.getItem() == ModItems.DIESELYTRA.get() && ElytraItem.isFlyEnabled(stack), entity)
                .map(ImmutableTriple::getRight)
                .orElse(ItemStack.EMPTY);

    }

    public static void registerSlots() {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.BACK.getMessageBuilder().size(1).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.BELT.getMessageBuilder().size(1).build());
    }

    private static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.getItem() == ModItems.DIESELYTRA.get()) {
            ICurio curio = new ICurio() {
                @OnlyIn(Dist.CLIENT)
                @Override
                public boolean canRender(String identifier, int index, LivingEntity livingEntity){
                    return true;
                }

                @OnlyIn(Dist.CLIENT)
                public void render(String identifier, int index, MatrixStack matrixStack,
                                    IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
                                    float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                    float netHeadYaw, float headPitch) {
                            DieselytraCurioRenderer.renderElytra(
                                    matrixStack, renderTypeBuffer, light, livingEntity, stack,
                                    limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch
                            );
                }

                @Override
                public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                    if (Dieselytra.isFlyEnabled(stack) && livingEntity.isFallFlying() && livingEntity.level.isClientSide()) {
                        Dieselytra.handleFlightTick(stack, livingEntity, livingEntity.getFallFlyingTicks());

                    }
                    if (!livingEntity.level.isClientSide && livingEntity.isFallFlying()) {
                        if ((livingEntity.tickCount + 1) % 20 == 0) {
                            stack.hurtAndBreak(1, livingEntity, entity ->
                                    CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, entity)
                            );
                        }
                    }
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }

                @Nonnull
                @Override
                public SoundInfo getEquipSound(SlotContext slotContext) {
                    return new SoundInfo(ModSounds.DIESELYTRA_BOOST.get(), 1.0f, 1.0f);
                }

                private final UUID CURIO_ELYTRA_UUID = UUID.fromString("8f3a1c4e-2b7d-4e9a-9c1f-6d5b4a3e2f10");

                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
                    Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

                    if (ModList.get().isLoaded("caelus") && ElytraItem.isFlyEnabled(stack)) {
                        CaelusCompat.addFallFlyingModifier(attributes, uuid);
                    }

                    return attributes;
                }
            };
            LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);
            event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
                }
            });
            event.addListener(curioOpt::invalidate);
        }
        if (stack.getItem() instanceof ManaVial) {
            ICurio vialCurio = new ICurio() {
                @Override
                public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
                    return true;
                }

                @OnlyIn(Dist.CLIENT)
                @Override
                public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                    matrixStack.pushPose();

                    if (livingEntity.isCrouching()) {
                        matrixStack.translate(0.0D, 0.2D, 0.0D);
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F / (float) Math.PI));
                    }

                    float sideOffset = (index % 2 == 0) ? 0.18F : -0.18F;
                    matrixStack.translate(sideOffset, 0.68D, -0.22D);

                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

                    float scale = 0.45F;
                    matrixStack.scale(scale, scale, scale);

                    Minecraft.getInstance().getItemRenderer().renderStatic(
                            stack,
                            ItemCameraTransforms.TransformType.FIXED,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            matrixStack,
                            renderTypeBuffer
                    );

                    matrixStack.popPose();
                }
            };
            LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> vialCurio);
            event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
                }
            });
            event.addListener(curioOpt::invalidate);
        }
    }

    public static boolean hasCurioEquipped(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity).isPresent();
    }
    public static void getCurioVials(PlayerEntity player, List<ItemStack> manaSources) {
        CuriosApi.getCuriosHelper().findCurios(player, stack -> stack.getItem() instanceof ManaVial)
                .forEach(slotResult -> manaSources.add(slotResult.getStack()));
    }
}

