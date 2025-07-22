package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.BarrierEntity;
import net.geminiimmortal.mobius.entity.custom.spell.MajorProtectionEntity;
import net.geminiimmortal.mobius.item.StaffType;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class StaffMajorProtection extends ModularStaff {
    private final StaffType staffType;

    public StaffMajorProtection(Properties properties, StaffType staffType) {
        super(properties, staffType);
        this.staffType = staffType;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack manaVial = findManaVial(player);
        CompoundNBT tag = stack.getOrCreateTag();
        long currentTime = player.level.getGameTime();
        long lastUsed = tag.getLong("LastUsedTime");

        if (manaVial == null || getStoredMana(manaVial) < staffType.getManaCost()) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
            return ActionResult.fail(stack);
        }

        if (!player.level.isClientSide()) {

            if (tag.contains("LastUsedTime")) {
                long lastUsedTime = tag.getLong("LastUsedTime");
                if (currentTime - lastUsedTime < this.staffType.getCooldown()) {
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.cooldown"), true);
                    return ActionResult.fail(stack);
                }
            }

            if (manaVial.isEmpty()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.no_mana"), true);
                return ActionResult.fail(stack);
            }

            int currentMana = getStoredMana(manaVial);
            if (currentMana < this.staffType.getManaCost()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"), true);
                return ActionResult.fail(stack);
            }

            setStoredMana(manaVial, currentMana - this.staffType.getManaCost());

            if (!world.isClientSide) {
                summonWards(world, player);
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);
            player.addEffect(new EffectInstance(Effects.ABSORPTION, 15 * 20, 3));
            player.addEffect(new EffectInstance(Effects.REGENERATION, 15 * 20, 1));
        }
        if (world.isClientSide() && !(currentTime - lastUsed < this.staffType.getCooldown())) {
            player.playSound(ModSounds.TIER_THREE_PROT_CAST.get(), 1.0f, 1.0f);
        }
        return ActionResult.success(stack);
    }

    private void summonWards(World world, PlayerEntity player) {
        MajorProtectionEntity barrier = new MajorProtectionEntity(ModEntityTypes.BARRIER.get(), world, (ServerPlayerEntity) player);
        double spawnY = player.getY() + player.getEyeHeight() - 1.0;
        barrier.setPos(player.getX(), spawnY, player.getZ());
        world.addFreshEntity(barrier);
        for (int cooldown = 0; cooldown <= this.staffType.getCooldown() - 1000; cooldown++) {
            barrier.setPos(player.getX(), player.getY() + player.getEyeHeight() - 1.0, player.getZ());
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
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

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getCooldownProgress(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - getCooldownProgress(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFFAA00;
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof PlayerEntity) {
            updateCooldown(stack, level);
        }
    }

    private void updateCooldown(ItemStack stack, World level) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            assert tag != null;
            long lastUsed = tag.getLong("LastUsedTime");
            long currentTime = level.getGameTime();
            long cooldownTicks = staffType.getCooldown();

            if (currentTime - lastUsed >= cooldownTicks) {
                tag.remove("LastUsedTime");
                stack.setTag(tag);
            }
        }
    }

    private float getCooldownProgress(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            assert tag != null;
            long lastUsed = tag.getLong("LastUsedTime");
            long currentTime = Minecraft.getInstance().level.getGameTime();
            long cooldownTicks = staffType.getCooldown();
            long timeRemaining = cooldownTicks - (currentTime - lastUsed);

            if (timeRemaining <= 0) {
                return 1.0F;
            }

            return 1.0F - ((float) timeRemaining / cooldownTicks);
        }
        return 1.0F;
    }
}


