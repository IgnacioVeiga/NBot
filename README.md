# NBot (Telegram Bot Template)

Template project for experimenting with Telegram bot commands in Java (Maven, Java 21).

This repository is intentionally generic and is meant to be extended with custom commands and integrations.

## Current Features

- Telegram long polling bot (`telegrambots` library)
- Command router with modular command classes
- Per-user language preference (`English` / `Spanish`)
- Persistent reminders (survive restarts)
- Per-chat message history
- Weather lookup via OpenWeatherMap
- Admin check (`/admin`) using `ADMIN_USER_ID`
- Experimental Python script execution commands (admin-only)

## Language System (EN/ES)

- Users can set language with `/lang en` or `/lang es`
- Alias: `/language en` or `/language es`
- Preference is persisted by Telegram user ID in `data/user_preferences.json`
- If no preference exists, the bot falls back to Telegram's `language_code` when available
- Default fallback is English

## Commands

General commands:

- `/start`
- `/help`
- `/lang <en|es>` (or `/language <en|es>`)
- `/echo <text>`
- `/info`
- `/pic`
- `/quote`
- `/time`
- `/weather <city>`
- `/remind <seconds> <message>`
- `/history`
- `/ping`
- `/uptime`
- `/delete` (reply to a message) or `/delete <messageId>`

Admin / restricted commands:

- `/admin` -> returns `true` only if the sender is admin; otherwise silent
- `/pyhello [args...]` -> runs `scripts/python/hello_admin.py` (admin-only, experimental)
- `/pyrun <script.py> [args...]` -> runs a script from `scripts/python` (admin-only, experimental)

Help visibility:

- `/help` hides admin commands for non-admin users
- `/help` only shows Python commands for admin users when `PYTHON_COMMANDS_ENABLED=true`

## Environment Variables

Required:

- `BOT_TOKEN` - Telegram bot token

Optional:

- `WEATHER_API_KEY` - OpenWeatherMap API key for `/weather`
- `ADMIN_USER_ID` - Telegram numeric user ID used by `/admin` and admin-only commands
- `PYTHON_COMMANDS_ENABLED` - `true/false` (default disabled)
- `PYTHON_BIN` - Python executable (default `python3`)
- `PYTHON_SCRIPTS_DIR` - Allowed scripts directory (default `scripts/python`)

See `.env.example` for the expected keys.

## Persistence Files

The bot writes runtime data under `data/`:

- `data/history/message_history_<chatId>.txt`
- `data/reminders.json`
- `data/user_preferences.json`

These files are ignored by git.

## Build & Run

Compile:

```bash
./mvnw -DskipTests compile
```

Package:

```bash
./mvnw -DskipTests package
```

Run packaged JAR:

```bash
java -jar target/app.jar
```

Or use:

```bash
./run.sh
```

## CI (GitHub Actions)

- Workflow: `.github/workflows/ci.yml`
- Trigger: `push` on any branch except `main`
- Steps:
  - compile (`./mvnw -B -DskipTests compile`)
  - tests (`./mvnw -B test`)
- It does not require valid runtime bot credentials because CI only compiles/tests.

## Project Structure (high level)

- `src/main/java/nbots/telegram/org/commands` - bot commands
- `src/main/java/nbots/telegram/org/services` - integrations and persistence services
- `src/main/java/nbots/telegram/org/i18n` - language selection helpers
- `src/main/java/nbots/telegram/org/utils` - utilities
- `scripts/python` - admin-only Python scripts (experimental)

Example safe utility scripts in `scripts/python`:

- `hello_admin.py` - sample JSON output for `/pyhello`
- `hash_text.py` - hashes text with `sha256`/`sha512` (useful for `/pyrun`)

## Notes

- Python commands are intentionally restricted to admin users only.
- Unauthorized users calling admin-only Python commands receive no response.
- `/help` also hides admin/Python entries when they are not applicable to the current user/configuration.
- `/admin` is a smoke test for future admin-only features.
