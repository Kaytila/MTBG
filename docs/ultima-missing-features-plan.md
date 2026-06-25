# Ultima Missing Features Plan

## Problem

The project already has a U4-U6-inspired foundation, but several hallmark systems from Ultima 4, 5, and 6 are still
missing or only partially represented.
The codebase currently shows single-player traversal, basic NPCs, combat, spells, inventory, dialogs, weather, and
scheduling,
but not the broader party, virtue, quest, travel, and world-simulation loops that define the classics.

## Proposed approach

Map the codebase against the major Ultima 4/5/6 gameplay pillars,
then implement the missing systems in dependency order so core travel,
combat, and dialogue can support higher-level quest and virtue mechanics.

## Todos

- Audit current systems against U4/U5/U6 feature pillars and mark gaps by subsystem.
- Design and add a party/companions layer that supports recruited followers, party management, and party-based
  combat/travel.
- Add virtue/karma/reputation state and wire it into dialogue, combat, quests, and progression.
- Expand dialogue and quest handling to support multi-step conversations, quest flags, and branching outcomes.
- Add world-travel features such as boats/ships, mounts, and map transitions that match classic exploration loops.
- Extend dungeon/overworld progression with stronger map-state hooks, locks/keys, and progression gates.
- Fill out missing magic/combat support where the current spell and action systems are still placeholders.

## Notes

- The existing code already has useful scaffolding: NPC schedules, map transitions, spell data, combat actions,
  inventory, and UI dialogs.
- The biggest design risk is feature scope: U4, U5, and U6 overlap but are not identical,
- so the final implementation should decide whether to target a unified classic Ultima ruleset or a game-specific blend.
- Several current interfaces are broad and stub-heavy;
- some plan items will likely need contract cleanup so new systems do not inherit null/stub behavior.
