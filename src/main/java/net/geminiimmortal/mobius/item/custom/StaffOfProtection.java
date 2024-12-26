package net.geminiimmortal.mobius.item.custom;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public class StaffOfProtection extends Item {
    private static final int COOLDOWN_SECONDS = 120; // 2 minutes in seconds

    public StaffOfProtection(Properties properties) {
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
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.cooldown") {
                    }, true);
                    return ActionResult.fail(stack);
                }
            }

            // Apply absorption effect
            player.addEffect(new EffectInstance(Effects.ABSORPTION, 20 * 20, 3)); // 20 seconds, level 2
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundCategory.AMBIENT, 1.0f, 1.0f);

            // Set cooldown
            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);

            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.used"), true);
        }

        return ActionResult.sidedSuccess((stack), level.isClientSide());
    }
}

