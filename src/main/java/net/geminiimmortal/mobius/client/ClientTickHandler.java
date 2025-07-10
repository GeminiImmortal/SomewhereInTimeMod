package net.geminiimmortal.mobius.client;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT)
public class ClientTickHandler {
    private static boolean skyRendererSet = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END && mc.level != null && !skyRendererSet) {
            if (mc.level.dimension().equals(ModDimensions.MOBIUS_WORLD)) {
                if (!ModList.get().isLoaded("oculus") && !ModList.get().isLoaded("optifine")) {
                    mc.level.effects().setSkyRenderHandler(new MobiusSkyRenderer());
                    skyRendererSet = true;
                }
            }
        }

        // Reset when world unloads
        if (event.phase == TickEvent.Phase.END && mc.level == null) {
            skyRendererSet = false;
        }
    }
}

