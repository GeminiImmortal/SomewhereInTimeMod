package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.item.AstralArmorMaterial;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class AstralArmor extends ArmorItem {
    public AstralArmor(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if(!world.isClientSide()) {
            if(hasFullSuitOfArmorOn(player) && ModEffects.ASTRAL_GRACE.isPresent() && hasCorrectArmorOn(AstralArmorMaterial.ASTRAL, player)) {
                player.addEffect(new EffectInstance(ModEffects.ASTRAL_GRACE.get(), 40, 0, true, false));
            }
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
            if (armorItem.getMaterial() != AstralArmorMaterial.ASTRAL) {
                return false;
            }
        }
        return true;
    }

}
