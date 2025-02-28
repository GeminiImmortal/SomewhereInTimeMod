package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ManaVial extends Item {

    private static final String MANA_TAG = "StoredMana";
    private boolean active = false;

    public ManaVial() {
        super(new Properties()
                .stacksTo(1)
                .durability(100)
                .setNoRepair()
                .tab(ItemGroup.TAB_MISC)
                .rarity(Rarity.RARE));
    }

    @Override
    public int getDamage(ItemStack itemStack) {
        return 101 - ((getStoredMana(itemStack) * 100) / getMaxManalevel());

    }

    IFormattableTextComponent manaLevelTip = new StringTextComponent("Right-click to absorb mana. Mana capacity: " + getMaxManalevel()).setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.AQUA));

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(manaLevelTip);
    }

    @Override
    public ActionResultType useOn (ItemUseContext context) {
        if(!context.getLevel().isClientSide()) {
            if (!active) {
                active = true;
                int absorbedMana = absorbMana(Objects.requireNonNull(context.getPlayer()), context.getItemInHand());
                if (absorbedMana > 0) {
                    context.getPlayer().displayClientMessage(new TranslationTextComponent("item.mobius.mana_vial.active"), true);
                    absorbMana(context.getPlayer(), context.getItemInHand());
                    return ActionResultType.SUCCESS;
                }
            } else {
                active = false;
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    public int getMaxManalevel() {
        return 128;
    }

    private int absorbMana(PlayerEntity player, ItemStack vial) {
        int totalAbsorbed = 0;
        int capacity = getMaxManalevel();
        int currentMana = getStoredMana(vial);

        for (ItemStack stack : player.inventory.items) {
            if (stack.getItem().equals(ModItems.RAW_MANA.get())) {
                int transferable = Math.min(stack.getCount(), capacity - currentMana);
                if (transferable > 0) {
                    stack.shrink(transferable);
                    currentMana += transferable;
                    totalAbsorbed += transferable;
                    if (currentMana >= capacity) {
                        break;
                    }
                }
            }
        }

        setStoredMana(vial, currentMana);
        return totalAbsorbed;
    }

    private int getStoredMana(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt(MANA_TAG);
    }

    private void setStoredMana(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(MANA_TAG, amount);
    }

}
