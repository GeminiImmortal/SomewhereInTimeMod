package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CloneDeathHandler {
    @SubscribeEvent
    public static void onCloneDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof CloneEntity) {
            CloneEntity clone = (CloneEntity) event.getEntity();
            if (clone.getOwner() instanceof GovernorEntity) {
                GovernorEntity boss = clone.getOwner();
                boss.getActiveClones().remove(clone);
            }
        }
    }
}

