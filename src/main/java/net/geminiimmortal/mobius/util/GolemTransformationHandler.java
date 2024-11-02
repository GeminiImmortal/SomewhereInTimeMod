package net.geminiimmortal.mobius.util;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GolemTransformationHandler {
    // HashMap to store entities and their remaining time before transformation
    private final Map<UUID, Integer> golemTransformationTimers = new HashMap<>();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        // Only handle SERVER-side ticks in the right dimension
        if (!event.world.isClientSide() && event.world.dimension() == ModDimensions.MOBIUS_WORLD) {
            if (event.phase == TickEvent.Phase.START) {
                // Create a copy of the entry set to safely iterate and modify the original map
                Map<UUID, Integer> timersCopy = new HashMap<>(golemTransformationTimers);

                for (Map.Entry<UUID, Integer> entry : timersCopy.entrySet()) {
                    UUID golemUUID = entry.getKey();
                    Integer remainingTicks = entry.getValue();

                    // Get the golem entity from the world using its UUID
                    Entity golem = ((ServerWorld) event.world).getEntity(golemUUID);

                    if (golem instanceof IronGolemEntity) {
                        IronGolemEntity ironGolem = (IronGolemEntity) golem;

                        // Check if the golem is one of your custom golems
                        if (!(ironGolem instanceof ClubGolemEntity) &&
                                !(ironGolem instanceof DiamondGolemEntity) &&
                                !(ironGolem instanceof HeartGolemEntity) &&
                                !(ironGolem instanceof SpadeGolemEntity)) {

                            // Apply shaking effect or visual feedback here (optional)
                            applyShakingEffect(ironGolem);

                            // Reduce the remaining ticks
                            if (remainingTicks <= 0) {
                                // Transform into a random suit golem
                                transformIntoRandomSuitGolem(ironGolem);
                                golemTransformationTimers.remove(golemUUID); // Remove from original map
                            } else {
                                // Update the remaining ticks
                                golemTransformationTimers.put(golemUUID, remainingTicks - 1);
                            }
                        } else {
                            // Skip transformation for custom golems
                        }
                    } else {
                        // Remove invalid golems
                        golemTransformationTimers.remove(golemUUID);
                    }
                }
            }
        }
    }


    // This method is called to start the transformation timer when the Iron Golem spawns
    public void startGolemTransformation(IronGolemEntity ironGolem) {
        // Set 600 ticks (30 seconds)
        golemTransformationTimers.put(ironGolem.getUUID(), 600);
        System.out.println("Starting transformation countdown for Golem UUID: " + ironGolem.getUUID());
    }



    private void applyShakingEffect(IronGolemEntity golem) {
        // Apply small random rotation changes to simulate shaking
        float shakeAmount = (golem.level.random.nextFloat() - 0.5F) * 10; // Random shake between -5 to 5 degrees
        golem.yRot += shakeAmount; // Adjust the Yaw rotation

        // Randomly adjust the golem's velocity for added visual shaking
        golem.setDeltaMovement(golem.getDeltaMovement().add(
                (golem.level.random.nextDouble() - 0.5) * 0.1,
                0,
                (golem.level.random.nextDouble() - 0.5) * 0.1
        ));
    }


    private void transformIntoRandomSuitGolem(IronGolemEntity ironGolem) {
        // Log the position of the old golem before removal
        double oldX = ironGolem.getX();
        double oldY = ironGolem.getY();
        double oldZ = ironGolem.getZ();

        // Log the transformation
        System.out.println("Transforming Golem at position: " + oldX + ", " + oldY + ", " + oldZ);

        // Remove the iron golem
        ironGolem.remove();

        // Create a new suit golem based on a random type
        Entity suitGolem;
        int randomGolemType = ironGolem.level.random.nextInt(5); // Adjust this if necessary for your cases

        switch (randomGolemType) {
            case 0:
                suitGolem = new HeartGolemEntity(ModEntityTypes.HEART_GOLEM.get(), ironGolem.level);
                break;
            case 1:
                suitGolem = new ClubGolemEntity(ModEntityTypes.CLUB_GOLEM.get(), ironGolem.level);
                break;
            case 2:
                suitGolem = new DiamondGolemEntity(ModEntityTypes.DIAMOND_GOLEM.get(), ironGolem.level);
                break;
            case 3:
                suitGolem = new SpadeGolemEntity(ModEntityTypes.SPADE_GOLEM.get(), ironGolem.level);
                break;
            default:
                suitGolem = new DiamondGolemEntity(ModEntityTypes.DIAMOND_GOLEM.get(), ironGolem.level);
                break;
        }

        // Set the position of the new suit golem to match the Iron Golem's position
        suitGolem.setPos(oldX, oldY, oldZ);

        // Add the new suit golem to the world
        ironGolem.level.addFreshEntity(suitGolem);
        System.out.println("New Golem spawned at position: " + oldX + ", " + oldY + ", " + oldZ);
        applyTransformationParticles(ironGolem);
        playTransformationSound(ironGolem);
    }


    @SubscribeEvent
    public void onIronGolemSpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof IronGolemEntity) {
            IronGolemEntity ironGolem = (IronGolemEntity) event.getEntity();

            // Log when an Iron Golem is spawned
            System.out.println("Iron Golem spawned in dimension: " + event.getWorld().dimension());

            // Add your dimension check here if needed
            if (event.getWorld().dimension() == ModDimensions.MOBIUS_WORLD) {
                System.out.println("Iron Golem spawned in the custom dimension!");
                startGolemTransformation(ironGolem); // This should also log
            }
        }
    }



    private void applyTransformationParticles(IronGolemEntity golem) {
        // Generate particles around the golem
        for (int i = 0; i < 10; i++) {
            double xOffset = (golem.level.random.nextDouble() - 0.5) * 2.0;
            double yOffset = golem.level.random.nextDouble() * golem.getBbHeight();
            double zOffset = (golem.level.random.nextDouble() - 0.5) * 2.0;

            // Example particle: Happy Villager particle (you can use different particles here)
            golem.level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    golem.getX() + xOffset,
                    golem.getY() + yOffset,
                    golem.getZ() + zOffset,
                    0.0, 0.0, 0.0);
        }
    }
    private void playTransformationSound(IronGolemEntity golem) {
        // Play a sound at the golem's position
        golem.level.playSound(null, golem.getX(), golem.getY(), golem.getZ(),
                SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE,
                1.0F, 1.0F);
    }

}
