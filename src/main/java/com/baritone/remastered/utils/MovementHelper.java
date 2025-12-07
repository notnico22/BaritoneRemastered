package com.baritone.remastered.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MovementHelper {
    
    public static void moveTowards(MinecraftClient client, Vec3d target) {
        if (client.player == null) return;
        
        Vec3d playerPos = client.player.getPos();
        double dx = target.x - playerPos.x;
        double dz = target.z - playerPos.z;
        
        // Calculate angle to target
        double angle = Math.atan2(dz, dx) - Math.PI / 2;
        
        // Get player's current yaw
        float yaw = (float) Math.toDegrees(angle);
        
        // Smoothly rotate towards target
        lookAt(client, target);
        
        // Move forward
        client.player.input.pressingForward = true;
        client.player.input.pressingBack = false;
        client.player.input.pressingLeft = false;
        client.player.input.pressingRight = false;
        
        // Handle strafing for better movement
        float yawDiff = MathHelper.wrapDegrees(yaw - client.player.getYaw());
        
        if (Math.abs(yawDiff) > 45) {
            if (yawDiff > 0) {
                client.player.input.pressingLeft = true;
            } else {
                client.player.input.pressingRight = true;
            }
        }
    }
    
    public static void lookAt(MinecraftClient client, Vec3d target) {
        if (client.player == null) return;
        
        Vec3d eyes = client.player.getEyePos();
        double dx = target.x - eyes.x;
        double dy = target.y - eyes.y;
        double dz = target.z - eyes.z;
        
        double distance = Math.sqrt(dx * dx + dz * dz);
        
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));
        
        // Smoothly interpolate
        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();
        
        float yawDiff = MathHelper.wrapDegrees(yaw - currentYaw);
        float pitchDiff = MathHelper.wrapDegrees(pitch - currentPitch);
        
        float maxRotation = 20.0f; // degrees per tick
        
        yawDiff = MathHelper.clamp(yawDiff, -maxRotation, maxRotation);
        pitchDiff = MathHelper.clamp(pitchDiff, -maxRotation, maxRotation);
        
        client.player.setYaw(currentYaw + yawDiff);
        client.player.setPitch(currentPitch + pitchDiff);
    }
    
    public static void stopMovement(MinecraftClient client) {
        if (client.player == null) return;
        
        client.player.input.pressingForward = false;
        client.player.input.pressingBack = false;
        client.player.input.pressingLeft = false;
        client.player.input.pressingRight = false;
        client.player.input.jumping = false;
    }
}
