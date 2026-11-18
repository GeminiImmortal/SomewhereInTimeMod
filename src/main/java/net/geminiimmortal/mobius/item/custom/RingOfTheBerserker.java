package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.item.util.ManaHelper;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class RingOfTheBerserker extends Item implements IActivatableRing{

    private static final String TAG_ACTIVE = "BerserkActive";
    private static final int MANA_DRAIN_PER_SECOND = 8;

    private static final UUID HAND_DAMAGE_UUID = UUID.fromString("a3b1c2d4-5e6f-4a7b-8c9d-0e1f2a3b4c5d");
    private static final UUID HAND_SPEED_UUID  = UUID.fromString("b4c2d3e5-6f7a-4b8c-9d0e-1f2a3b4c5d6e");

    public RingOfTheBerserker(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && player instanceof ServerPlayerEntity) {
            toggleAbility((ServerPlayerEntity) player, stack);
        }
        return ActionResult.sidedSuccess(stack, world.isClientSide());
    }

    public void toggleAbility(ServerPlayerEntity player, ItemStack stack) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return;
        }

        if (!isActive(stack)) {
            if (!ManaHelper.consumeMana(player, MANA_DRAIN_PER_SECOND, false)) {
                player.displayClientMessage(
                        new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"),
                        true
                );
                return;
            }
            setActive(stack, true);
            player.level.playSound(null, player.blockPosition(), ModSounds.CAPTAIN_ULTI.get(), SoundCategory.PLAYERS, 0.85F, 0.6F);
            player.level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.85F, 0.9F);

        } else {
            deactivate(player, stack, false);
        }

        player.getCooldowns().addCooldown(this, 5);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide || !(entity instanceof ServerPlayerEntity) || !isActive(stack)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        tickActiveEffects(player, stack);
    }

    private void tickActiveEffects(ServerPlayerEntity player, ItemStack stack) {
        if (!isActive(stack)) {
            return;
        }

        if (player.tickCount % 20 == 0) {
            if (!ManaHelper.consumeMana(player, MANA_DRAIN_PER_SECOND, false)) {
                deactivate(player, stack, true);
                return;
            } else {
                ManaHelper.consumeMana(player, MANA_DRAIN_PER_SECOND, false);
            }
        }

        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 25, 1, true, false));
        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 25, 2, true, false));
        player.addEffect(new EffectInstance(Effects.REGENERATION, 25, 0, true, false));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isActive(stack) || super.isFoil(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    private void deactivate(ServerPlayerEntity player, ItemStack stack, boolean outOfMana) {
        setActive(stack, false);
        player.level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.6F, 1.0F);
        if (outOfMana) {
            player.displayClientMessage(
                    new TranslationTextComponent("item.mobius.ring_of_the_berserker_no_mana_left"),
                    true
            );
        }
    }

    public static boolean isActive(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.getBoolean(TAG_ACTIVE);
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(TAG_ACTIVE, active);
    }

    @Override
    public void onKeyActivate(ServerPlayerEntity player, ItemStack stack) {
        if (!player.level.isClientSide && player instanceof ServerPlayerEntity) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return;
            }

            if (!isActive(stack)) {
                if (!ManaHelper.consumeMana(player, MANA_DRAIN_PER_SECOND, false)) {
                    player.displayClientMessage(
                            new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"),
                            true
                    );
                    return;
                }
                setActive(stack, true);
                player.level.playSound(null, player.blockPosition(), ModSounds.CAPTAIN_ULTI.get(), SoundCategory.PLAYERS, 0.85F, 0.6F);
                player.level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.85F, 0.9F);

            } else {
                deactivate(player, stack, false);
            }

            player.getCooldowns().addCooldown(this, 5);
        }
    }
}