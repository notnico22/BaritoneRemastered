# Generating Gradle Wrapper JAR

The gradle wrapper JAR file is missing. Here are ways to fix this:

## Method 1: Use GitHub Actions (RECOMMENDED)

1. Push your code to GitHub:
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/YOUR_USERNAME/baritone-remastered.git
   git push -u origin main
   ```

2. The GitHub Actions workflow will automatically:
   - Generate the wrapper if missing
   - Build the mod
   - Provide downloadable JAR artifacts

## Method 2: Generate Wrapper Locally

If you have Gradle installed on your system:

```bash
cd C:\Users\greys\Desktop\baritoneremastered
gradle wrapper --gradle-version 8.8
```

This will download and create the `gradle/wrapper/gradle-wrapper.jar` file.

## Method 3: Download Wrapper JAR Manually

Download the gradle-wrapper.jar from:
https://raw.githubusercontent.com/gradle/gradle/v8.8.0/gradle/wrapper/gradle-wrapper.jar

Save it to: `gradle/wrapper/gradle-wrapper.jar`

## Method 4: Use IntelliJ IDEA or Android Studio

1. Open the project in IntelliJ IDEA
2. The IDE will automatically detect and generate the Gradle wrapper
3. Use the Gradle panel to run the `build` task

## After Generating the Wrapper

Run:
```bash
./gradlew build      # Linux/Mac
gradlew.bat build    # Windows
```

The JAR will be in `build/libs/baritone-remastered-1.0.0.jar`

## Quick GitHub Setup

```bash
# Initialize git
cd C:\Users\greys\Desktop\baritoneremastered
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - Baritone Remastered mod"

# Create GitHub repo first, then:
git remote add origin https://github.com/YOUR_USERNAME/baritone-remastered.git
git branch -M main
git push -u origin main

# Go to GitHub Actions tab and watch it build!
```

The GitHub Actions method is recommended because:
- No local setup needed
- Builds on GitHub servers
- Provides downloadable artifacts
- Works on any platform
- Automatically handles wrapper generation
