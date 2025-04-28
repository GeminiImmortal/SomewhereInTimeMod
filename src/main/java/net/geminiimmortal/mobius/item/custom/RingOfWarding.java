package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.BarrierEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RingOfWarding extends Item {
    private static final int COOLDOWN_SECONDS = 4;
    private static final int MANA_COST = 8;

    public RingOfWarding(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            long currentTime = level.getGameTime();
            CompoundNBT tag = stack.getOrCreateTag();

            if (tag.contains("LastUsedTime")) {
                long lastUsedTime = tag.getLong("LastUsedTime");
                if (currentTime - lastUsedTime < COOLDOWN_SECONDS * 20) { // 20 ticks per second
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.ring_of_repulsion.cooldown"), true);
                    return ActionResult.fail(stack);
                }
            }

            // Find a mana vial with sufficient mana
            ItemStack manaVial = findManaVial(player);
            if (manaVial.isEmpty()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.no_mana"), true);
                return ActionResult.fail(stack);
            }

            int currentMana = getStoredMana(manaVial);
            if (currentMana < MANA_COST) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"), true);
                return ActionResult.fail(stack);
            }

            World world = player.level;
            Vector3d look = player.getLookAngle();

            BarrierEntity barrier = new BarrierEntity(ModEntityTypes.BARRIER.get(), world);

            double distance = 2.5D;
            double spawnX = player.getX() + look.x * distance;
            double spawnY = player.getY() + player.getEyeHeight() - 1.0;
            double spawnZ = player.getZ() + look.z * distance;

            barrier.setPos(spawnX, spawnY, spawnZ);
            world.addFreshEntity(barrier);


            world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.BARRIER.get(), SoundCategory.PLAYERS, 50.0f, 1.0f);


            // Deduct mana and apply effect
            setStoredMana(manaVial, currentMana - MANA_COST);

            // Set cooldown
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);

            player.displayClientMessage(new TranslationTextComponent("item.mobius.ring_of_repulsion.used"), true);
        }

        return ActionResult.sidedSuccess(stack, level.isClientSide());
    }

    private ItemStack findManaVial(PlayerEntity player) {
        for (ItemStack stack : player.inventory.items) {
            if (stack.getItem() instanceof ManaVial && getStoredMana(stack) > 0) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private int getStoredMana(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt("StoredMana");
    }

    private void setStoredMana(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("StoredMana", amount);
    }
}
