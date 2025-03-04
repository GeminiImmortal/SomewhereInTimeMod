package net.geminiimmortal.mobius.item.custom;


import net.geminiimmortal.mobius.entity.custom.TornadoEntity;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class HurricaneStaff extends ModularStaff {
    private final StaffType staffType;

    public HurricaneStaff(Properties properties, StaffType staffType) {
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

        if (player.level.isClientSide() && !manaVial.isEmpty()) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(ModSounds.TIER_TWO_PROT_CAST.get(), 1.0F, 1.0F);
            Minecraft.getInstance().player.playSound(this.staffType.getSound().resolve().get(), 1.0F, 1.0F);
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
                summonTornadoes(world, player);
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);
        }
        return ActionResult.success(stack);
    }

    private void summonTornadoes(World world, PlayerEntity player) {
        double radius = 3.0;
        int tornadoCount = 4;

        for (int i = 0; i < tornadoCount; i++) {
            double angle = Math.toRadians((360.0 / tornadoCount) * i);
            Vector3d spawnPos = player.position()
                    .add(radius * Math.cos(angle), 0, radius * Math.sin(angle));

            TornadoEntity tornado = new TornadoEntity(world, spawnPos.x, spawnPos.y, spawnPos.z, player);
            world.addFreshEntity(tornado);
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

