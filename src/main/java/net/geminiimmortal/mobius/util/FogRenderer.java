package net.geminiimmortal.mobius.util;

import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mobius", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FogRenderer {

    private static final float TARGET_DENSITY = 0.075F; // Desired fog density in the biome

    private static final float TRANSITION_SPEED = 0.0025F; // Adjust transition speed for smoother effect
    private static float currentFogDensity = 0.0F; // Initialize current fog density


    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        PlayerEntity player = Minecraft.getInstance().player;
        assert player != null;
        BlockPos playerPos = player.getEntity().blockPosition();
        Biome biome = player.level.getBiome(playerPos);
        boolean isOptiFineLoaded = ModList.get().isLoaded("optifine");
        boolean inWindingWoods = biome.getRegistryName().toString().equals("mobius:forsaken_thicket");
        if (!isOptiFineLoaded) {
            if (inWindingWoods) {
                if (currentFogDensity < TARGET_DENSITY) {
                    currentFogDensity += TRANSITION_SPEED; // Increase density toward target
                }
            } else {
                if (currentFogDensity > 0.0F) {
                    currentFogDensity -= TRANSITION_SPEED; // Decrease density to zero
                }
            }
            currentFogDensity = Math.max(0.0F, Math.min(TARGET_DENSITY, currentFogDensity));

            event.setDensity(currentFogDensity);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        boolean isOptiFineLoaded = ModList.get().isLoaded("optifine");
        PlayerEntity player = Minecraft.getInstance().player;
        assert player != null;
        BlockPos playerPos = player.getEntity().blockPosition();
        Biome biome = player.level.getBiome(playerPos);
        if (!isOptiFineLoaded){
            if (biome.getRegistryName().toString().equals("mobius:forsaken_thicket")) {

                event.setRed(0.6F); // Customize fog color as needed
                event.setGreen(0.6F);
                event.setBlue(0.7F);

            }
        }
    }
}


