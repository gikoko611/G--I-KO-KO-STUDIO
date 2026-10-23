# G I KO KO STUDIO

Android-native mobile development workspace with a modern cyber-green interface.

- App label: **G I KO KO STUDIO**
- Version: **0.0.1**
- Application ID: `com.gikoko.studio`
- Minimum Android: 6.0 (API 23)
- Build: GitHub Actions / Android SDK / Gradle
- APK signing: local signing via Android's document picker and a user-provided JKS/PKCS12 keystore
- Plugin manager: imports JSON manifests and supports enable/disable/remove
- Sample manifest: `plugins/example-plugin.json`

## Build APK
Push to `main` or manually run **Build G I KO KO STUDIO APK** in GitHub Actions. Download the artifact named `G-I-KO-KO-STUDIO-0.0.1`.

## Current implemented modules
- Cyber-themed workspace navigation
- Local APK signer with V1/V2/V3 signatures (user-provided signing key)
- Declarative JSON plugin manifest import, listing, enable/disable and remove
- GitHub Actions build-status links
- Project ZIP picker foundation

## Important limitations
This is an evolving mobile IDE foundation, not a complete replacement for desktop Android Studio. Arbitrary source-project ZIP uploads and cloud builds for external projects are not connected yet. The current GitHub workflow builds this repository. The code editor, terminal, Gradle project importer, real device log capture and plugin action execution still need separate implementation and testing.

Plugin manifests are treated as data only. The app intentionally does not load or execute arbitrary plugin code. APK signing changes the APK's signing identity; it does not rebuild source code, and updating an installed app generally requires the original signing key.

## Security
- APK signing and keystore passwords are handled locally in the app; never share your private keystore or passwords.
- Use only APKs you are authorized to modify/sign.
- Keep a secure backup of your signing key. If you lose it, you may be unable to publish compatible updates for apps signed with that key.
