# G.I KO KO STUDIO — Offline-First Creative Production

**Version:** 0.1.0  
**Package:** `com.gikoko.studio`  
**Target:** Android 6+; designed for Android 10 / Redmi 9A / ARMv7 Java-only compatibility.

## Production Studio

The refreshed Studio UI uses a sharp cyber palette (neon blue, cyan/green and red), animated icon cards, and a mobile-first layout. The original app logo and launcher icons are retained.

### Offline-first tools
- **PDF source import:** imports PDF files into private app storage and extracts selectable text locally with PDFBox-Android. Scanned/image-only PDFs require OCR and are not yet supported.
- **Script workspace:** edit and save narration drafts locally.
- **Subtitle Lab:** edit SubRip (.srt) text and export it to phone storage.
- **Offline Voice Studio:** uses the Android Text-to-Speech engine; available languages depend on installed system voice data. English voice data may need to be downloaded once in Android settings.
- **Local video assets:** import direct HTTPS video URLs or local phone videos and inspect duration/resolution. Imported clips are retained in app-private storage.

### Existing tools retained
- Project Explorer and ZIP import
- Text Code Editor for imported projects
- Local Build Center, offline static web preview and ZIP export
- GitHub Actions APK build entry point
- APK signer using a user-provided keystore
- AI Assistant with custom provider settings
- Phone File Manager with user-granted Storage Access Framework permissions
- Plugin Manager (JSON manifests and restricted safe actions)
- Device diagnostics and About page

## Online is optional
Internet is only needed for optional AI provider calls, plugin catalog downloads, direct URL media imports and GitHub Actions. PDF text extraction, script editing, subtitle authoring and Android TTS run locally. The app does not claim to run large generative video models locally; Redmi 9A's 32-bit ARMv7 CPU/RAM are not suitable for that workload.

## Build
GitHub Actions builds a debug APK using Java 17 and Gradle 8.9. The APK is a Java app and does not bundle ARM64-only native libraries. Use Actions artifacts to download the APK after a successful workflow run.

## Security
- PDFs and imported videos are stored in app-private storage.
- PDF imports are limited to 100 MB; local video imports are limited to 300 MB.
- ZIP import keeps its path-traversal and extraction-size protections.
- Plugin manifests are data only; arbitrary plugin code is not executed.
- Never share APK signing keystores or passwords.
