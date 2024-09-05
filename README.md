# SimpleSkinBackport

HEAVY WIP

Attempts to backport the modern skin format with mixins in a *simple, compatible* way. I created this to hopefully solve the issue of there being no simple skin backport once and for all. I always tell people Ears was the best one, but in reality it cannot maintain perfect compatibility due to adding extra features that some may not want.

This mod currently has no plans to add extra features beyond backporting the skin format and maintaining compatibility with other mods. For extra skin features, try [MorePlayerModels](https://www.curseforge.com/minecraft/mc-mods/more-player-models) or [Ears](https://modrinth.com/mod/ears/versions) instead.

Alternate skin server mods should just work, but would likely need manual patching for slim arm support.

Current Features:
- 1.8 skin format
  - Slim arm support
  - Extended hat layer support
- Translucent hat layer support
- Fix overlap in hat layer when wearing a head
- Hat layers on head blocks
- New default skins alongside just Steve
- Patch for Twilight Forest giants

Todo:
- Config
  - Toggle head block inventory icon render
  - Toggle new default skins (Either Steve/Alex or JUST Steve)

Current incompatibility todo:
- Twilight Forest heads
- Smart Moving (it's like 90% working slim models are just slightly wrong)
- Botania Gaia Guardian
