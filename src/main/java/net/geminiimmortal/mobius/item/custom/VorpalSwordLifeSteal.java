package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.MolvanEntity;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VorpalSwordLifeSteal {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) event.getSource().getDirectEntity();
            // Check if the player is holding the custom sword
            if (playerEntity.getMainHandItem().getItem() == ModItems.VORPAL_SWORD.get()) {
                // Determine the life-steal amount (e.g., 20% of the damage dealt)
                float lifeStealAmount = event.getAmount() * 0.2F;
                playerEntity.heal(lifeStealAmount);  // Heal the player
                if(playerEntity.level.isClientSide()){
                    int numberOfParticles = 20;
                    for (int i = 0; i < numberOfParticles; i++) {
                        // Get the current position of the deer
                        double x = playerEntity.getX();
                        double y = playerEntity.getY();
                        double z = playerEntity.getZ();

                        // Create and spawn the particle
                        playerEntity.level.addParticle(ParticleTypes.ENCHANT,
                                x + (Math.random() - 0.5) * 2, // Random X position
                                y + 1.0, // Slightly above the deer
                                z + (Math.random() - 0.5) * 2, // Random Z position
                                0.3, 0.3, 0.3); // Initial velocity can be set to zero
                    }
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();

            if (event.getEntity() instanceof MolvanEntity) {
                ((MolvanEntity) event.getEntity()).setTarget(player);
            }
        }
    }
}


