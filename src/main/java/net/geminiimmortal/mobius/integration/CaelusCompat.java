package net.geminiimmortal.mobius.integration;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.UUID;

public class CaelusCompat {
    public static void addFallFlyingModifier(Multimap<Attribute, AttributeModifier> attributes, UUID uuid) {
        Attribute fallFlying = CaelusApi.ELYTRA_FLIGHT.get();
        attributes.put(
                fallFlying,
                new AttributeModifier(uuid, "Curio Elytra flight", 1.0D, AttributeModifier.Operation.ADDITION)
        );
    }
}
