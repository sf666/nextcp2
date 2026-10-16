---
name: translations
description: Add, change or remove UI translation keys in nextCP/2. Covers messages.properties, the Tolgee workflow and who owns which language file. Use when touching any messages*.properties file or adding user-visible text.
---

# Translations

Translation files live in `./backend/nextcp2-runtime/src/main/resources/`:

- `messages.properties` — English, **single source of truth**, hand-edited in code.
- `messages_<locale>.properties` (e.g. `messages_de.properties`) — generated/maintained via Tolgee, do not hand-edit.

Config: `tolgee.yaml` (project 31950) and CI in `.github/workflows/translate.yml`. Auto-commits from the workflow are prefixed `chore(i18n)`.

## Ownership

| Concern                  | Owner                | Where to edit              |
| ------------------------ | -------------------- | -------------------------- |
| Keys (add / remove)      | Developer            | `messages.properties`      |
| English values           | Developer            | `messages.properties`      |
| German / other languages | Translators          | Tolgee web UI              |

Do **not** edit English values in the Tolgee UI — they are overwritten on the next CI run. Do **not** hand-edit `messages_de.properties` etc. — the CI overwrites them with what Tolgee returns.

## Workflow

1. Add or remove a key? Edit `messages.properties` (English only).
2. Push to `main`. The CI does the rest: syncs Tolgee, pulls every language, commits the localized files back.
3. Need a German translation? Open Tolgee, translate, save. Next CI run on `main` will commit `messages_de.properties` into the repo.
