package com.baritone.remastered.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class PlayerUtils {
    
    public static BlockPos getPlayerBlockPos(MinecraftClient client) {
        if (client.player == null) return BlockPos.ORIGIN;
        return client.player.getBlockPos();
    }
    
    public static boolean canReachBlock(MinecraftClient client, BlockPos pos) {
        if (client.player == null) return false;
        return client.player.getPos().distanceTo(pos.toCenterPos()) <= 4.5;
    }
    
    public static boolean isPlayerOnGround(MinecraftClient client) {
        if (client.player == null) return false;
        return client.player.isOnGround();
    }
}
