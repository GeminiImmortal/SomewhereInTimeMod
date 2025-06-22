package net.geminiimmortal.mobius.item.custom.patchouli;

import net.minecraft.entity.player.ServerPlayerEntity;
import vazkii.patchouli.api.PatchouliAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MobiusGuidebook extends Item {
    public MobiusGuidebook() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, new ResourceLocation("mobius", "mobius_guide"));
        }
        return ActionResult.success(stack);
    }
}

