#!/usr/bin/env bash
# Downloads the original TikTok APK for the pinned version from APKMirror, for
# CI verification only. The file must never be committed, cached or uploaded.
#
# Usage: scripts/fetch-tiktok-apk.sh <version> <output.apk>
set -euo pipefail

version="${1:?version, e.g. 47.4.3}"
out="${2:?output path}"
slug="tiktok-${version//./-}"
base="https://www.apkmirror.com"
variant="${base}/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/${slug}-release/${slug}-android-apk-download/"
ua="Mozilla/5.0 (X11; Linux x86_64; rv:140.0) Gecko/20100101 Firefox/140.0"

fetch() { curl -fsSL --retry 3 --compressed -A "$ua" -H "Accept-Language: en-US,en;q=0.8" "$@"; }

echo "Variant page: $variant"
download_page=$(fetch "$variant" | grep -oE 'href="[^"]*/download/\?key=[^"]+"' | head -n1 | sed -E 's/^href="//; s/"$//; s/&amp;/\&/g')
[[ -n "$download_page" ]] || { echo "error: download button not found (page layout changed or request blocked)" >&2; exit 1; }

final=$(fetch "${base}${download_page}" | grep -oE 'href="[^"]*download\.php\?[^"]+"' | head -n1 | sed -E 's/^href="//; s/"$//; s/&amp;/\&/g')
[[ -n "$final" ]] || { echo "error: direct download link not found" >&2; exit 1; }

fetch -o "$out" "${base}${final}"
echo "Downloaded $(du -h "$out" | cut -f1) to $out"
