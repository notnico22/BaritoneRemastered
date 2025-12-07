package com.baritone.remastered.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockScanner {
    
    public static List<BlockPos> scanForBlocks(World world, BlockPos center, List<Block> targetBlocks, int radius) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        
        int centerX = center.getX();
        int centerY = center.getY();
        int centerZ = center.getZ();
        
        // Scan in chunks for better performance
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                for (int y = Math.max(world.getBottomY(), centerY - radius); 
                     y <= Math.min(world.getTopY() - 1, centerY + radius); y++) {
                    
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    
                    if (targetBlocks.contains(state.getBlock())) {
                        // Check if it's exposed (has at least one air block adjacent)
                        if (isExposed(world, pos)) {
                            foundBlocks.add(pos);
                        }
                    }
                }
            }
        }
        
        return foundBlocks;
    }
    
    private static boolean isExposed(World world, BlockPos pos) {
        // Check all 6 adjacent blocks
        BlockPos[] adjacent = {
            pos.up(), pos.down(),
            pos.north(), pos.south(),
            pos.east(), pos.west()
        };
        
        for (BlockPos adjPos : adjacent) {
            if (world.getBlockState(adjPos).isAir() || 
                world.getBlockState(adjPos).isReplaceable()) {
                return true;
            }
        }
        
        return false;
    }
    
    public static List<BlockPos> scanInDirection(World world, BlockPos start, int distance, 
                                                  int lateralRange, List<Block> targetBlocks) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        
        // Scan forward in a cone shape
        for (int d = 0; d < distance; d++) {
            int range = Math.min(lateralRange, d / 2);
            
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = start.add(x, y, d + z);
                        BlockState state = world.getBlockState(pos);
                        
                        if (targetBlocks.contains(state.getBlock())) {
                            if (isExposed(world, pos)) {
                                foundBlocks.add(pos);
                            }
                        }
                    }
                }
            }
        }
        
        return foundBlocks;
    }
}
