package net.geminiimmortal.mobius.item.custom;


import net.geminiimmortal.mobius.entity.custom.spell.SpellProjectileEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VorpalSword extends SwordItem {
    public VorpalSword(IItemTier itemTier, int damage, float speed, Properties properties) {
        super(itemTier, damage, speed, properties);
    }

    private final int COOLDOWN_SECONDS = 1;
    private final int MANA_COST = 8;

    IFormattableTextComponent vorpalSwordAbility = new TranslationTextComponent("tooltip.mobius.vorpal_sword").setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.GOLD));

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(vorpalSwordAbility);
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

            castSpell(world, player);


            world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SPELL_OF_AGGRESSION.get(), SoundCategory.PLAYERS, 50.0f, 1.0f);


            // Deduct mana and apply effect
            setStoredMana(manaVial, currentMana - MANA_COST);

            // Set cooldown
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);

            player.displayClientMessage(new TranslationTextComponent("item.mobius.ring_of_aggression.used"), true);
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

    public static void castSpell(World world, PlayerEntity player) {
        if (!world.isClientSide) {
            Vector3d look = player.getLookAngle();
            double speed = 2.5D;
            double spawnDistance = 1.0D;

            Vector3d spawnPos = player.position()
                    .add(0, player.getEyeHeight(), 0)
                    .add(look.scale(spawnDistance));

            SpellProjectileEntity projectile = new SpellProjectileEntity(world, spawnPos.x, spawnPos.y, spawnPos.z);
            projectile.setDeltaMovement(look.scale(speed));
            projectile.setOwner(player); // Optional, so it knows who shot it

            world.addFreshEntity(projectile);
        }
    }
}

