# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

nextCP/2 is a web-based UPnP audio control point. A single instance (typically on a NAS, server, or Raspberry Pi) is controlled by any browser on the LAN — there is no per-device install. The backend is a Spring Boot app speaking UPnP via [JUPnP](https://github.com/jupnp/jupnp); the frontend is an Angular SPA served as static resources from the same JAR.

The Maven backend lives in `backend/` (groupId `de.sf666`), the Angular app in
`frontend/nextcp-ui/`. Two directories are build artifacts and must never be
committed or hand-edited: `backend/nextcp2-runtime/src/main/resources/static/`
(the Angular build output) and `backend/nextcp2-modelgen/src/main/java/nextcp/dto/`
(generated DTOs — see below).

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

## Code generation — do not hand-edit generated files

Two generators live in `backend/nextcp2-codegen/`:

- **DTOs** — `codegen.DtoModelGen` reads `backend/nextcp2-codegen/src/main/resources/yaml/dto.yaml` and writes Java DTOs into `nextcp2-modelgen/src/main/java/nextcp/dto/`. The Maven `process-classes` phase of `nextcp2-modelgen` then derives `frontend/nextcp-ui/src/app/service/dto.d.ts` from those Java classes. To regenerate TS only: `cd backend/nextcp2-modelgen && mvn process-classes` (or run `gen_typescript.sh`).
- **UPnP services** — `codegen.UpnpModelGen` can generate Java service/event classes for any discovered UPnP service (controlled by config flags).

Never edit anything under `nextcp2-modelgen/src/main/java/nextcp/dto/` or `frontend/nextcp-ui/src/app/service/dto.d.ts` directly — the next generator run overwrites it. Change `dto.yaml` and regenerate.

### Changing `dto.yaml` takes two generator runs, in this order

`mvn process-classes` alone is **not** enough: it reads the *existing* Java classes and only writes
`dto.d.ts`. Skip step 1 and nothing happens — silently, without an error.

```bash
cd backend
# 1. dto.yaml -> Java DTOs
mvn -o -pl nextcp2-codegen -am install -DskipTests
mvn -q -o -pl nextcp2-codegen dependency:build-classpath -Dmdep.outputFile=/tmp/cg-cp.txt
java -cp "nextcp2-codegen/target/classes:$(cat /tmp/cg-cp.txt)" codegen.DtoModelGen \
     "$PWD/nextcp2-modelgen/src/main/java/nextcp/dto"

# 2. Java DTOs -> dto.d.ts
mvn -o -pl nextcp2-modelgen process-classes
```

Step 1 is spelled out rather than run through `exec:java`, because `exec-maven-plugin` is not in the
local repository and the call fails offline with a `PluginVersionResolutionException`.

A new **mandatory** field then breaks every frontend object literal that initialises the type in
full — for `ApplicationConfig` that is `applicationConfig` in
`frontend/nextcp-ui/src/app/service/configuration.service.ts` (TS2741). Either add the field there
with a default, or mark it optional in `dto.yaml` with a trailing `?`.

## Notes

- `GEMINI.md` and `.ai/context.md` exist for other AI assistants and are gitignored / partially in-repo respectively — they overlap with this file; treat this file as authoritative for Claude Code.
- All comments written into source files (code, YAML, properties, build scripts, etc.) MUST be in English, regardless of the language used in chat.

## Where the rest lives

Loaded on demand, so it is not in context for every task:

- Angular conventions and frontend build output → `frontend/nextcp-ui/CLAUDE.md`
- Runtime config resolution (`nextcp2config.json`, `NEXTCP_*` env) → `backend/nextcp2-runtime/CLAUDE.md`
- Docs site (Astro + Starlight, auto-deploy) → `docs/CLAUDE.md`
- Translation workflow / Tolgee ownership → the `translations` skill
