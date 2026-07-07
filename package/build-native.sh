#!/usr/bin/env bash
#
# Builds a self-contained native package of nextCP/2 for the CURRENT operating
# system using the JDK's jpackage tool. The result bundles a Java runtime, so
# the end user does NOT need a JVM installed.
#
# jpackage cannot cross-compile: run this on macOS to get a .dmg / .app, on
# Windows to get a .msi / app-image, on Linux to get a .deb / app-image. See
# .github/workflows/release.yml for the CI matrix that builds every platform.
#
# Prerequisites:
#   * A JDK >= 17 with `jpackage` on PATH (JDK 25 to match the project).
#   * The Spring Boot fat jar already built:
#       backend/nextcp2-assembly/target/nextcp2.jar
#     (run ./build.sh, or: cd backend && mvn -pl nextcp2-assembly -am -DskipTests install)
#
# Usage:
#   package/build-native.sh [app-image|installer]   (default: both)
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

APP_NAME="nextCP2"
VENDOR="sf666"
DESCRIPTION="nextCP/2 - web-based UPnP audio control point"
MAIN_JAR="nextcp2.jar"
JAR_PATH="$ROOT_DIR/backend/nextcp2-assembly/target/$MAIN_JAR"
DIST_DIR="$ROOT_DIR/build/native"

WHAT="${1:-both}"

if ! command -v jpackage >/dev/null 2>&1; then
  echo "ERROR: 'jpackage' not found on PATH. Install a JDK >= 17 (project uses 25)." >&2
  exit 1
fi

if [ ! -f "$JAR_PATH" ]; then
  echo "ERROR: fat jar not found: $JAR_PATH" >&2
  echo "       Build it first (./build.sh, or 'cd backend && mvn -pl nextcp2-assembly -am -DskipTests install')." >&2
  exit 1
fi

# Derive an x.y.z app version from the root pom, stripping any -SNAPSHOT / -bN
# suffix (jpackage rejects non-numeric versions). Override with APP_VERSION=...
if [ -z "${APP_VERSION:-}" ]; then
  RAW="$(sed -n 's:.*<version>\(.*\)</version>.*:\1:p' "$ROOT_DIR/backend/pom.xml" | head -1)"
  APP_VERSION="${RAW%%-*}"
fi
[ -n "$APP_VERSION" ] || APP_VERSION="1.0.0"

# jpackage wants an --input directory that contains ONLY the app jar(s).
STAGE="$(mktemp -d)"
trap 'rm -rf "$STAGE"' EXIT
cp "$JAR_PATH" "$STAGE/$MAIN_JAR"

mkdir -p "$DIST_DIR"

# Per-OS icon (optional; jpackage falls back to a default when absent).
ICON_ARGS=()
case "$(uname -s)" in
  Darwin)
    INSTALLER_TYPE="dmg"
    ICNS="$SCRIPT_DIR/nextcp2.icns"
    [ -f "$ICNS" ] && ICON_ARGS=(--icon "$ICNS")
    ;;
  Linux)
    INSTALLER_TYPE="deb"
    PNG="$SCRIPT_DIR/nextcp2.png"
    [ -f "$PNG" ] && ICON_ARGS=(--icon "$PNG")
    ;;
  *)
    INSTALLER_TYPE="msi"
    ICO="$SCRIPT_DIR/nextcp2.ico"
    [ -f "$ICO" ] && ICON_ARGS=(--icon "$ICO")
    ;;
esac

COMMON_ARGS=(
  --name "$APP_NAME"
  --app-version "$APP_VERSION"
  --vendor "$VENDOR"
  --description "$DESCRIPTION"
  --input "$STAGE"
  --main-jar "$MAIN_JAR"
  --java-options "-Xmx512m"
  # Marks this as the native desktop app. Enables desktop-only conveniences that
  # must NOT apply to the plain jar / Docker image / headless server: opening the
  # browser at the local UI on startup, and exposing the in-app shutdown button.
  --java-options "-Dnextcp.desktopMode=true"
)
# Append the icon only when one was found (guarded so an empty array does not
# trip 'set -u' on the bash 3.2 shipped with macOS).
if [ "${#ICON_ARGS[@]}" -gt 0 ]; then
  COMMON_ARGS+=("${ICON_ARGS[@]}")
fi

echo "==> nextCP/2 $APP_VERSION - jpackage on $(uname -s)"

if [ "$WHAT" = "app-image" ] || [ "$WHAT" = "both" ]; then
  echo "==> Building portable app-image ..."
  rm -rf "$DIST_DIR/app-image"
  mkdir -p "$DIST_DIR/app-image"
  jpackage --type app-image "${COMMON_ARGS[@]}" --dest "$DIST_DIR/app-image"
  echo "    -> $DIST_DIR/app-image"
fi

if [ "$WHAT" = "installer" ] || [ "$WHAT" = "both" ]; then
  echo "==> Building installer (.$INSTALLER_TYPE) ..."
  jpackage --type "$INSTALLER_TYPE" "${COMMON_ARGS[@]}" --dest "$DIST_DIR"
  echo "    -> $DIST_DIR/*.$INSTALLER_TYPE"
fi

echo "==> Done. Artifacts in: $DIST_DIR"
ls -la "$DIST_DIR"
