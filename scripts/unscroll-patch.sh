#!/usr/bin/env bash
# Applies the Unscroll patch bundle to an original TikTok APK with a pinned
# Morphe Desktop CLI. Produces nothing if any Unscroll patch fails (fail-closed).
#
# Usage:
#   scripts/unscroll-patch.sh --apk TikTok-47.4.3.apkm --bundle patches-x.y.z.mpp \
#       --keystore ~/unscroll.keystore [--out unscroll.apk]
#
# The keystore is created by Morphe on first use if it does not exist. Keep it:
# every later Unscroll update must be signed with the same keystore.
set -euo pipefail

MORPHE_VERSION="1.18.1"
MORPHE_JAR="morphe-desktop-${MORPHE_VERSION}-all.jar"
MORPHE_URL="https://github.com/MorpheApp/morphe-desktop/releases/download/v${MORPHE_VERSION}/${MORPHE_JAR}"
MORPHE_SHA256="1b506ab5f03d16a2f65026d5e0e1910d01fc1e2152f21eaeb44ed2f30856597b"

# Every patch that makes up Unscroll. All of them must apply.
PATCHES=(
  "Block feed endpoints"
  "Restrict navigation"
  "Fix Google login"
)

die() { echo "error: $*" >&2; exit 1; }

apk="" bundle="" keystore="" out="" keystore_password="${UNSCROLL_KEYSTORE_PASSWORD:-}"
while [[ $# -gt 0 ]]; do
  case "$1" in
    --apk) apk="$2"; shift 2 ;;
    --bundle) bundle="$2"; shift 2 ;;
    --keystore) keystore="$2"; shift 2 ;;
    --out) out="$2"; shift 2 ;;
    --list) printf '%s\n' "${PATCHES[@]}"; exit 0 ;;
    -h|--help) sed -n '2,12p' "$0"; exit 0 ;;
    *) die "unknown argument: $1" ;;
  esac
done

[[ -f "$apk" ]] || die "--apk must point to the original TikTok APK or APKMirror bundle (.apkm)"
case "$apk" in *.apk|*.apkm|*.apks|*.xapk) ;; *) die "--apk must end in .apk, .apkm, .apks or .xapk (Morphe picks the format by extension)" ;; esac
[[ -f "$bundle" ]] || die "--bundle must point to the Unscroll patches-*.mpp"
[[ -n "$keystore" ]] || die "--keystore is required (reuse the same file for every update)"
command -v java >/dev/null || die "Java 21 or newer is required"
java_major=$(java -XshowSettings:properties -version 2>&1 | awk -F'= ' '/java.specification.version/ {print $2}')
[[ "${java_major%%.*}" -ge 21 ]] || die "Java 21 or newer is required (found ${java_major})"

cache_dir="${XDG_CACHE_HOME:-$HOME/.cache}/unscroll"
mkdir -p "$cache_dir"
jar="$cache_dir/$MORPHE_JAR"
if [[ ! -f "$jar" ]]; then
  echo "Downloading Morphe Desktop ${MORPHE_VERSION}..."
  curl -fL --retry 3 -o "$jar.tmp" "$MORPHE_URL"
  mv "$jar.tmp" "$jar"
fi
echo "${MORPHE_SHA256}  ${jar}" | sha256sum -c --quiet - || { rm -f "$jar"; die "Morphe Desktop checksum mismatch"; }

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
base_name=$(basename "$apk"); out="${out:-unscroll-${base_name%.*}.apk}"
result="$work/result.json"

args=(-jar "$jar" patch -p "$bundle" --exclusive)
for name in "${PATCHES[@]}"; do args+=(-e "$name"); done
args+=(--keystore "$keystore" --keystore-password "$keystore_password" --signer Unscroll
       -r "$result" -o "$work/out.apk" "$apk")

echo "Patching $(basename "$apk")..."
java "${args[@]}"

[[ -f "$work/out.apk" ]] || die "patching produced no APK"
# The CLI stops on the first failing patch; check the result report as a second line of defence.
if [[ -f "$result" ]] && grep -Eqi '"(success|succeeded)"[[:space:]]*:[[:space:]]*false|"exception"[[:space:]]*:[[:space:]]*"[^"]' "$result"; then
  die "at least one patch failed; see $result (not producing an APK)"
fi

mv "$work/out.apk" "$out"
echo "Done: $out"
echo "Install only after reading docs/INSTALL.md (the official TikTok must be uninstalled first)."
