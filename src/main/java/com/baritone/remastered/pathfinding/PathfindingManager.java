package com.baritone.remastered.pathfinding;

import com.baritone.remastered.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class PathfindingManager {
    private PathExecutor currentPath;
    private BlockPos targetPos;
    private boolean active = false;
    private boolean paused = false;
    private int tickCounter = 0;
    
    public void tick(MinecraftClient client) {
        if (!active || paused || client.player == null) {
            return;
        }
        
        tickCounter++;
        
        // Execute current path
        if (currentPath != null && !currentPath.isFinished()) {
            currentPath.tick(client);
            
            // Check if we need to recalculate
            if (tickCounter % 20 == 0) { // Recalculate every second
                if (targetPos != null && !currentPath.isOnPath(client.player.getBlockPos())) {
                    calculatePath(client, targetPos);
                }
            }
        } else if (currentPath != null && currentPath.isFinished()) {
            stop();
        }
    }
    
    public void goTo(MinecraftClient client, BlockPos target) {
        if (client.player == null) return;
        
        this.targetPos = target;
        this.active = true;
        this.paused = false;
        
        calculatePath(client, target);
    }
    
    private void calculatePath(MinecraftClient client, BlockPos target) {
        if (client.player == null || client.world == null) return;
        
        BlockPos start = client.player.getBlockPos();
        List<BlockPos> path = AStarPathfinder.findPath(client.world, start, target);
        
        if (path != null && !path.isEmpty()) {
            currentPath = new PathExecutor(path);
        } else {
            stop();
            client.player.sendMessage(net.minecraft.text.Text.literal("§c[Baritone] Failed to find path!"), false);
        }
    }
    
    public void stop() {
        active = false;
        paused = false;
        currentPath = null;
        targetPos = null;
        tickCounter = 0;
    }
    
    public void pause() {
        paused = true;
    }
    
    public void resume() {
        paused = false;
    }
    
    public boolean isActive() {
        return active && !paused;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public BlockPos getTarget() {
        return targetPos;
    }
}
