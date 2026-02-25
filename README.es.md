# NBot (Plantilla de Bot de Telegram)

Proyecto plantilla para experimentar con comandos de un bot de Telegram en Java (Maven, Java 21).

Este repositorio es intencionalmente genérico y está pensado para extenderse con comandos e integraciones propias.

## Funcionalidad actual

- Bot de Telegram por long polling (`telegrambots`)
- Router de comandos con clases modulares
- Preferencia de idioma por usuario (`English` / `Spanish`)
- Recordatorios persistentes (sobreviven reinicios)
- Historial de mensajes por chat
- Consulta de clima con OpenWeatherMap
- Verificación de admin (`/admin`) usando `ADMIN_USER_ID`
- Comandos experimentales para ejecutar scripts Python (solo admin)

## Sistema de idioma (EN/ES)

- Los usuarios pueden configurar su idioma con `/lang en` o `/lang es`
- Alias: `/language en` o `/language es`
- La preferencia se persiste por ID de usuario de Telegram en `data/user_preferences.json`
- Si no existe preferencia guardada, el bot usa `language_code` de Telegram cuando está disponible
- El fallback por defecto es inglés

## Comandos

Comandos generales:

- `/start`
- `/help`
- `/lang <en|es>` (o `/language <en|es>`)
- `/echo <texto>`
- `/info`
- `/pic`
- `/quote`
- `/time`
- `/weather <ciudad>`
- `/remind <segundos> <mensaje>`
- `/history`
- `/ping`
- `/uptime`
- `/delete` (respondiendo a un mensaje) o `/delete <messageId>`

Comandos de admin / restringidos:

- `/admin` -> retorna `true` solo si el remitente es admin; si no, no responde
- `/pyhello [args...]` -> ejecuta `scripts/python/hello_admin.py` (solo admin, experimental)
- `/pyrun <script.py> [args...]` -> ejecuta un script desde `scripts/python` (solo admin, experimental)

## Variables de entorno

Obligatoria:

- `BOT_TOKEN` - token del bot de Telegram

Opcionales:

- `WEATHER_API_KEY` - API key de OpenWeatherMap para `/weather`
- `ADMIN_USER_ID` - ID numérico de usuario de Telegram usado por `/admin` y comandos solo admin
- `PYTHON_COMMANDS_ENABLED` - `true/false` (deshabilitado por defecto)
- `PYTHON_BIN` - ejecutable de Python (por defecto `python3`)
- `PYTHON_SCRIPTS_DIR` - directorio permitido de scripts (por defecto `scripts/python`)

Ver `.env.example` para las claves esperadas.

## Archivos persistentes

El bot escribe datos de ejecución bajo `data/`:

- `data/history/message_history_<chatId>.txt`
- `data/reminders.json`
- `data/user_preferences.json`

Estos archivos están ignorados por git.

## Build y ejecución

Compilar:

```bash
./mvnw -DskipTests compile
```

Empaquetar:

```bash
./mvnw -DskipTests package
```

Ejecutar el JAR empaquetado:

```bash
java -jar target/app.jar
```

O usar:

```bash
./run.sh
```

## Estructura del proyecto (alto nivel)

- `src/main/java/nbots/telegram/org/commands` - comandos del bot
- `src/main/java/nbots/telegram/org/services` - integraciones y servicios de persistencia
- `src/main/java/nbots/telegram/org/i18n` - helpers de selección de idioma
- `src/main/java/nbots/telegram/org/utils` - utilidades
- `scripts/python` - scripts Python solo admin (experimental)

## Notas

- Los comandos Python están restringidos intencionalmente a usuarios admin.
- Usuarios no autorizados que llamen comandos Python de admin no reciben respuesta.
- `/admin` es una prueba base para futuras funciones exclusivas de administración.
