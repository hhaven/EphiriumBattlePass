# EphiriumBattlePass

`EphiriumBattlePass` is a custom Minecraft battle pass plugin built for Paper `1.21.x`.

It tracks player XP, levels, daily progress, missions, rewards, boosters, and GUI-based battle pass interaction.

## Requirements

- Java `21`
- A Paper server for Minecraft `1.21.x`

Required server plugins:

- Vault
- PlaceholderAPI
- Jobs

Optional integrations:

- CoinsEngine
- EconomyShopGUI-Premium

## Project Setup

This project uses Gradle and targets Java 21.
The repository includes the Gradle wrapper, so you do not need to install Gradle manually.

## Build The Plugin

From the project root, run:

```bash
./gradlew build
```

On Windows, you can also use:

```powershell
.\gradlew.bat build
```

After a successful build, the plugin jar will be generated in:

```text
build/libs/
```

## Run A Local Paper Test Server

This project uses the `xyz.jpenilla.run-paper` plugin, so you can launch a local Paper server directly from Gradle:

```bash
./gradlew runServer
```

On Windows, you can also use:

```powershell
.\gradlew.bat runServer
```

That task will download and start a Paper `1.21` test server and load the built plugin jar automatically.

## Install On Your Server

1. Build the project with `./gradlew build` or `.\gradlew.bat build` on Windows.
2. Copy the generated jar from `build/libs/` into your server's `plugins/` folder.
3. Make sure these required plugins are also installed on the server:
   - Vault
   - PlaceholderAPI
   - Jobs
4. If you want the extra integrations, also install:
   - CoinsEngine
   - EconomyShopGUI-Premium
5. Start or restart the Paper server.

On first startup, the plugin creates:

```text
plugins/EphiriumBattlePass/data.yml
```

This file stores player progress data.

## Included Local Libraries

The project currently references these local jars from the `libs/` folder during compilation:

- `Jobs5.2.6.5.jar`
- `CoinsEngine-2.6.0.jar`
- `EconomyShopGUI-Premium-5.30.1.jar`

If you clone this repository elsewhere, keep the `libs/` folder intact or update `build.gradle` to match your own dependency setup.

## Commands

Player commands:

- `/battlepass`
- `/bpa`
- `/bpa missions`
- `/bpa daily`
- `/bpa help`

Admin commands:

- `/bpadmin <player> addxp <amount>`
- `/bpadmin <player> removexp <amount>`
- `/bpadmin <player> setlevel <level>`
- `/bpadmin <player> addlevel <level>`
- `/bpadmin <player> removelevel <level>`
- `/bpadmin <player> resetrewards`
- `/bpadmin <player> resetmissions`
- `/bpadmin <player> resetall`

## Development Notes

- Main plugin class: `com.ephirium.ephiriumBattlePass.EphiriumBattlePass`
- API version: `1.21`
- Java target: `21`
