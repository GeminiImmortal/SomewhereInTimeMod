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
    private boolean active = false;

    public ManaVial(Properties properties, FlaskType flaskType, Rarity rarity) {
        super(properties);
        this.flaskType = flaskType;
        this.rarity = rarity;
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
        if(!level.isClientSide()) {
            if (!active) {
                active = true;
                int absorbedMana = absorbMana(Objects.requireNonNull(player), player.getItemInHand(hand));
                if (absorbedMana > 0) {
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.mana_vial.active"), true);
                    absorbMana(player, player.getItemInHand(hand));
                    return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
                }
            } else {
                active = false;
                return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public int getMaxManalevel() {
        return flaskType.getManaCapacity();
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

        addMana(vial, currentMana);
        return totalAbsorbed;
    }

    public int getStoredMana(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt(MANA_TAG);
    }

    public boolean addMana(ItemStack stack, int amount) {
        if (stack.isEmpty()) return false; // Prevents issues with empty stacks

        CompoundNBT tag = stack.getOrCreateTag();
        int currentMana = tag.getInt(MANA_TAG);
        int maxMana = this.getMaxManalevel();
        int newMana = Math.min(currentMana + amount, maxMana);

        System.out.println("Current Mana: " + currentMana + " | Adding: " + amount + " | Max Mana: " + maxMana);

        if (newMana != currentMana) {
            tag.putInt(MANA_TAG, newMana);
            stack.setTag(tag); // Ensure the update persists

            System.out.println("Updated Mana: " + newMana);
            return true;
        }

        System.out.println("Mana unchanged.");
        return false;
    }
}
