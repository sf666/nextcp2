# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

nextCP/2 is a web-based UPnP audio control point. A single instance (typically on a NAS, server, or Raspberry Pi) is controlled by any browser on the LAN — there is no per-device install. The backend is a Spring Boot app speaking UPnP via [JUPnP](https://github.com/jupnp/jupnp); the frontend is an Angular SPA served as static resources from the same JAR.

## Repository layout

```
backend/                                  Maven multi-module (groupId de.sf666)
├── nextcp2-assembly/                     Builds the runnable nextcp2.jar (Spring Boot)
├── nextcp2-runtime/                      Spring Boot app: REST, SSE, UPnP, services
├── nextcp2-modelgen/                     GENERATED Java DTOs live here (nextcp.dto.*)
├── nextcp2-codegen/                      DTO + UPnP code generators (DtoModelGen, UpnpModelGen)
├── nextcp2-db/                           H2 + MyBatis persistence
├── nextcp2-player/                       Player module
├── nextcp2-external-information-provider/  MusicBrainz / Last.fm / Spotify
└── nextcp2-device-driver/
    └── nextcp2-ma9000/                   McIntosh MA9000/MA12000 driver (RS232/TCP)

frontend/nextcp-ui/                       Angular app — output is copied into
                                          backend/nextcp2-runtime/src/main/resources/static/
                                          (do not commit the static/ directory)
```

## Build

The frontend **must be built before** the backend, because the Angular build writes into the backend's `static/` resource directory which gets packaged into the JAR.

```bash
./build_dependencies.sh                   # one-time: clones+installs sf666/musicbrainz into ~/.m2
./build.sh                                # full clean rebuild; artifacts land in ./build/
```

`build.sh` runs roughly: `yarn install && ng build` in `frontend/nextcp-ui/`, then `mvn clean install package` in `backend/`. The final `nextcp2.jar` ends up in `backend/nextcp2-assembly/target/` and is copied to `./build/`.

Single-module / partial builds:

```bash
cd frontend/nextcp-ui && yarn install && ./ng build      # frontend only
cd backend && mvn -pl nextcp2-runtime -am install        # one Maven module + its deps
cd backend && mvn -pl nextcp2-runtime test               # tests for one module
cd backend && mvn -Dtest=ClassName#method test           # one JUnit test method
cd frontend/nextcp-ui && yarn test                       # Karma/Jasmine frontend tests
```

Compiler target: backend Maven uses `<maven.compiler.release>25</maven.compiler.release>` even though the README still says JDK 17 minimum — install a JDK that can target 25, or lower the property locally if you must. Frontend requires Node 26.

## Dev loop

```bash
# 1. Start the backend in your IDE — main class:
#    backend/nextcp2-assembly/src/main/java/nextcp/NextcpApplicationStartup
#    Listens on http://localhost:8085

# 2. Start the frontend dev server with the proxy that forwards REST/SSE to :8085
cd frontend/nextcp-ui
yarn start -c dev         # uses proxy.config.json (alias: ng serve --proxy-config proxy.config.json)
```

`proxy.config.json` forwards every REST endpoint and `/SSE` to `localhost:8085`. If you add a new top-level REST path on the backend, also add it to `proxy.config.json` or it won't reach the backend in dev. The browser must support Server-Sent Events.

## Configuration

`FileConfigPersistence` locates `nextcp2config.json` at startup. If `NEXTCP_DATA` (env) / `-Dnextcp.dataDir` is set it is **authoritative**: the config is read from — or generated into — that directory, ignoring the legacy locations. This is what the Docker image uses (`NEXTCP_DATA=/nextcp2_data`, mounted as a volume). Otherwise the search order is: `-DconfigFile=…`, the platform per-user data dir (see below), `/etc/nextcp2/`, `$HOME/`, then the working directory.

If none is found, a default config is generated in the resolved **data directory** and found again on the next start (so settings survive restarts / app updates). The data directory is the `NEXTCP_DATA` override, or platform-specific:

- macOS: `~/Library/Application Support/nextcp2`
- Windows: `%APPDATA%\nextcp2` (fallback `~\nextcp2`)
- Linux/other: `$XDG_CONFIG_HOME/nextcp2` or `~/.config/nextcp2`

Inside the data directory the app creates `logs/` and `upnp_code/` sub-directories; the generated `logback.xml` points `LOG_DIR` at `logs/`, and the H2 database lives in the data-dir root. Existing hand-edited configs and an explicit `-DconfigFile` are always respected.

## Code generation — do not hand-edit generated files

Two generators live in `backend/nextcp2-codegen/`:

- **DTOs** — `codegen.DtoModelGen` reads `backend/nextcp2-codegen/src/main/resources/yaml/dto.yaml` and writes Java DTOs into `nextcp2-modelgen/src/main/java/nextcp/dto/`. The Maven `process-classes` phase of `nextcp2-modelgen` then derives `frontend/nextcp-ui/src/app/service/dto.d.ts` from those Java classes. To regenerate TS only: `cd backend/nextcp2-modelgen && mvn process-classes` (or run `gen_typescript.sh`).
- **UPnP services** — `codegen.UpnpModelGen` can generate Java service/event classes for any discovered UPnP service (controlled by config flags).

Never edit anything under `nextcp2-modelgen/src/main/java/nextcp/dto/` or `frontend/nextcp-ui/src/app/service/dto.d.ts` directly — the next generator run overwrites it. Change `dto.yaml` and regenerate.

## Angular conventions (from `.ai/best-practices_angular.md`)

- Standalone components only (do **not** set `standalone: true` — it's the v20+ default).
- Use `signal()` / `computed()` for state; never call `mutate` on signals.
- Set `changeDetection: ChangeDetectionStrategy.OnPush` on every component.
- Use `input()` / `output()` functions, not `@Input`/`@Output` decorators.
- Use `inject()`, not constructor DI.
- Use native control flow (`@if`, `@for`, `@switch`) — not `*ngIf`/`*ngFor`.
- Use `class`/`style` bindings — not `ngClass`/`ngStyle`.
- No arrow functions in templates.
- Services: `providedIn: 'root'`, single responsibility.
- Use component 'flowbite' and 'tailwindcss' where possible to reduce complex scss.

## Notes

- `GEMINI.md` and `.ai/context.md` exist for other AI assistants and are gitignored / partially in-repo respectively — they overlap with this file; treat this file as authoritative for Claude Code.
- All comments written into source files (code, YAML, properties, build scripts, etc.) MUST be in English, regardless of the language used in chat.

## Translations

Translation files live in `./backend/nextcp2-runtime/src/main/resources/`:

- `messages.properties` — English, **single source of truth**, hand-edited in code.
- `messages_<locale>.properties` (e.g. `messages_de.properties`) — generated/maintained via Tolgee, do not hand-edit.

Config: `tolgee.yaml` (project 31950) and CI in `.github/workflows/translate.yml`. Auto-commits from the workflow are prefixed `chore(i18n)`.

### Ownership

| Concern                  | Owner                | Where to edit              |
| ------------------------ | -------------------- | -------------------------- |
| Keys (add / remove)      | Developer            | `messages.properties`      |
| English values           | Developer            | `messages.properties`      |
| German / other languages | Translators          | Tolgee web UI              |

Do **not** edit English values in the Tolgee UI — they are overwritten on the next CI run. Do **not** hand-edit `messages_de.properties` etc. — the CI overwrites them with what Tolgee returns.


### Cheat sheet for developers

1. Add or remove a key? Edit `messages.properties` (English only).
2. Push to `main`. The CI does the rest: syncs Tolgee, pulls every language, commits the localized files back.
3. Need a German translation? Open Tolgee, translate, save. Next CI run on `main` will commit `messages_de.properties` into the repo.

## Documentation site

The docs are an Astro + Starlight site in `docs/` (Yarn, `site: https://sf666.github.io`, base path `/nextcp2/`), published to GitHub Pages at `https://sf666.github.io/nextcp2/`.

**Deployment is automatic.** The `.github/workflows/docs.yml` workflow builds `docs/` and publishes it via GitHub Pages (`actions/deploy-pages`) on every push to `main` that touches `docs/**` (or manually via *workflow_dispatch*). No manual build/copy step is needed. The Pages source is set to "GitHub Actions", and the site is served as the project page of this repo — the old `sf666.github.io/nextcp2/` folder in the separate user-pages repo is no longer used.

To preview locally before pushing:

```bash
cd docs && yarn install && yarn build   # builds docs/dist/ (Astro + Pagefind index)
yarn preview                            # serve the built site locally
```
