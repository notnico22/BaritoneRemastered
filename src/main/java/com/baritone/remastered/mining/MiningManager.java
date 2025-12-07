package com.baritone.remastered.mining;

import com.baritone.remastered.BaritoneRemastered;
import com.baritone.remastered.utils.BlockScanner;
import com.baritone.remastered.utils.MovementHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class MiningManager {
    private boolean active = false;
    private List<Block> targetBlocks = new ArrayList<>();
    private BlockPos currentTarget = null;
    private int miningTicks = 0;
    private int searchRadius = 64;
    private int scanCounter = 0;
    
    public void tick(MinecraftClient client) {
        if (!active || client.player == null || client.world == null) {
            return;
        }
        
        scanCounter++;
        
        // Scan for new targets every 20 ticks (1 second)
        if (scanCounter % 20 == 0) {
            if (currentTarget == null || !isValidTarget(client, currentTarget)) {
                findNextTarget(client);
            }
        }
        
        if (currentTarget != null) {
            mineBlock(client, currentTarget);
        }
    }
    
    private void findNextTarget(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        
        List<BlockPos> foundBlocks = BlockScanner.scanForBlocks(
            client.world, 
            client.player.getBlockPos(), 
            targetBlocks, 
            searchRadius
        );
        
        if (!foundBlocks.isEmpty()) {
            // Find closest block
            BlockPos playerPos = client.player.getBlockPos();
            BlockPos closest = null;
            double minDistance = Double.MAX_VALUE;
            
            for (BlockPos pos : foundBlocks) {
                double distance = pos.getSquaredDistance(playerPos);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = pos;
                }
            }
            
            currentTarget = closest;
            miningTicks = 0;
            
            // Navigate to the block
            if (closest != null && !isInReach(client, closest)) {
                BaritoneRemastered.getInstance().getPathfindingManager().goTo(client, closest);
            }
        } else {
            currentTarget = null;
            if (client.player != null) {
                client.player.sendMessage(
                    net.minecraft.text.Text.literal("§6[Baritone] §7No more target blocks found"), 
                    false
                );
            }
            stop();
        }
    }
    
    private void mineBlock(MinecraftClient client, BlockPos pos) {
        if (client.player == null || client.world == null) return;
        
        if (!isValidTarget(client, pos)) {
            currentTarget = null;
            return;
        }
        
        // Check if we're in range
        if (!isInReach(client, pos)) {
            // Navigate closer
            if (!BaritoneRemastered.getInstance().getPathfindingManager().isActive()) {
                BaritoneRemastered.getInstance().getPathfindingManager().goTo(client, pos);
            }
            return;
        } else {
            // Stop pathfinding if we're in range
            if (BaritoneRemastered.getInstance().getPathfindingManager().isActive()) {
                BaritoneRemastered.getInstance().getPathfindingManager().stop();
            }
        }
        
        // Look at the block
        MovementHelper.lookAt(client, Vec3d.ofCenter(pos));
        
        // Mine the block
        if (client.interactionManager != null) {
            miningTicks++;
            
            // Start mining
            if (miningTicks == 1) {
                client.interactionManager.attackBlock(pos, net.minecraft.util.math.Direction.UP);
            }
            
            // Continue mining
            client.interactionManager.updateBlockBreakingProgress(pos, net.minecraft.util.math.Direction.UP);
            client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
            
            // Check if block is broken
            if (client.world.getBlockState(pos).isAir()) {
                currentTarget = null;
                miningTicks = 0;
            }
        }
    }
    
    private boolean isValidTarget(MinecraftClient client, BlockPos pos) {
        if (client.world == null) return false;
        BlockState state = client.world.getBlockState(pos);
        return !state.isAir() && targetBlocks.contains(state.getBlock());
    }
    
    private boolean isInReach(MinecraftClient client, BlockPos pos) {
        if (client.player == null) return false;
        return client.player.getPos().distanceTo(Vec3d.ofCenter(pos)) <= 4.5;
    }
    
    public void startMining(List<Block> blocks, int radius) {
        this.targetBlocks = new ArrayList<>(blocks);
        this.searchRadius = radius;
        this.active = true;
        this.currentTarget = null;
        this.miningTicks = 0;
        this.scanCounter = 0;
    }
    
    public void stop() {
        this.active = false;
        this.currentTarget = null;
        this.miningTicks = 0;
        this.targetBlocks.clear();
    }
    
    public boolean isActive() {
        return active;
    }
    
    public BlockPos getCurrentTarget() {
        return currentTarget;
    }
}
