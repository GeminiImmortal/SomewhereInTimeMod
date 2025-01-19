package net.geminiimmortal.mobius.item.custom;

import com.google.common.collect.ImmutableMap;

import net.geminiimmortal.mobius.item.AstralArmorMaterial;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;


import java.util.Map;

public class AstralArmor extends ArmorItem {
    private static final Map<IArmorMaterial, EffectInstance> MATERIAL_TO_EFFECT_MAP =
            ImmutableMap.<IArmorMaterial, EffectInstance>builder()
                    .put(AstralArmorMaterial.ASTRAL,
                            new EffectInstance(Effects.MOVEMENT_SPEED, 40, 0, true, false)).build();

    public AstralArmor(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if(!world.isClientSide()) {
            if(hasFullSuitOfArmorOn(player)) {
                evaluateArmorEffects(player);
            }
        }
    }

    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<IArmorMaterial, EffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            IArmorMaterial mapArmorMaterial = entry.getKey();
            EffectInstance mapStatusEffect = entry.getValue();

            if(hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, IArmorMaterial mapArmorMaterial,
                                            EffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

        if(hasCorrectArmorOn(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addEffect(new EffectInstance(mapStatusEffect.getEffect(),
                    mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier()));
        }
    }

    private boolean hasFullSuitOfArmorOn(PlayerEntity player) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private boolean hasCorrectArmorOn(IArmorMaterial material, PlayerEntity player) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }

            ArmorItem armorItem = (ArmorItem) armorStack.getItem();
            if (armorItem.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

}
