package net.geminiimmortal.mobius.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static KeyBinding activateRingOneKey;
    public static KeyBinding activateRingTwoKey;

    public static void register() {
        activateRingOneKey = new KeyBinding(
                "key.mobius.activate_warding_ring",
                GLFW.GLFW_KEY_R,
                "key.categories.mobius"
        );
        activateRingTwoKey = new KeyBinding(
                "key.mobius.activate_berserker_ring",
                GLFW.GLFW_KEY_Y,
                "key.categories.mobius"

        );
        ClientRegistry.registerKeyBinding(activateRingOneKey);
        ClientRegistry.registerKeyBinding(activateRingTwoKey);
    }
}
