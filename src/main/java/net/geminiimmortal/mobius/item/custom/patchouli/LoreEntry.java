package net.geminiimmortal.mobius.item.custom.patchouli;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class LoreEntry extends Item {
    private final LoreEntryType loreEntryType;

    public LoreEntry(Properties properties, LoreEntryType loreEntryType) {
        super(properties);
        this.loreEntryType = loreEntryType;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(stack);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {return 32;}

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!((PlayerEntity) entity).isCreative()) {
            stack.shrink(1);
            if (!world.isClientSide()) {
                grantAdvancementIfPossible((ServerPlayerEntity) entity, new ResourceLocation(this.loreEntryType.getEntryAdv().toString()));
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);

                ((ServerPlayerEntity) entity).sendMessage(new TranslationTextComponent("lore.mobius.lore_unlocked"), entity.getUUID());
            }
        }
        return stack;
    }

    public static void grantAdvancementIfPossible(ServerPlayerEntity player, ResourceLocation advancementId) {
        MinecraftServer server = player.server;

        if (server != null) {
            Advancement advancement = server.getAdvancements().getAdvancement(advancementId);
            if (advancement != null) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                if (!progress.isDone()) {
                    for (String criterion : progress.getRemainingCriteria()) {
                        player.getAdvancements().award(advancement, criterion);
                    }
                }
            } else {
                System.err.println("Advancement not found: " + advancementId);
            }
        } else {
            System.err.println("Server is null for player: " + player.getName().getString());
        }
    }

}
