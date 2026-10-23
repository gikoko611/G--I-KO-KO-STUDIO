# G I KO KO STUDIO

Android-native mobile development workspace with a modern cyber-green interface.

- App label: **G I KO KO STUDIO**
- Version: **0.0.1**
- Application ID: `com.gikoko.studio`
- Minimum Android: 6.0 (API 23)
- Target device: Redmi 9A / Android 10 / ARMv7
- Local workspace: ZIP import and safe extraction into app-private storage
- Web projects: offline preview for projects containing `index.html`, and export a packaged source ZIP
- Android projects: source ZIP import/export and Termux local-build guide
- APK signing: local signing with a user-provided JKS/PKCS12 keystore
- Plugin manager: JSON manifest import, listing, enable/disable and remove

## Local ZIP workflow
1. Open **LOCAL BUILD CENTER • ZIP IMPORT**.
2. Select a project ZIP using Android's file picker.
3. The app extracts it locally, rejects path-traversal ZIP entries, limits extraction to 200 MB, and attempts to detect web vs Android source projects.
4. For a static web app, use **PREVIEW WEB APP OFFLINE** or **PACKAGE WEB PROJECT ZIP**.
5. For an Android source project, export the ZIP and use Termux with a compatible JDK, Android SDK and Gradle installation.

## Online and offline modes

The app exposes two build modes:

- **Online mode:** opens the GitHub Actions cloud workflow and repository build logs. The current workflow builds this Studio repository and uploads a debug APK artifact. It does not yet upload arbitrary imported project ZIPs to a cloud build job.
- **Offline mode:** imports project ZIPs into app-private storage. Static HTML web projects can be previewed offline and packaged into a source ZIP. Android source projects can be edited/exported locally and handed off to Termux for a native build.

## Important native-build limitation

This app does **not** bundle the Android SDK, Gradle, JDK, Node.js or all Maven/NPM dependencies. Therefore it cannot independently promise a one-tap, fully offline APK build for arbitrary native projects. Native Android builds need a compatible toolchain and all required dependencies already installed/cached on the device. On a 32-bit Redmi 9A this is constrained by ARMv7 support, memory and storage. The Termux guide uses `gradle --offline assembleDebug`; this only works after the required Gradle plugins, Android platform/build-tools and dependencies have been cached.

The offline web workflow is a preview and source-ZIP packaging workflow, not a compiler for every React/Vite/Angular framework. Framework web projects need generated static output (such as `dist/index.html`) included in the ZIP for preview/package.

The GitHub Actions workflow runs on pushes to `main` and can also be started manually from the Actions page.

## Security
- Imported ZIPs are extracted into private app storage; unsafe paths and overlarge extraction are rejected.
- APK signing uses a local selected keystore. Never share its private key or passwords.
- Plugin manifests are treated as data only; arbitrary plugin code is not executed.
