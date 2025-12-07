package com.baritone.remastered.pathfinding;

import com.baritone.remastered.utils.MovementHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class PathExecutor {
    private final List<BlockPos> path;
    private int currentIndex = 0;
    private boolean finished = false;
    
    public PathExecutor(List<BlockPos> path) {
        this.path = path;
    }
    
    public void tick(MinecraftClient client) {
        if (finished || client.player == null) return;
        
        if (currentIndex >= path.size()) {
            finished = true;
            return;
        }
        
        BlockPos target = path.get(currentIndex);
        Vec3d playerPos = client.player.getPos();
        Vec3d targetVec = Vec3d.ofCenter(target);
        
        // Check if we reached current waypoint
        double distance = playerPos.distanceTo(targetVec);
        if (distance < 0.8) {
            currentIndex++;
            if (currentIndex >= path.size()) {
                finished = true;
                MovementHelper.stopMovement(client);
            }
            return;
        }
        
        // Move towards target
        MovementHelper.moveTowards(client, targetVec);
        
        // Handle jumping
        if (shouldJump(client, target)) {
            client.player.jump();
        }
    }
    
    private boolean shouldJump(MinecraftClient client, BlockPos target) {
        if (client.player == null || client.world == null) return false;
        
        BlockPos playerPos = client.player.getBlockPos();
        
        // Jump if target is above us
        if (target.getY() > playerPos.getY()) {
            return client.player.isOnGround();
        }
        
        // Jump if there's a block in front
        Vec3d forward = client.player.getRotationVec(1.0f).multiply(1, 0, 1).normalize();
        BlockPos frontBlock = playerPos.add((int) forward.x, 0, (int) forward.z);
        
        if (!client.world.getBlockState(frontBlock).isAir() && 
            client.world.getBlockState(frontBlock.up()).isAir() &&
            client.player.isOnGround()) {
            return true;
        }
        
        return false;
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public boolean isOnPath(BlockPos pos) {
        for (int i = Math.max(0, currentIndex - 2); i < Math.min(path.size(), currentIndex + 5); i++) {
            if (path.get(i).isWithinDistance(pos, 3)) {
                return true;
            }
        }
        return false;
    }
    
    public List<BlockPos> getPath() {
        return path;
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }
}
