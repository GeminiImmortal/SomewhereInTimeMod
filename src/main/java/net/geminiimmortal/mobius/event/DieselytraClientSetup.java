package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.render.DieselytraLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DieselytraClientSetup {

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
