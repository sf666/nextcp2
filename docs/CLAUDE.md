# Documentation site

The docs are an Astro + Starlight site in `docs/` (Yarn, `site: https://sf666.github.io`, base path `/nextcp2/`), published to GitHub Pages at `https://sf666.github.io/nextcp2/`.

**Deployment is automatic.** The `.github/workflows/docs.yml` workflow builds `docs/` and publishes it via GitHub Pages (`actions/deploy-pages`) on every push to `main` that touches `docs/**` (or manually via *workflow_dispatch*). No manual build/copy step is needed. The Pages source is set to "GitHub Actions", and the site is served as the project page of this repo — the old `sf666.github.io/nextcp2/` folder in the separate user-pages repo is no longer used.

To preview locally before pushing:

```bash
cd docs && yarn install && yarn build   # builds docs/dist/ (Astro + Pagefind index)
yarn preview                            # serve the built site locally
```

## Animated clips

The animations on the *Music Library* page are h264 clips in `docs/public/anim/`, embedded as
`<video autoplay loop muted playsinline controls>` (see `music_library.mdx`). They are produced
with the tools in `docs/tools/` — recording either via the macOS screencast or the scripted
Playwright recorder, converting with `tools/anim2video.sh` (or `tools/anim2webp.sh` for an
animated-image variant). `docs/tools/README.md` has the details; note that the playlist clips
write to the media server they are recorded against. Files under `public/` are copied into the
build unchanged, which is why the clips live there instead of `src/.../img/`.
