# Android project justfile

# Variables
package := "xyz.courtcircuit.hackthecrous"
activity := "xyz.courtcircuit.hackthecrous.MainActivity"

# Build the debug APK
build:
    ./gradlew assembleDebug

# Install the APK to the running emulator
install: build
    ./gradlew installDebug

# Launch the app on the emulator
launch:
    adb shell am start -n {{package}}/{{activity}}

# Build, install, and launch the app
run: install launch

# Clean build artifacts
clean:
    ./gradlew clean

# Uninstall the app from the emulator
uninstall:
    adb uninstall {{package}}

# Show logs from the app
logs:
    adb logcat | grep {{package}}

# List available recipes
default:
    @just --list
