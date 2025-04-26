package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.entity.goals.util.ExpandingTelegraphEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEffectHandler {
    private static final List<ExpandingTelegraphEffect> TELEGRAPH_EFFECTS = new ArrayList<>();

    public static void spawnExpandingTelegraph(Vector3d center) {
        TELEGRAPH_EFFECTS.add(new ExpandingTelegraphEffect(
                center,
                ParticleTypes.END_ROD, // or another particle
                30,                 // duration in ticks
                0.2,                // outward speed
                80                  // particles per ring
        ));
    }

    public static void clientTick(Minecraft mc) {
        if (mc.level == null) return;

        TELEGRAPH_EFFECTS.removeIf(effect -> {
            effect.tick();
            return effect.isDone();
        });
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            clientTick(Minecraft.getInstance());
        }
    }

}
