package net.geminiimmortal.mobius.world.worldgen.structure.structures;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.structure.config.FlyingStructureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class GovernorTower extends Structure<FlyingStructureConfig> {

    public GovernorTower() {
        super(FlyingStructureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource,
                                     long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ,
                                     Biome biome, ChunkPos chunkPos, FlyingStructureConfig featureConfig) {
        BlockPos center = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        int landHeight = chunkGenerator.getBaseHeight(center.getX(), center.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG);

        IBlockReader column = chunkGenerator.getBaseColumn(center.getX(), center.getZ());
        BlockState topBlock = column.getBlockState(center.above(landHeight));

        return topBlock.getFluidState().isEmpty();
    }

    @Override
    public IStartFactory<FlyingStructureConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<FlyingStructureConfig> {

        public Start(Structure<FlyingStructureConfig> structure, int chunkX, int chunkZ,
                     MutableBoundingBox box, int references, long seed) {
            super(structure, chunkX, chunkZ, box, references, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator,
                                   TemplateManager templates, int chunkX, int chunkZ,
                                   Biome biome, FlyingStructureConfig config) {
            // Base chunk center
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos base = new BlockPos(x, 100, z);

            int terrainY = generator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            if (base.getY() - terrainY < config.minClearance) {
                base = base.above(config.minClearance - (base.getY() - terrainY));
            }

            JigsawManager.addPieces(
                    registries,
                    new VillageConfig(() -> registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(MobiusMod.MOD_ID, "boss_dungeons/floating_island_pool")),
                            20),
                    AbstractVillagePiece::new,
                    generator,
                    templates,
                    base,
                    this.pieces,
                    this.random,
                    false,
                    true
            );

            this.calculateBoundingBox();
            MutableBoundingBox fullBox = this.getBoundingBox();
            int structureHeight = fullBox.y1 - fullBox.y0;

            int minOffset = Math.max(0, (terrainY + config.minClearance) - fullBox.y0);

            int maxOffset = 255 - fullBox.y1;

            int finalOffset = MathHelper.clamp(minOffset, minOffset, maxOffset);
            this.pieces.forEach(piece -> piece.move(0, finalOffset, 0));
            this.pieces.forEach(piece -> piece.setOrientation(Direction.WEST));
            this.calculateBoundingBox();

        }


        @Override
        public void placeInChunk(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_) {
            super.placeInChunk(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
        }
    }
}


