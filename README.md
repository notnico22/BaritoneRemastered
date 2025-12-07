# Baritone Remastered

A powerful pathfinding and automation mod for Minecraft 1.21.1 (Fabric).

## Features

- **Smart Pathfinding**: A* algorithm-based pathfinding with obstacle avoidance
- **Auto Mining**: Automatically finds and mines specified ores/blocks
- **Commands**: Easy-to-use command system
- **Pause/Resume**: Press P to pause/resume any task
- **Multi-block Mining**: Mine multiple block types at once

## Commands

- `/goto <x> <y> <z>` - Pathfind to coordinates
- `/mine <block> [radius]` - Start mining blocks (default radius: 64)
  - Example: `/mine diamond_ore 128`
  - Example: `/mine diamond_ore,iron_ore,gold_ore 64`
- `/stop` - Stop all tasks
- `/pause` - Pause current task
- `/resume` - Resume paused task
- `/baritone` - Show help menu

## Keybinds

- **P** - Pause/Resume

## Building

1. Make sure you have Java 21 installed
2. Run `./gradlew build` (Linux/Mac) or `gradlew.bat build` (Windows)
3. The built JAR will be in `build/libs/`

## Installation

1. Install Fabric Loader for Minecraft 1.21.1
2. Install Fabric API
3. Place the mod JAR in your `.minecraft/mods` folder

## Usage Examples

### Mining
```
/mine diamond_ore
/mine diamond_ore,iron_ore,coal_ore 96
/mine ancient_debris 128
```

### Pathfinding
```
/goto 100 64 -200
/goto ~ ~10 ~
```

## Technical Details

- Uses A* pathfinding algorithm
- Scans for exposed ore blocks within radius
- Automatically navigates to blocks and mines them
- Handles jumping, strafing, and complex movement
- Recalculates paths dynamically

## License

LGPL-3.0

## Credits

Based on the original Baritone mod concept
