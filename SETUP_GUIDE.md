# Baritone Remastered - Complete Setup Guide

## What This Mod Does

Baritone Remastered is an automation mod for Minecraft that can:
- Automatically pathfind to any coordinates
- Mine specific ores/blocks automatically
- Navigate around obstacles and through caves
- Handle complex terrain with jumping and swimming

## Quick Start (GitHub Method - Easiest!)

1. **Push to GitHub:**
   ```bash
   cd C:\Users\greys\Desktop\baritoneremastered
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/baritone-remastered.git
   git push -u origin main
   ```

2. **GitHub will automatically build the JAR:**
   - Go to the "Actions" tab on GitHub
   - Wait for the build to complete (takes ~2-5 minutes)
   - Download the JAR from the build artifacts

3. **Install:**
   - Place the JAR in `.minecraft/mods/`
   - Make sure you have Fabric Loader and Fabric API installed
   - Launch Minecraft 1.21.1

## Local Build Method

If you prefer to build locally:

1. **Install Java 21:**
   - Download from: https://adoptium.net/
   - Make sure `java -version` shows version 21

2. **Generate Gradle Wrapper:**
   ```bash
   cd C:\Users\greys\Desktop\baritoneremastered
   gradle wrapper
   ```

3. **Build:**
   ```bash
   gradlew.bat build    # Windows
   ./gradlew build      # Linux/Mac
   ```

4. **Get JAR:**
   - Located in: `build/libs/baritone-remastered-1.0.0.jar`

## Usage Examples

### Mining Ores
```
/mine diamond_ore           # Mine diamonds in 64 block radius
/mine diamond_ore 128       # Mine diamonds in 128 block radius
/mine iron_ore,gold_ore     # Mine multiple ores
/mine ancient_debris 96     # Mine ancient debris in the nether
```

### Pathfinding
```
/goto 100 64 200            # Go to specific coordinates
/goto ~ ~10 ~               # Go 10 blocks up
/pause                      # Pause the current task
/resume                     # Resume
/stop                       # Stop everything
```

### Keybinds
- Press **P** to quickly pause/resume

## Project Structure

```
baritoneremastered/
├── src/main/java/com/baritone/remastered/
│   ├── BaritoneRemastered.java       # Main mod class
│   ├── pathfinding/
│   │   ├── PathfindingManager.java   # Manages pathfinding
│   │   ├── AStarPathfinder.java      # A* algorithm
│   │   └── PathExecutor.java         # Executes paths
│   ├── mining/
│   │   └── MiningManager.java        # Auto mining logic
│   ├── commands/
│   │   └── BaritoneCommands.java     # Command registration
│   └── utils/
│       ├── MovementHelper.java       # Player movement
│       ├── BlockScanner.java         # Scans for blocks
│       └── PlayerUtils.java          # Player utilities
├── src/main/resources/
│   └── fabric.mod.json               # Mod metadata
├── build.gradle                       # Build configuration
└── gradle.properties                  # Version settings
```

## Features Explained

### Pathfinding
- Uses A* algorithm for optimal paths
- Handles obstacles, jumping, and falling
- Dynamically recalculates if you get off-path
- Avoids lava and dangerous blocks

### Mining
- Scans in a radius for specified blocks
- Only mines "exposed" blocks (visible/accessible)
- Automatically navigates to blocks
- Supports multiple block types at once
- Searches caves and exposed areas

### Commands
- All commands use Fabric's client command system
- Work in singleplayer and multiplayer (if allowed)
- Full tab completion support

## Troubleshooting

**Mod won't load:**
- Check you have Fabric Loader for 1.21.1
- Install Fabric API (required dependency)
- Make sure Java 21 is installed

**Build fails:**
- Run `gradle wrapper` first
- Delete `.gradle` and `build` folders
- Try again

**Mining doesn't find blocks:**
- Increase the radius: `/mine diamond_ore 128`
- Blocks must be "exposed" (visible in caves/ravines)
- The mod won't mine through solid stone

**Pathfinding gets stuck:**
- Press P to pause
- Type `/stop` to cancel
- The A* algorithm needs line of sight

## Advanced Usage

### Branch Mining
```
/goto <x> <y> <z>   # Navigate to mining level
/mine diamond_ore 64  # Start mining
```

### Multiple Ores
```
/mine diamond_ore,iron_ore,gold_ore,lapis_ore 96
```

### Nether Mining
```
/mine ancient_debris 128
/mine nether_gold_ore,ancient_debris 64
```

## Performance Notes

- Larger scan radius = slower performance
- Recommended radius: 64-96 blocks
- Mining scans every 1 second
- Pathfinding recalculates every 1 second

## License

This mod is open source under the MIT License.

## Credits

Inspired by the original Baritone mod by leijurv and contributors.

## Support

If you find bugs or have suggestions:
1. Check the console for errors
2. Report on GitHub Issues
3. Include Minecraft version and mod version

Enjoy automating your Minecraft adventures! 🎮⛏️
