package net.geminiimmortal.mobius.item.custom;


import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.ArcaneCircleEntity;
import net.geminiimmortal.mobius.item.StaffType;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class RejectionStaff extends ModularStaff {
    private final StaffType staffType;

    public RejectionStaff(Properties properties, StaffType staffType) {
        super(properties, staffType);
        this.staffType = staffType;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack manaVial = findManaVial(player);
        CompoundNBT tag = stack.getOrCreateTag();
        long currentTime = player.level.getGameTime();

        if (manaVial == null || getStoredMana(manaVial) < staffType.getManaCost()) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
            return ActionResult.fail(stack);
        }

        if (player.level.isClientSide()) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(ModSounds.TIER_TWO_PROT_CAST.get(), 1.0F, 1.0F);
            Minecraft.getInstance().player.playSound(this.staffType.getSound().resolve().get(), 1.0F, 1.0F);
        } else {
            if (tag.contains("LastUsedTime")) {
                long lastUsedTime = tag.getLong("LastUsedTime");
                if (currentTime - lastUsedTime < this.staffType.getCooldown()) {
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.cooldown"), true);
                    return ActionResult.fail(stack);
                }
            }

            if (manaVial.isEmpty() || getStoredMana(manaVial) < this.staffType.getManaCost()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
                return ActionResult.fail(stack);
            }

            setStoredMana(manaVial, getStoredMana(manaVial) - this.staffType.getManaCost());

            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);

            // --- Summon shield immediately ---
            ArcaneCircleEntity shield = new ArcaneCircleEntity(world, player.getX(), player.getY(), player.getZ(), player);
            Vector3d spawnPos = player.position().add(player.getLookAngle().scale(2.0)).add(0, 1.5, 0);
            shield.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            world.addFreshEntity(shield);

            // Save the shield's UUID to the item's tag
            tag.putUUID("ShieldUUID", shield.getUUID());
            stack.setTag(tag);

            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.2f);
        }

        player.startUsingItem(hand);
        return ActionResult.success(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity user, int timeCharged) {
        if (!world.isClientSide() && user instanceof PlayerEntity) {
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.hasUUID("ShieldUUID")) {
                UUID shieldUUID = tag.getUUID("ShieldUUID");
                Entity entity = ((ServerWorld) world).getEntity(shieldUUID);
                if (entity instanceof ArcaneCircleEntity) {
                    entity.remove();
                }
                tag.remove("ShieldUUID");
                stack.setTag(tag);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    // [your findManaVial, getStoredMana, setStoredMana, durability bar, cooldown, etc., stay unchanged]

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

    private void updateCooldown(ItemStack stack, World level) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                long lastUsed = tag.getLong("LastUsedTime");
                long currentTime = level.getGameTime();
                long cooldownTicks = staffType.getCooldown();
                if (currentTime - lastUsed >= cooldownTicks) {
                    tag.remove("LastUsedTime");
                    stack.setTag(tag);
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof PlayerEntity) {
            updateCooldown(stack, level);
        }
    }

    private float getCooldownProgress(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                long lastUsed = tag.getLong("LastUsedTime");
                long currentTime = Minecraft.getInstance().level.getGameTime();
                long cooldownTicks = staffType.getCooldown();
                long timeRemaining = cooldownTicks - (currentTime - lastUsed);
                if (timeRemaining <= 0) {
                    return 1.0F;
                }
                return 1.0F - ((float) timeRemaining / cooldownTicks);
            }
        }
        return 1.0F;
    }
}


