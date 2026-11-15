package net.geminiimmortal.mobius.integration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.item.custom.Dieselytra;
import net.geminiimmortal.mobius.item.render.DieselytraCurioRenderer;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class CuriosCompat {

    public static void init() {
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CuriosCompat::attachCapabilities);
    }

    public static void registerSlots() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.BODY.getMessageBuilder().size(1).build());
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
                    if (Dieselytra.isFlyEnabled(stack) && livingEntity.isFallFlying() && !livingEntity.level.isClientSide()) {
                        Dieselytra.handleFlightTick(stack, livingEntity, livingEntity.getFallFlyingTicks());
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
    }
    // Safe helper to call from your shared mod code
    public static boolean hasCurioEquipped(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity).isPresent();
    }
}

