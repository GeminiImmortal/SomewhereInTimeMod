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
            (new ImmutableMap.Builder<IArmorMaterial, EffectInstance>())
                    .put(AstralArmorMaterial.ASTRAL,
                            new EffectInstance(Effects.REGENERATION, 40, 0, true, false)).build();

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
        ItemStack boots = player.inventory.getArmor(0);
        ItemStack leggings = player.inventory.getArmor(1);
        ItemStack breastplate = player.inventory.getArmor(2);
        ItemStack helmet = player.inventory.getArmor(3);

        return !helmet.isEmpty() && !breastplate.isEmpty()
                && !leggings.isEmpty() && !boots.isEmpty();
    }

    private boolean hasCorrectArmorOn(IArmorMaterial material, PlayerEntity player) {
        for (ItemStack armorStack: player.getArmorSlots()) {
            if(!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = ((ArmorItem)player.inventory.getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem)player.inventory.getArmor(1).getItem());
        ArmorItem breastplate = ((ArmorItem)player.inventory.getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem)player.inventory.getArmor(3).getItem());

        return helmet.getMaterial() == material && breastplate.getMaterial() == material &&
                leggings.getMaterial() == material && boots.getMaterial() == material;
    }
}
