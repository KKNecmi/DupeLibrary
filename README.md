# DupeLibrary

[![GitHub release](https://img.shields.io/github/release/KKNecmi/DupeLibrary?include_prereleases=&sort=semver&color=blue)](https://github.com/KKNecmi/DupeLibrary/releases/)
[![License](https://img.shields.io/badge/License-MIT-blue)](https://github.com/KKNecmi/DupeLibrary/blob/main/LICENSE)
[![issues - DupeLibrary](https://img.shields.io/github/issues/KKNecmi/DupeLibrary?color=darkgreen)](https://github.com/KKNecmi/DupeLibrary/issues)

**DupeLibrary** plugin for PaperMC 1.21.4 that adds various dupes to Minecraft.

---

## 🚀 Features

- **Toggleable dupes** on/off with a command.
- **Set custom chances** for each dupe.
- Easy-to-use commands and configuration.

---

## 📦 Dupe Modules

| Module              | Description                                | Default State |
|---------------------|--------------------------------------------|---------------|
| `ItemFrameDupe`     | Duplicates items when breaking item frames | Enabled       |
| `PistonShulkerDupe` | Duplicates shulker boxes when pistons push | Enabled       |
| `AnvilDropDupe`     | Duplicates items hit by falling anvils     | Enabled       |

More dupes will be added in the future! We are open to opinions.

---

## 🛠️ Commands

| Command                                | Description                               |
|----------------------------------------|-------------------------------------------|
| `/dupelibrary`                         | Show help and usage instructions          |
| `/dupelibrary list [page]`             | List available dupe modules               |
| `/dupelibrary toggle <module>`         | Enable/disable a specific dupe module     |
| `/dupelibrary <module> chance <value>` | Set the chance (0-100%) for a dupe module |
| `/dupelibrary reload`                  | Reload the configuration                  |

---

## 🧩 Configuration

After first run, the plugin generates a `config.yml` in the plugin folder.  
Example configuration:

```yaml
dupes:
  ItemFrameDupe: true
  ItemFrameDupeChance: 100
  PistonShulkerDupe: true
  PistonShulkerDupeChance: 100
  AnvilDropDupe: true
  AnvilDropDupeChance: 100
