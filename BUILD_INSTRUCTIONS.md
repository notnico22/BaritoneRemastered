# Build Instructions for Baritone Remastered

## Prerequisites
1. Java 21 JDK installed
2. Git installed

## Building Steps

### Option 1: Using Gradle Wrapper (Recommended)

1. Open terminal/command prompt in the project directory
2. Initialize Gradle wrapper:
   ```
   gradle wrapper
   ```
3. Build the mod:
   - Windows: `gradlew.bat build`
   - Linux/Mac: `./gradlew build`

4. Find the JAR file in `build/libs/baritone-remastered-1.0.0.jar`

### Option 2: Using GitHub Actions (Easiest)

1. Push this project to GitHub
2. Create `.github/workflows/build.yml` with the following content:

```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew build
      - name: Upload artifacts
        uses: actions/upload-artifact@v3
        with:
          name: Baritone-Remastered
          path: build/libs/*.jar
```

3. GitHub will automatically build the JAR for you on every commit
4. Download from the Actions tab

### Option 3: Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Wait for Gradle to sync
3. Run the `build` Gradle task from the Gradle panel
4. Find JAR in `build/libs/`

## Installation

1. Install Minecraft 1.21.1
2. Install Fabric Loader for 1.21.1
3. Download Fabric API from https://modrinth.com/mod/fabric-api
4. Place Fabric API and the built Baritone Remastered JAR in `.minecraft/mods/`
5. Launch Minecraft with the Fabric profile

## Troubleshooting

- **"Gradle not found"**: Install Gradle or use `gradle wrapper` first
- **Java version error**: Make sure Java 21 is installed and JAVA_HOME is set
- **Build fails**: Delete `.gradle` and `build` folders, then try again
- **Mod doesn't load**: Check Fabric API version matches Minecraft version

## Quick Commands After Installation

In Minecraft:
- `/baritone` - Show help
- `/mine diamond_ore` - Start mining diamonds
- `/goto 0 64 0` - Pathfind to coordinates
- Press `P` to pause/resume
