# Copilot / LLM Instructions for This Repository

## Project Purpose

This is a Java 21 + Maven Telegram bot template for experimentation.
It is not product-specific yet. The codebase is intentionally simple and easy to extend with commands.

## Core Architecture

- Entry point: `src/main/java/nbots/telegram/org/Main.java`
- Bot update consumer and command registration: `src/main/java/nbots/telegram/org/NBot.java`
- Command router: `src/main/java/nbots/telegram/org/commands/CommandHandler.java`
- Commands: `src/main/java/nbots/telegram/org/commands/*`
- Services (Telegram actions, weather, reminders, admin auth, python runner, user language prefs): `src/main/java/nbots/telegram/org/services/*`
- I18n helpers: `src/main/java/nbots/telegram/org/i18n/*`
- Utilities: `src/main/java/nbots/telegram/org/utils/*`

## Important Runtime Constraints

- Do **not** read or modify `.env` directly in automation tasks.
- If environment keys need to be documented, update `.env.example` only.
- Admin-only commands must remain silent for unauthorized users unless explicitly specified otherwise.
- Python script execution is experimental and must stay restricted to admin users.

## Internationalization Rules (Important)

- User-facing bot responses should be localized (English + Spanish) using:
  - `I18n.language(update)`
  - `I18n.t(language, englishText, spanishText)`
- User language preference is persisted in `data/user_preferences.json` via `UserLanguageService`
- `/lang` and `/language` are the user-facing configuration commands for language
- Admin-exclusive messages may remain English-only if needed

## Persistence Files

Runtime-generated files are stored under `data/` and should not be committed:

- `data/history/`
- `data/reminders.json`
- `data/user_preferences.json`

## Python Script Execution (Experimental)

- Allowed scripts live in `scripts/python/`
- Commands:
  - `/pyhello` -> sample admin-only script
  - `/pyrun <script.py> ...` -> generic admin-only script runner
- The runner validates script names and restricts execution to the configured scripts directory
- Controlled by env vars:
  - `PYTHON_COMMANDS_ENABLED`
  - `PYTHON_BIN`
  - `PYTHON_SCRIPTS_DIR`

## Build / Validation

Prefer validating changes with:

```bash
./mvnw -DskipTests compile
```

For package validation:

```bash
./mvnw -DskipTests package
```

## Coding Guidance

- Preserve the modular command-per-file style
- Prefer small services for external integrations and persistence
- Keep command handlers thin (parse input, call services, format response)
- Avoid adding dependencies unless necessary
- When adding user-facing text, update both English and Spanish variants
