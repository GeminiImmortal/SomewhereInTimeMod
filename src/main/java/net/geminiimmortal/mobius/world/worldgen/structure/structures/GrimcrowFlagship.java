package net.geminiimmortal.mobius.world.worldgen.structure.structures;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GrimcrowFlagship extends Structure<NoFeatureConfig> {

    public GrimcrowFlagship() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource,
                                     long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ,
                                     Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        int landHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG);

        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));

        return topBlock.getFluidState().isEmpty();
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }


    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ,
                     MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator,
                                   TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn,
                                   NoFeatureConfig config) {

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockpos = new BlockPos(x, 0, z);

            PlacementSettings placement = new PlacementSettings()
                    .setMirror(Mirror.NONE)
                    .setIgnoreEntities(false);
            
            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(MobiusMod.MOD_ID, "grimcrow_flagship_front")),
                            20),
                    AbstractVillagePiece::new,
                    chunkGenerator, templateManagerIn,
                    blockpos, this.pieces, this.random, false, true
            );

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(MobiusMod.MOD_ID, "grimcrow_flagship_frigate")),
                            20),
                    AbstractVillagePiece::new,
                    chunkGenerator, templateManagerIn,
                    blockpos.immutable().offset(18, -36, 18), this.pieces, this.random, false, true
            );

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(MobiusMod.MOD_ID, "grimcrow_flagship_frigate")),
                            20),
                    AbstractVillagePiece::new,
                    chunkGenerator, templateManagerIn,
                    blockpos.immutable().offset(-28, -26, -28), this.pieces, this.random, false, true
            );

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(MobiusMod.MOD_ID, "grimcrow_flagship_frigate")),
                            20),
                    AbstractVillagePiece::new,
                    chunkGenerator, templateManagerIn,
                    blockpos.immutable().offset(-30, -10, 30), this.pieces, this.random, false, true
            );

            this.calculateBoundingBox();

            int desiredOffset = 120;

            MutableBoundingBox fullBox = this.getBoundingBox();
            int structureHeight = fullBox.y1 - fullBox.y0;

            int predictedTop = fullBox.y0 + desiredOffset + structureHeight;

            int safeOffset = desiredOffset;
            if (predictedTop > 255) {
                safeOffset -= (predictedTop - 255);
            }

            int finalSafeOffset = safeOffset;
            this.pieces.forEach(piece -> piece.move(0, finalSafeOffset, 0));
            this.pieces.forEach(piece -> piece.getBoundingBox().y0 -= 1);
            this.pieces.forEach(piece -> piece.setOrientation(Direction.SOUTH));

            this.calculateBoundingBox();
        }
    }
}

