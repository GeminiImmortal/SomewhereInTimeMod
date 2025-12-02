package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.boost.BoostDataProvider;
import net.geminiimmortal.mobius.item.custom.Dieselytra;
import net.geminiimmortal.mobius.item.render.DieselytraLayer;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class DieselytraEvents {
    private static final int BOOST_COOLDOWN = 40; // 2 seconds

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        ItemStack chest = player.getItemBySlot(EquipmentSlotType.CHEST);
        if (!(chest.getItem() instanceof Dieselytra)) return;

        if (player.isShiftKeyDown() && player.isOnGround()) {
            player.getCapability(BoostDataProvider.BOOST_CAP).ifPresent(boostData -> {
                long gameTime = player.level.getGameTime();

                if (gameTime - boostData.getLastBoost() >= BOOST_COOLDOWN) {
                    // Apply boost
                    Vector3d motion = player.getDeltaMovement();
                    player.setDeltaMovement(motion.x, 1.1D, motion.z);
                    player.hasImpulse = true;
                    player.level.playSound(null, player.blockPosition(),
                            ModSounds.DIESELYTRA_BOOST.get(),
                            SoundCategory.PLAYERS, 1.0F, 1.0F);

                    // Prevent fall damage from this jump
                    boostData.setIgnoreNextFall(true);

                    // Update cooldown
                    boostData.setLastBoost(gameTime);

                    // Feedback
                    player.level.playSound(null, player.blockPosition(),
                            SoundEvents.FIREWORK_ROCKET_LAUNCH,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);

                    if (player.level instanceof ServerWorld) {
                        ((ServerWorld) player.level).sendParticles(ParticleTypes.CLOUD,
                                player.getX(), player.getY(), player.getZ(),
                                10, 0.3, 0.1, 0.3, 0.02);
                    }
                }

            });
        }
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
                    spawnEngineParticles(player);
                    // same logic vanilla uses for durability tick damage
                    if (!player.level.isClientSide && (player.tickCount % 20 == 0)) {
                        chest.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlotType.CHEST));
                    }
                    if (!player.level.isClientSide && (player.tickCount % 200 == 0)) {
                        player.level.playSound(null, player.blockPosition(), ModSounds.DIESELYTRA_ENGINE.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (entity instanceof PlayerEntity) {
            entity.getCapability(BoostDataProvider.BOOST_CAP).ifPresent(boostData -> {
                if (boostData.shouldIgnoreNextFall()) {
                    event.setCanceled(true);
                    boostData.setIgnoreNextFall(false);
                    entity.level.playSound(null, entity.blockPosition(),
                            ModSounds.DIESELYTRA_LANDS.get(),
                            SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            });
        }
    }

    private static void spawnEngineParticles(PlayerEntity player) {
        World world = player.level;

        // Don’t run on client AND server, only server sends them to clients
        if (world.isClientSide) return;

        // Direction the player is facing
        Vector3d look = player.getLookAngle();

        // Get left/right offset using perpendicular vector to look dir
        Vector3d left = look.cross(new Vector3d(0, 1, 0)).normalize().scale(0.4); // tweak spacing

        // Engine positions behind the player
        Vector3d back = look.scale(-0.6); // push behind player
        Vector3d base = player.position().add(0, player.getBbHeight() * 0.5, 0).add(back);

        Vector3d leftEnginePos = base.add(left);
        Vector3d rightEnginePos = base.subtract(left);

        ServerWorld serverWorld = (ServerWorld) world;

        // Smoke
        serverWorld.sendParticles(ParticleTypes.SMOKE, leftEnginePos.x, leftEnginePos.y, leftEnginePos.z,
                1, 0.02, 0.02, 0.02, 0.01);
        serverWorld.sendParticles(ParticleTypes.SMOKE, rightEnginePos.x, rightEnginePos.y, rightEnginePos.z,
                1, 0.02, 0.02, 0.02, 0.01);

        // Flames sometimes (e.g. 1 in 5 ticks)
        if (world.random.nextInt(5) == 0) {
            serverWorld.sendParticles(ParticleTypes.FLAME, leftEnginePos.x, leftEnginePos.y, leftEnginePos.z,
                    1, 0.0, 0.0, 0.0, 0.01);
            serverWorld.sendParticles(ParticleTypes.FLAME, rightEnginePos.x, rightEnginePos.y, rightEnginePos.z,
                    1, 0.0, 0.0, 0.0, 0.01);
        }
    }
    @Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEventSubscriber {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            net.minecraft.client.renderer.entity.PlayerRenderer renderer =
                    (net.minecraft.client.renderer.entity.PlayerRenderer)
                            net.minecraft.client.Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get("default");
            renderer.addLayer(new DieselytraLayer<>(renderer));

            renderer = (net.minecraft.client.renderer.entity.PlayerRenderer)
                    net.minecraft.client.Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get("slim");
            renderer.addLayer(new DieselytraLayer<>(renderer));

            // Add to armor stand renderer
            if (Minecraft.getInstance().getEntityRenderDispatcher().renderers.containsKey(EntityType.ARMOR_STAND)) {
                @SuppressWarnings("unchecked")
                ArmorStandRenderer armorStandRenderer =
                        (ArmorStandRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(EntityType.ARMOR_STAND);

                armorStandRenderer.addLayer(new DieselytraLayer<>(armorStandRenderer));
            }
        }
    }

}

