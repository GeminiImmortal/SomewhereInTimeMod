package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.boost.BoostDataProvider;
import net.geminiimmortal.mobius.capability.infamy.InfamyProvider;
import net.geminiimmortal.mobius.item.custom.Dieselytra;
import net.geminiimmortal.mobius.util.InfamyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class CapabilityEventHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(InfamyProvider.ID, new InfamyProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(BoostDataProvider.ID, new BoostDataProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        event.getOriginal().getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(oldCap ->
                event.getPlayer().getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(newCap ->
                        newCap.setInfamy(oldCap.getInfamy())
                )
        );
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        InfamyHelper.sync(event.getPlayer());
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack chest = entity.getItemBySlot(EquipmentSlotType.CHEST);

        if (chest.getItem() instanceof Dieselytra && ElytraItem.isFlyEnabled(chest)) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                // allow player to glide like normal elytra
                if (player.isFallFlying()) {
                    // same logic vanilla uses for durability tick damage
                    if (!player.level.isClientSide && (player.tickCount % 20 == 0)) {
                        chest.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlotType.CHEST));
                    }
                }
            }
        }
    }


}

