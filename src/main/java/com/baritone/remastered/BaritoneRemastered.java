package com.baritone.remastered;

import com.baritone.remastered.commands.BaritoneCommands;
import com.baritone.remastered.mining.MiningManager;
import com.baritone.remastered.pathfinding.PathfindingManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaritoneRemastered implements ClientModInitializer {
    public static final String MOD_ID = "baritone-remastered";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static BaritoneRemastered instance;
    private PathfindingManager pathfindingManager;
    private MiningManager miningManager;
    
    private static KeyBinding pauseKeyBinding;
    
    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("Initializing Baritone Remastered");
        
        // Initialize managers
        pathfindingManager = new PathfindingManager();
        miningManager = new MiningManager();
        
        // Register keybindings
        pauseKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.baritone-remastered.pause",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.baritone-remastered"
        ));
        
        // Register commands
        BaritoneCommands.register();
        
        // Register tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                pathfindingManager.tick(client);
                miningManager.tick(client);
                
                // Handle pause keybinding
                while (pauseKeyBinding.wasPressed()) {
                    if (pathfindingManager.isActive()) {
                        pathfindingManager.pause();
                        if (client.player != null) {
                            client.player.sendMessage(net.minecraft.text.Text.literal("§6[Baritone] §7Paused"), false);
                        }
                    } else {
                        pathfindingManager.resume();
                        if (client.player != null) {
                            client.player.sendMessage(net.minecraft.text.Text.literal("§6[Baritone] §7Resumed"), false);
                        }
                    }
                }
            }
        });
        
        LOGGER.info("Baritone Remastered initialized successfully");
    }
    
    public static BaritoneRemastered getInstance() {
        return instance;
    }
    
    public PathfindingManager getPathfindingManager() {
        return pathfindingManager;
    }
    
    public MiningManager getMiningManager() {
        return miningManager;
    }
}
