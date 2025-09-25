# ZePatch

Make a composable. Get a real embroidered patch.

ZePatch is a fun Android + Kotlin project from GDG Berlin Android. During events like droidcon, attendees submit small Jetpack Compose snippets (we call them patchables) and receive the result back as a real-world fabric patch. Under the hood, we render composables to bitmaps and convert them to stitch files that an embroidery machine can sew.


## Badge Rainbow

[![License: Apache-2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-A97BFF?logo=kotlin)](gradle/libs.versions.toml)
[![Compose BOM](https://img.shields.io/badge/Compose_BOM-2025.08.01-4285F4?logo=jetpackcompose)](gradle/libs.versions.toml)
[![Android Gradle Plugin](https://img.shields.io/badge/AGP-8.10.1-3DDC84?logo=android)](gradle/libs.versions.toml)
[![minSdk](https://img.shields.io/badge/minSdk-24-informational)](app/build.gradle.kts)
[![targetSdk](https://img.shields.io/badge/targetSdk-36-informational)](app/build.gradle.kts)
[![Latest Release](https://img.shields.io/github/v/release/gdg-berlin-android/ZePatch?sort=semver)](https://github.com/gdg-berlin-android/ZePatch/releases/latest)


## What’s a "patchable"?

A patchable is simply a Composable function annotated with @Patch that ZePatch can capture and turn into stitches.
- You write a small composable (texts, shapes, icons, etc.).
- It must contain only one `SafeArea` composable.
- You can add interactive elements. Only the part in the `SafeArea` composable is converted to a patch
- Annotate it with `@Patch("YourNameOrTitle")`
- The build’s KSP processor discovers it and adds it to a registry.
- The app can render it to a bitmap and the converter turns that into a stitch format (e.g., PES) for embroidery.

See [examples](app/src/main/java/de/berlindroid/zepatch/patchable)

In its simplest form, a patchable looks like this:
```kotlin
@Patch("Hello World") // you need this to register it
@Composable
fun HelloWorld(
    shouldCapture: Boolean = false, // used to activate the convert to bitmap
    onBitmap: (ImageBitmap) -> Unit = {}, // used to return the bitmap from the SafeArea
) {
    // Safe Area is the part that becomes the patch
    SafeArea(
        shouldCapture = shouldCapture, // You need to pass this through from the parent or it won't work
        onBitmap = onBitmap, // You need to pass this through from the parent or it won't work
    ) {
        Text("Hello World!", fontSize = 48.sp) // <- Your creative input goes here
    }
    // Add interactive pieces here. This will not be part of the patch
}

```

## First steps for attendees
0. Follow local setup to set Python local variable: [Local setup and building](#local-setup-and-building)
1. Get the code
   - Open in Android Studio (Narwhal+). Let Gradle sync.  
   - Or build from CLI: ./gradlew assembleDebug
2. Run the app
   - Use a device or emulator on Android 8.0 (API 26) or higher.
3. Make your own patchable
   - Create a Composable and annotate it with `@Patch("YourNameOrTitle")`.
   - Keep it simple, bold, and high-contrast for best stitching results.
   - Rebuild so KSP regenerates the patch registry.
4. Preview and submit
   - Use the app’s UI to preview and export your patch design.
   - Event staff will provide a USB stick, save your patch on the device.
   - Event staff will embroider your patch.

> [!TIP]
> Avoid tiny text and hairline strokes. Solid fills and thick lines stitch best.


## Project overview

- app: Jetpack Compose UI and the patch preview/generation flow.
- patch-annotations: Kotlin annotation(s) like @Patch used to mark composables.
- patch-processor: KSP processor that finds @Patch composables and generates a registry.
- converter: Image and stitch conversion utilities, including a Python script to write stitch formats.


## Download the latest release

Grab the newest APK from the Releases page:  
https://github.com/gdg-berlin-android/ZePatch/releases/latest


## Pick up an issue (great for attendees!)

- [🤓 Good First Issue 🫶](https://github.com/gdg-berlin-android/ZePatch/issues?q=is%3Aissue%20is%3Aopen%20label%3A%22%F0%9F%A4%93%20Good%20First%20Issue%20%F0%9F%AB%B6%22) for something easy to start
- [🦾 droidcon 🤖](https://github.com/gdg-berlin-android/ZePatch/issues?q=is%3Aissue%20is%3Aopen%20label%3A%22%F0%9F%A6%BE%20droidcon%20%F0%9F%A4%96%22) specific for droidcon

If you’re unsure where to start, grab any "good first issue" or ping maintainers on the issue.


## Local setup and building

- Requirements
  - Android Studio Narwhal+ recommended.
  - JDK bundled with Android Studio is fine.
  - Python 3.8+ is required for certain converter features.
- Build
  - Android Studio: Build the app module.
  - CLI: ./gradlew assembleDebug
- Tests
  - Unit tests: ./gradlew test
  - Instrumentation tests: ./gradlew connectedAndroidTest (device/emulator required)


## Python path (converter)

If you see missing Python errors, set an environment variable ZEPATCH_PYTHON_PATH to the Python 3.8+ executable on your machine.

Example (macOS/Linux):

```bash
export ZEPATCH_PYTHON_PATH=/usr/bin/python3
```


## Contributing

Small, focused PRs are welcome. Please include a brief description and screenshots or screen recordings for UI changes when practical. Don’t commit build outputs or generated files.
