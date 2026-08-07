# Frontend (Angular)

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

## Generated files

`src/app/service/dto.d.ts` is GENERATED from the backend Java DTOs — never edit it
directly. Change `backend/nextcp2-codegen/src/main/resources/yaml/dto.yaml` and
regenerate (`cd backend/nextcp2-modelgen && mvn process-classes`, or `gen_typescript.sh`).

## Build output

`ng build` writes into `backend/nextcp2-runtime/src/main/resources/static/` — that
directory is a build artifact and must not be committed.
