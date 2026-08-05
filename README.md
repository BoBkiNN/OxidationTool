
## OxidationTool

Simple Minecraft Paper plugin that adds tool to adjust oxidation of copper blocks.

### Features
* Supports all oxidizable blocks, including doors
* Keeps block state (rotation and other states are preserved)
* Cycle through oxidation phases
* Decrease oxidation on left click

### Requirements:
* Java 21 or newer
* Minecraft Paper 1.21.4 or newer

### Installation:

1. Copy plugin jar into `plugins` folder.
2. Install [CommandAPI](https://modrinth.com/plugin/commandapi)
3. Done

### Usage

1. Obtain tool using `/oxidizer give`
2. Use RMB on oxidizable block to increase oxidation
3. Use LMB on oxidizable block to decrease oxidation

Usage requires `oxidizer.use` permission

### Commands:
Commands permission: `oxidizer.command`
* `/oxidizer give <player>` - gives tool to player or to self if no player argument supplied.
  Permission: `oxidizer.command.give` and `oxidizer.command.give.other`
* `/oxidizer reload` - reloads configuration.
  Permission: `oxidizer.command.reload`

#### Adding custom texture
You can use `oxidizer:tool` string from Custom Model Data in custom resource pack.

### Configuration:

After installing and running plugin for first time, 
see `plugins/OxidationTool/config.yml` to change messages and settings

Default config: [config.yml](/src/main/resources/config.yml)
