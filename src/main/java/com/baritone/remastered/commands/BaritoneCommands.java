package com.baritone.remastered.commands;

import com.baritone.remastered.BaritoneRemastered;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BaritoneCommands {
    
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerGoToCommand(dispatcher);
            registerMineCommand(dispatcher);
            registerStopCommand(dispatcher);
            registerPauseCommand(dispatcher);
            registerResumeCommand(dispatcher);
            registerHelpCommand(dispatcher);
        });
    }
    
    private static void registerGoToCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("goto")
            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                    .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                        .executes(context -> {
                            int x = IntegerArgumentType.getInteger(context, "x");
                            int y = IntegerArgumentType.getInteger(context, "y");
                            int z = IntegerArgumentType.getInteger(context, "z");
                            
                            BlockPos target = new BlockPos(x, y, z);
                            MinecraftClient client = MinecraftClient.getInstance();
                            
                            BaritoneRemastered.getInstance().getPathfindingManager().goTo(client, target);
                            context.getSource().sendFeedback(Text.literal("§6[Baritone] §7Pathfinding to " + x + ", " + y + ", " + z));
                            
                            return 1;
                        })
                    )
                )
            )
        );
    }
    
    private static void registerMineCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("mine")
            .then(ClientCommandManager.argument("block", StringArgumentType.string())
                .executes(context -> executeMineCommand(context, 64))
                .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer(1, 128))
                    .executes(context -> {
                        int radius = IntegerArgumentType.getInteger(context, "radius");
                        return executeMineCommand(context, radius);
                    })
                )
            )
        );
    }
    
    private static int executeMineCommand(CommandContext<FabricClientCommandSource> context, int radius) {
        String blockName = StringArgumentType.getString(context, "block");
        
        // Parse block name
        List<Block> blocks = new ArrayList<>();
        
        // Handle multiple blocks separated by commas
        String[] blockNames = blockName.split(",");
        
        for (String name : blockNames) {
            name = name.trim();
            
            // Add minecraft: prefix if not present
            if (!name.contains(":")) {
                name = "minecraft:" + name;
            }
            
            Identifier id = Identifier.tryParse(name);
            if (id != null && Registries.BLOCK.containsId(id)) {
                Block block = Registries.BLOCK.get(id);
                blocks.add(block);
            } else {
                context.getSource().sendError(Text.literal("§c[Baritone] Unknown block: " + name));
                return 0;
            }
        }
        
        if (blocks.isEmpty()) {
            context.getSource().sendError(Text.literal("§c[Baritone] No valid blocks specified"));
            return 0;
        }
        
        BaritoneRemastered.getInstance().getMiningManager().startMining(blocks, radius);
        
        String blockList = String.join(", ", blockNames);
        context.getSource().sendFeedback(Text.literal("§6[Baritone] §7Mining " + blockList + " within " + radius + " blocks"));
        
        return 1;
    }
    
    private static void registerStopCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("stop")
            .executes(context -> {
                BaritoneRemastered.getInstance().getPathfindingManager().stop();
                BaritoneRemastered.getInstance().getMiningManager().stop();
                context.getSource().sendFeedback(Text.literal("§6[Baritone] §7Stopped all tasks"));
                return 1;
            })
        );
    }
    
    private static void registerPauseCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("pause")
            .executes(context -> {
                BaritoneRemastered.getInstance().getPathfindingManager().pause();
                context.getSource().sendFeedback(Text.literal("§6[Baritone] §7Paused"));
                return 1;
            })
        );
    }
    
    private static void registerResumeCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("resume")
            .executes(context -> {
                BaritoneRemastered.getInstance().getPathfindingManager().resume();
                context.getSource().sendFeedback(Text.literal("§6[Baritone] §7Resumed"));
                return 1;
            })
        );
    }
    
    private static void registerHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("baritone")
            .executes(context -> {
                context.getSource().sendFeedback(Text.literal("§6§l[Baritone Remastered]"));
                context.getSource().sendFeedback(Text.literal("§7Commands:"));
                context.getSource().sendFeedback(Text.literal("§e/goto <x> <y> <z> §7- Pathfind to coordinates"));
                context.getSource().sendFeedback(Text.literal("§e/mine <block> [radius] §7- Mine specific blocks"));
                context.getSource().sendFeedback(Text.literal("§e/stop §7- Stop all tasks"));
                context.getSource().sendFeedback(Text.literal("§e/pause §7- Pause current task"));
                context.getSource().sendFeedback(Text.literal("§e/resume §7- Resume paused task"));
                context.getSource().sendFeedback(Text.literal("§7Press §eP §7to pause/resume"));
                context.getSource().sendFeedback(Text.literal(""));
                context.getSource().sendFeedback(Text.literal("§7Example: §e/mine diamond_ore,iron_ore 64"));
                return 1;
            })
        );
    }
}
