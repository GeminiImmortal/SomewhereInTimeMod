package net.geminiimmortal.mobius.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManaTap extends Item {
    private final Block livingManawoodLog;
    private final Block manawoodLog;
    private final Item rawMana;

    public ManaTap(Properties properties, Block livingManawoodLog, Block manawoodLog, Item rawMana) {
        super(properties);
        this.livingManawoodLog = livingManawoodLog;
        this.manawoodLog = manawoodLog;
        this.rawMana = rawMana;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() == livingManawoodLog) {
            if (!world.isClientSide) {
                world.setBlock(pos, manawoodLog.defaultBlockState(), 3);
                if (player != null) {
                    player.getItemInHand(hand).hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    player.addItem(new ItemStack(rawMana, 4));
                }
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}

