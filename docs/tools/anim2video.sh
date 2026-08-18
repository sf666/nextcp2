#!/usr/bin/env bash
#
# Turns a screen recording into a looping video clip for the documentation.
#
# This is the alternative to tools/anim2webp.sh: same input (macOS screencast .mov or
# any video ffmpeg reads), but the result is delivered as <video> instead of <img>.
# For long clips that is markedly smaller and sharper than an animated WebP, because
# h264 can use motion compensation instead of storing whole frames.
#
# Unlike the WebP path this is a single ffmpeg pass - no PNG frames, no img2webp.
#
# usage: anim2video.sh <input> <output.mp4> [options]
#   --width <px>     output width, height follows the aspect ratio (default 1100)
#   --fps <n>        frames per second of the result (default 12, i.e. keep it fluid)
#   --speed <f>      speed up factor, e.g. 1.6 for a long clip (default 1.0)
#   --ss <sec>       skip this many seconds at the start
#   --t <sec>        keep only this many seconds
#   --crf <n>        h264 quality, lower is better, 18..30 useful (default 26)
#   --crop <W:H:X:Y> crop before scaling, e.g. to drop the window shadow
#   --webm           additionally write a VP9 <output>.webm source
#   --poster         additionally write <output>.poster.webp (first frame)
#
# The clip is silent (-an): documentation clips autoplay, and autoplay is only allowed
# for muted video anyway.
#
# Markup for the result (autoplays and loops, no click needed):
#   <video src="/nextcp2/anim/foo.mp4" poster="/nextcp2/anim/foo.poster.webp"
#          autoplay loop muted playsinline preload="metadata"
#          style="width:100%;height:auto;border-radius:8px"></video>

set -euo pipefail

if [[ $# -lt 2 ]]; then
  sed -n '/^# usage:/,/^# The clip is silent/p' "$0" | sed 's/^# \{0,1\}//'
  exit 1
fi

IN="$1"; OUT="$2"; shift 2

WIDTH=1100
FPS=12
SPEED=1.0
SS=""
T=""
CRF=26
CROP=""
WEBM=0
POSTER=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --width) WIDTH="$2"; shift 2 ;;
    --fps)   FPS="$2"; shift 2 ;;
    --speed) SPEED="$2"; shift 2 ;;
    --ss)    SS="$2"; shift 2 ;;
    --t)     T="$2"; shift 2 ;;
    --crf)   CRF="$2"; shift 2 ;;
    --crop)  CROP="$2"; shift 2 ;;
    --webm)  WEBM=1; shift ;;
    --poster) POSTER=1; shift ;;
    *) echo "unknown option: $1" >&2; exit 1 ;;
  esac
done

command -v ffmpeg >/dev/null || { echo "missing tool: ffmpeg" >&2; exit 1; }
[[ -f "$IN" ]] || { echo "no such input file: $IN" >&2; exit 1; }
mkdir -p "$(dirname "$OUT")"

# scale to an even height, yuv420p needs it
FILTER="setpts=PTS/${SPEED},fps=${FPS},scale=${WIDTH}:-2:flags=lanczos"
[[ -n "$CROP" ]] && FILTER="crop=${CROP},${FILTER}"
TRIM=(${SS:+-ss "$SS"} ${T:+-t "$T"})

echo "==> h264 (${WIDTH}px, ${FPS} fps, crf ${CRF}, speed ${SPEED}x)"
ffmpeg -hide_banner -loglevel error -y "${TRIM[@]}" -i "$IN" \
  -vf "$FILTER" -an \
  -c:v libx264 -preset slow -crf "$CRF" -profile:v high -pix_fmt yuv420p \
  -movflags +faststart "$OUT"
ls -lh "$OUT" | awk '{print "    " $9 "  " $5}'

if [[ $WEBM -eq 1 ]]; then
  echo "==> vp9 fallback source"
  ffmpeg -hide_banner -loglevel error -y "${TRIM[@]}" -i "$IN" \
    -vf "$FILTER" -an \
    -c:v libvpx-vp9 -crf $((CRF + 8)) -b:v 0 -row-mt 1 "${OUT%.mp4}.webm"
  ls -lh "${OUT%.mp4}.webm" | awk '{print "    " $9 "  " $5}'
fi

if [[ $POSTER -eq 1 ]]; then
  echo "==> poster frame"
  # ffmpeg here has no webp encoder, so go through PNG and let cwebp do the WebP
  ffmpeg -hide_banner -loglevel error -y "${TRIM[@]}" -i "$IN" \
    -vf "$FILTER" -frames:v 1 "${OUT%.mp4}.poster.png"
  if command -v cwebp >/dev/null; then
    cwebp -quiet -q 80 -m 6 "${OUT%.mp4}.poster.png" -o "${OUT%.mp4}.poster.webp"
    rm -f "${OUT%.mp4}.poster.png"
  fi
  ls -lh "${OUT%.mp4}".poster.* | awk '{print "    " $9 "  " $5}'
fi
