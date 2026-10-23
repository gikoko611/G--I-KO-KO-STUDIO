#!/data/data/com.termux/files/usr/bin/bash
# G.I KO KO STUDIO native build helper: JDK 17, Gradle 8.9, AGP 8.7.3, SDK 35, Build Tools 35.0.0
set -Eeuo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
fail() { printf '\n[GI BUILD ERROR] %s\n' "$*" >&2; exit 1; }
command -v java >/dev/null 2>&1 || fail 'Java missing: pkg install openjdk-17'
command -v gradle >/dev/null 2>&1 || fail 'Gradle missing: pkg install gradle'
JAVA_VERSION="$(java -version 2>&1 | sed -n '1s/.*version "\([^"]*\)".*/\1/p')"
case "$JAVA_VERSION" in 17*) ;; *) fail "JDK 17 required; current Java=$JAVA_VERSION";; esac
GRADLE_VERSION="$(gradle --version 2>/dev/null | sed -n '/^Gradle /{s/^Gradle //;p;q;}')"
case "$GRADLE_VERSION" in 8.9) ;; *) fail "Gradle 8.9 required; current Gradle=$GRADLE_VERSION";; esac
SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
[ -n "$SDK" ] || fail 'Set ANDROID_HOME or ANDROID_SDK_ROOT to an installed Android SDK.'
[ -d "$SDK/platforms/android-35" ] || fail "Missing SDK platform: $SDK/platforms/android-35"
[ -d "$SDK/build-tools/35.0.0" ] || fail "Missing Build Tools: $SDK/build-tools/35.0.0"
printf '\n=== G.I KO KO STUDIO NATIVE APK BUILD ===\nJava: %s | Gradle: %s | SDK: %s\n' "$JAVA_VERSION" "$GRADLE_VERSION" "$SDK"
printf 'First build needs internet for Gradle/AGP/Maven dependencies; --offline works only after dependencies are cached.\n\n'
gradle --no-daemon --stacktrace assembleDebug "$@"
APK="$ROOT/app/build/outputs/apk/debug/app-debug.apk"
[ -s "$APK" ] || fail "APK output not found: $APK"
ls -lh "$APK"
printf '\n[GI BUILD SUCCESS] APK: %s\n' "$APK"
