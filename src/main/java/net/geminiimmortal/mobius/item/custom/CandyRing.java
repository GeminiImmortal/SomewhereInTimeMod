package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.custom.MobiusPortalBlock;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CandyRing extends Item {
    public CandyRing() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .tab(ItemGroup.TAB_MISC)
        );
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getPlayer() != null) {
            // Check if the player is in the correct dimension
            if (context.getPlayer().level.dimension() == ModDimensions.MOBIUS_WORLD ||
                    context.getPlayer().level.dimension() == World.OVERWORLD) {

                BlockPos clickedPos = context.getClickedPos(); // Get the clicked position

                for (Direction direction : Direction.Plane.VERTICAL) {
                    BlockPos framePos = clickedPos.relative(direction); // Get the frame position
                    Block portalBlock = ModBlocks.MOBIUS_PORTAL.get();
                    if (portalBlock instanceof MobiusPortalBlock) {
                        // Attempt to spawn the portal
                        if (((MobiusPortalBlock) ModBlocks.MOBIUS_PORTAL.get()).trySpawnPortal(context.getLevel(), framePos)) {
                            context.getLevel().playSound(context.getPlayer(), framePos,
                                    SoundEvents.PORTAL_TRIGGER, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            return ActionResultType.CONSUME; // Consume the item if successful
                        }
                    }
                }
            }
        }
        return ActionResultType.FAIL; // Fail if not successful
    }
}

