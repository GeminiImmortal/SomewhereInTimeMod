package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.item.FlaskType;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


public class ManaVial extends Item {

    private final FlaskType flaskType;
    private final Rarity rarity;

    private static final String MANA_TAG = "StoredMana";

    public ManaVial(Properties properties, FlaskType flaskType, Rarity rarity) {
        super(properties);
        this.flaskType = flaskType;
        this.rarity = rarity;
    }

    public static final String TAG_ACTIVE = "IsActive";

    public boolean isActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(TAG_ACTIVE);
    }

    public void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(TAG_ACTIVE, active);
    }


    @Override
    public int getDamage(ItemStack itemStack) {
        int storedMana = getStoredMana(itemStack);
        int maxMana = getMaxManalevel();

        return maxMana - storedMana;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        IFormattableTextComponent manaLevelTip = new StringTextComponent("Right-click to absorb mana. Mana capacity: " + getMaxManalevel()).setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.AQUA));
        tooltip.add(manaLevelTip);
    }

    @Override
    public ActionResult<ItemStack> use (World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (!isActive(stack)) {
                setActive(stack, true);
                int absorbedMana = absorbMana(player, stack);
                if (absorbedMana > 0) {
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.mana_vial.active"), true);
                    return ActionResult.sidedSuccess(stack, false);
                }
            } else {
                setActive(stack, false);
            }
        }
        return ActionResult.sidedSuccess(stack, level.isClientSide());

    }

    public int getMaxManalevel() {
        return flaskType.getManaCapacity();
    }

    private int absorbMana(PlayerEntity player, ItemStack vial) {
        if (vial.isEmpty()) return 0;

        int capacity = getMaxManalevel();
        int currentMana = getStoredMana(vial);
        int remainingSpace = capacity - currentMana;

        if (remainingSpace <= 0) {
            return 0; // Already full
        }

        int totalAbsorbed = 0;

        // Loop through inventory and find RAW_MANA
        for (ItemStack stack : player.inventory.items) {
            if (stack.getItem() == ModItems.RAW_MANA.get() && !stack.isEmpty()) {
                int transferable = Math.min(stack.getCount(), remainingSpace);

                if (transferable > 0) {
                    stack.shrink(transferable);
                    totalAbsorbed += transferable;
                    remainingSpace -= transferable;

                    if (remainingSpace <= 0) {
                        break; // Reached capacity
                    }
                }
            }
        }

        if (totalAbsorbed > 0) {
            addMana(vial, totalAbsorbed);
        }

        return totalAbsorbed;
    }


    public int getStoredMana(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt(MANA_TAG);
    }

    public boolean addMana(ItemStack stack, int amountToAdd) {
        if (stack.isEmpty() || amountToAdd <= 0) return false;

        CompoundNBT tag = stack.getOrCreateTag();
        int currentMana = tag.getInt(MANA_TAG);
        int maxMana = getMaxManalevel();
        int newMana = Math.min(currentMana + amountToAdd, maxMana);

        if (newMana != currentMana) {
            tag.putInt(MANA_TAG, newMana);
            stack.setTag(tag);
            return true;
        }

        return false;
    }

}
