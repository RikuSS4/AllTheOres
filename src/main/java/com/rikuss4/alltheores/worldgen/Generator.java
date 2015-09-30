package com.rikuss4.alltheores.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;

import cpw.mods.fml.common.IWorldGenerator;

public class Generator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        for (ATOOre ore : Reference.ORES_LIST) {
            if (ore.veinRate != 0 && ore.veinHeight != 0 && ore.veinSize != 0 && (ore.dimWhiteList.contains(world.provider.dimensionId) || (ore.dimWhiteList.isEmpty() && !ore.dimBlackList.contains(world.provider.dimensionId)))) {
                for (int i = 0; i <= ore.veinRate; i++) {
                    new WorldGenMinable(ore, 0, ore.veinSize, Block.getBlockFromName(ore.underlyingBlockName)).generate(world, random, chunkX * 16 + random.nextInt(16), random.nextInt(ore.veinHeight), chunkZ * 16 + random.nextInt(16));
                }
            }
        }
    }
}
