# Documentation clips and screenshots

Tools for producing the animated clips in `public/anim/` and (re)producing screenshots.
Everything here is a developer tool — nothing of it runs during the site build.

## Recording

Two ways to get a recording:

**macOS screencast (simplest).** Cmd+Shift+5 → *Options* → enable **Show Mouse Clicks**, pick
*Record Selected Portion* around the browser window, record, stop. Result is a `.mov`
(H.264, or HEVC with "High Efficiency"), Retina 2×, cursor included. Leave 1–2 s of calm
before and after the action; trim later with `--ss` / `--t`.

**Scripted (reproducible).** `record.js` drives the app with Playwright and writes a webm.
Because Playwright videos contain no mouse pointer, it injects a synthetic cursor with a
click ripple. It needs Playwright and a reachable nextCP/2 instance (edit `BASE` in
`lib.js`):

```bash
npm i playwright                 # once, outside the repo is fine
node record.js search            # available clips: search, playlist_sidebar, playlist_create_add
```

> The `playlist_*` clips write to the media server they are pointed at: `playlist_sidebar`
> saves the MY PLAYLISTS ObjectID, `playlist_create_add` creates a playlist named
> "docs demo", adds and removes tracks and deletes it again. Point them at a test system,
> or accept those writes knowingly.

## Converting

**`anim2video.sh` — what the docs use.** One ffmpeg pass to h264. Smallest result, keeps the
full frame rate and stays sharp on text:

```bash
./anim2video.sh ~/Desktop/recording.mov ../public/anim/search.mp4 \
  --width 1100 --fps 25 --crf 26 --ss 5.7 --poster
```

Reference it in MDX (autoplays and loops, no click needed; `muted` is what makes autoplay
legal, `playsinline` keeps iOS from going fullscreen, `controls` lets readers pause a long
clip):

```html
<video src="/nextcp2/anim/search.mp4" poster="/nextcp2/anim/search.poster.webp"
       autoplay loop muted playsinline controls preload="metadata"
       class="block w-full h-auto my-4 rounded-lg" aria-label="…"></video>
```

**`anim2webp.sh` — alternative as an animated image.** Use it where a plain `<img>` is
wanted instead of a video element. Two stages, because no local ffmpeg build has libwebp:
frames via ffmpeg, packing via `img2webp`.

```bash
./anim2webp.sh ~/Desktop/recording.mov ../public/anim/search.webp --fps 12 --width 1100
```

Measured on the three clips of the Music Library page (1100 px): h264 was 3–4× smaller than
animated WebP *and* ran at 25 fps instead of 12 — hence the video route for the docs.

Do **not** go through GIF (`gif2webp`, `gifsicle`): the 256-colour quantization is baked in
by then. And do not re-encode a recording to another video codec before converting — that is
a second lossy generation for nothing.

## Where the files go

Clips live in `docs/public/anim/`. Everything under `public/` is copied into the build
verbatim, so the encode settings above are exactly what ships — Astro would otherwise
re-encode the asset (sharp reads animations with `pages: -1`) and add another generation.

Requirements: `ffmpeg` and, for the WebP route, `webp` (`img2webp`, `cwebp`, `webpmux`) —
`brew install ffmpeg webp`.
