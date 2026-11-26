package net.geminiimmortal.mobius.block.custom.crop;

import net.geminiimmortal.mobius.api.crop.ICropCompat;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.HashSet;
import java.util.Set;

public class ManaWartCrop extends CropsBlock implements ICropCompat {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
    };


    public ManaWartCrop(Properties builder) {
        super(builder);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.getAgeProperty(), 0));
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return null;
    }

    public IntegerProperty getAgeProperty() {
    return AGE;
}

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return ManaWartCrop.SHAPE_BY_AGE[(Integer)state.getValue(this.getAgeProperty())];
    }

    @Override
    public String getCropName() {
        return "mana_wart";
    }

    @Override
    public CropCategory getCropCategory() {
        return CropCategory.VEGETABLE;
    }

    @Override
    public IItemProvider getProduceItem() {
        return ModItems.MANA_WART.get();
    }

    @Override
    public Set<Season> getFertileSeasons() {
        return new HashSet<>();
    }
}
