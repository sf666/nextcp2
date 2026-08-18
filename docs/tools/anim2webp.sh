#!/usr/bin/env bash
#
# Turns a screen recording into an animated WebP for the documentation.
#
# The recording comes straight from the macOS screencast (Cmd+Shift+5 writes a .mov
# with H.264 or HEVC, Retina 2x, cursor included) or from any other video ffmpeg can
# read. Conversion is two staged on purpose:
#
#   1. ffmpeg decodes to lossless PNG frames (trim, speed, resample, downscale)
#   2. img2webp packs those frames into one animated WebP
#
# There is deliberately no GIF and no extra video transcode in between: a GIF would
# quantize to 256 colours before the WebP ever sees the pixels, and re-encoding the
# recording to another video codec first would only add a second lossy generation.
# The local ffmpeg builds have no libwebp, so the one shot "-c:v libwebp" is not
# available anyway.
#
# usage: anim2webp.sh <input> <output.webp> [options]
#   --fps <n>        frames per second of the result (default 12)
#   --width <px>     output width, height follows the aspect ratio (default 1100)
#   --speed <f>      speed up factor, e.g. 1.6 for a long clip (default 1.0)
#   --ss <sec>       skip this many seconds at the start
#   --t <sec>        keep only this many seconds
#   --q <n>          WebP quality 0..100 (default 75)
#   --crop <W:H:X:Y> crop before scaling, e.g. to drop the window shadow
#   --keep-frames    do not delete the temporary PNG directory
#
# If the file gets too big, lower --fps first, then --width, then --q.

set -euo pipefail

if [[ $# -lt 2 ]]; then
  sed -n '/^# usage:/,/^# If the file/p' "$0" | sed 's/^# \{0,1\}//'
  exit 1
fi

IN="$1"; OUT="$2"; shift 2

FPS=12
WIDTH=1100
SPEED=1.0
SS=""
T=""
Q=75
CROP=""
KEEP=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --fps)   FPS="$2"; shift 2 ;;
    --width) WIDTH="$2"; shift 2 ;;
    --speed) SPEED="$2"; shift 2 ;;
    --ss)    SS="$2"; shift 2 ;;
    --t)     T="$2"; shift 2 ;;
    --q)     Q="$2"; shift 2 ;;
    --crop)  CROP="$2"; shift 2 ;;
    --keep-frames) KEEP=1; shift ;;
    *) echo "unknown option: $1" >&2; exit 1 ;;
  esac
done

for tool in ffmpeg img2webp webpmux; do
  command -v "$tool" >/dev/null || { echo "missing tool: $tool (brew install ffmpeg webp)" >&2; exit 1; }
done

[[ -f "$IN" ]] || { echo "no such input file: $IN" >&2; exit 1; }
mkdir -p "$(dirname "$OUT")"

TMP="$(mktemp -d "${TMPDIR:-/tmp}/anim2webp.XXXXXX")"
cleanup() { [[ $KEEP -eq 1 ]] || rm -rf "$TMP"; }
trap cleanup EXIT

# Frame duration in ms. It has to match --fps: img2webp defaults to 100 ms, which
# would play a 12 fps clip at 10 fps.
DELAY=$(( 1000 / FPS ))

FILTER="setpts=PTS/${SPEED},fps=${FPS},scale=${WIDTH}:-1:flags=lanczos"
[[ -n "$CROP" ]] && FILTER="crop=${CROP},${FILTER}"

echo "==> extracting frames (${FPS} fps, ${WIDTH}px, speed ${SPEED}x)"
ffmpeg -hide_banner -loglevel error \
  ${SS:+-ss "$SS"} ${T:+-t "$T"} -i "$IN" \
  -vf "$FILTER" -vsync 0 "$TMP/f_%05d.png"

COUNT=$(ls "$TMP"/f_*.png | wc -l | tr -d ' ')
echo "==> encoding ${COUNT} frames to animated WebP (q=${Q}, delay=${DELAY}ms)"
# File level flags first, then per frame flags, then the frames themselves.
#   -mixed      picks lossy or lossless per frame, which suits UI clips
#   -min_size   optimizes for file size instead of seek friendliness
#   -sharp_yuv  keeps text edges clean
img2webp -loop 0 -min_size -mixed -sharp_yuv \
  -d "$DELAY" -q "$Q" -m 6 "$TMP"/f_*.png -o "$OUT" >/dev/null

echo "==> $OUT"
webpmux -info "$OUT" | head -3
ls -lh "$OUT" | awk '{print "    size: " $5}'
