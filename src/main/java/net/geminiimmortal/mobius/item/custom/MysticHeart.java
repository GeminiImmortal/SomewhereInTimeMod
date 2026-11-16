package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.geminiimmortal.mobius.item.StaffType;
import net.geminiimmortal.mobius.item.util.ManaHelper;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MysticHeart extends ModularStaff {
    private final StaffType staffType;

    public MysticHeart(Properties properties, StaffType staffType) {
        super(properties, staffType);
        this.staffType = staffType;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundNBT tag = stack.getOrCreateTag();
        if (player.level.isClientSide()) {
            boolean isSuccess = ManaHelper.consumeMana(player, this.staffType.getManaCost(), true);
            if (!isSuccess) {
                return ActionResult.fail(player.getItemInHand(hand));
            }
        }
        if (!player.level.isClientSide()) {
            boolean isSuccess = ManaHelper.consumeMana(player, this.staffType.getManaCost(), false);
            if (!isSuccess) {
                return ActionResult.fail(player.getItemInHand(hand));
            }
        }
        FaestagEntity stag = new FaestagEntity(ModEntityTypes.FAESTAG.get(), player.level);
        if (tag.contains("StagUUID") && !player.level.isClientSide()) {
            FaestagEntity currentStag = (FaestagEntity) ((ServerWorld) player.level).getEntity(tag.getUUID("StagUUID"));
            if (currentStag != null) {
                tag.remove("StagUUID");
                currentStag.remove();
            } else {
                tag.remove("StagUUID");
            }
            return ActionResult.success(stack);
        }

        if (player.level.isClientSide()) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(ModSounds.MYSTIC_TEAR_AMBIENT.get(), 1.0F, 1.0F);
            Minecraft.getInstance().player.playSound(this.staffType.getSound().resolve().get(), 1.0F, 1.0F);
        } else {
            if (!tag.contains("StagUUID")) {

                Vector3d spawnPos = player.position().add(player.getLookAngle().scale(2.0)).add(0, 1.5, 0);
                stag.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                world.addFreshEntity(stag);
                stag.setSummoned(true);

                // Save the shield's UUID to the item's tag
                tag.putUUID("StagUUID", stag.getUUID());
                stack.setTag(tag);

                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.2f);

                stack.setTag(tag);
                return ActionResult.success(stack);
            }
        }

        player.startUsingItem(hand);
        return ActionResult.success(stack);
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

