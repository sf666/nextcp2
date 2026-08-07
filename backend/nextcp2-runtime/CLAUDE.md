# Runtime configuration

`FileConfigPersistence` locates `nextcp2config.json` at startup. If `NEXTCP_DATA` (env) / `-Dnextcp.dataDir` is set it is **authoritative**: the config is read from — or generated into — that directory, ignoring the legacy locations. This is what the Docker image uses (`NEXTCP_DATA=/nextcp2_data`, mounted as a volume). Otherwise the search order is: `-DconfigFile=…`, the platform per-user data dir (see below), `/etc/nextcp2/`, `$HOME/`, then the working directory.

If none is found, a default config is generated in the resolved **data directory** and found again on the next start (so settings survive restarts / app updates). The data directory is the `NEXTCP_DATA` override, or platform-specific:

- macOS: `~/Library/Application Support/nextcp2`
- Windows: `%APPDATA%\nextcp2` (fallback `~\nextcp2`)
- Linux/other: `$XDG_CONFIG_HOME/nextcp2` or `~/.config/nextcp2`

Inside the data directory the app creates `logs/`, `upnp_code/` and `tmp/` sub-directories (`tmp/` is the internal streaming proxy's pre-transcode cache, i.e. `localPlayerCacheDir`); the generated `logback.xml` points `LOG_DIR` at `logs/`, and the H2 database lives in the data-dir root. Existing hand-edited configs and an explicit `-DconfigFile` are always respected.

More env overrides seed the **generated default config only** (ignored once a config exists): `NEXTCP_LIB` / `-Dnextcp.libDir` sets `libraryPath` (the device-driver dir — Docker points it at `/nextcp2/lib` where the MA9000 driver ships), `NEXTCP_PORT` / `-Dnextcp.port` sets `embeddedServerPort` (default 8085), and `NEXTCP_BIND_INTERFACE` / `-Dnextcp.bindInterface` sets `upnpBindInterface`. When the bind interface is unset **and** a data-dir override is present (Docker), the primary host interface is auto-detected.

## Translations

`messages.properties` in `src/main/resources/` is the English single source of truth
and the only translation file you may hand-edit. See the `translations` skill for the
full workflow and ownership rules.
