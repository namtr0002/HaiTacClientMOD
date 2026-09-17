# HTTH LibGDX Port (scaffold)

## Desktop run
- cd libgdx
- Use Java 17+
- Run: gradle :desktop:run

## Assets
- Place textures, fonts, and data under `core/src/main/resources/assets`
- Example path when loading: `Gdx.files.internal("assets/your.png")`

## Build artifacts
- Desktop fat jar: `gradle :desktop:fatJar` → `desktop/build/libs/desktop-all.jar`
- Android APK: `gradle :android:assembleDebug` → `android/build/outputs/apk/debug/*.apk`
- iOS: configure RoboVM on macOS host (placeholder `:ios` module)

Next: add wrappers for J2ME APIs and start moving logic from `src/`.
