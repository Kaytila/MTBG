# Ultima Implementation Roadmap

## Scope

Party/companions are intentionally excluded. This roadmap covers the missing Ultima 4/5/6 pillars the codebase already
hints at: virtue, quests, dialogue, travel, dungeons, magic, combat, and economy/services.

## Phase 1: Core foundation

**Goal:** make higher-level systems safe to build on.

- Audit current contracts in `AbstractEntity`, `LifeForm`, `Player`, `NPC`, and `World`.
- Remove or replace stub-heavy behavior where it breaks polymorphism.
- Normalize shared action/result handling for combat, dialogue, and travel.
- Add a small state model for persistent progression flags.

### Phase 1 breakdown

1. **Contract cleanup**
    - Identify methods in `LifeForm` that are too broad for all entity types.
    - Remove or narrow stub-only behavior in `Player`, `NPC`, and `World`.
    - Make inherited state consistent with overridden getters/setters.

2. **Action/result normalization**
    - Centralize success/failure handling for movement, combat, spellcasting, and interaction actions.
    - Make action execution return a predictable result instead of relying on side effects alone.
    - Ensure null or invalid targets fail safely and consistently.

3. **Persistent progression flags**
    - Add a compact state container for story flags, quest flags, and world-state switches.
    - Keep it simple enough to persist with save/load later.
    - Use it as the shared dependency for virtue, quests, travel locks, and dungeon gates.

4. **Validation and safety layer**
    - Define the minimum invariants for entity creation and map interaction.
    - Prevent inherited methods from silently returning invalid placeholder values.
    - Add lightweight checks so later systems can trust core entity behavior.

### Phase 1 deliverables

- A cleaner entity contract with fewer fake/stub implementations.
- Shared action outcome handling used by player, NPC, and world logic.
- A reusable progression-state model for later phases.
- A stable base that Phase 2+ can build on without reworking core entity behavior.

**Depends on:** nothing.

## Phase 2: Virtue and reputation

**Goal:** add the Ultima-style moral layer.

- Add virtue, karma, and reputation tracking to the player and world state.
- Define events that increase or decrease virtue.
- Wire reputation into NPC reactions and quest outcomes.
- Persist virtue state in save/load data.

### Phase 2 breakdown

1. **Virtue model**
    - Define the virtue set the game will track and how each virtue changes.
    - Keep the model small enough to serialize cleanly with save/load data.
    - Support both per-virtue values and an overall moral/reputation summary if needed.

2. **Karma and reputation rules**
    - Define what actions raise or lower karma/reputation.
    - Connect world events, NPC interactions, combat outcomes, and quest resolution to those rules.
    - Make the rules explicit so they can be tuned later without changing game flow code.

3. **Persistence and state access**
    - Store virtue/reputation state in the same progression layer introduced in Phase 1.
    - Expose read/write access through a small API so NPCs, quests, and travel systems can query it.
    - Ensure loaded games restore the same moral state that was saved.

4. **Gameplay hooks**
    - Trigger virtue changes from dialog decisions, quest completion, theft, murder, healing, and similar events.
    - Let NPCs react differently based on player virtue or reputation.
    - Reserve room for shrine-style or blessing-style progression hooks later.

### Phase 2 deliverables

- A persistent virtue/reputation system.
- Clear rules for how gameplay changes moral standing.
- NPC/quest hooks that can query virtue and reputation.
- A stable moral layer ready for quest branching and world gating.

**Depends on:** Phase 1.

## Phase 3: Dialogue and quests

**Goal:** support branching conversation and quest logic.

- Replace one-off talk handling with dialogue state and quest flags.
- Add branching responses, quest acceptance, and quest completion states.
- Connect NPC dialogue to virtue/reputation checks.
- Add journal-style tracking for active and completed quests.

### Phase 3 breakdown

1. **Dialogue state model**
    - Introduce a dialogue state container that can remember where a conversation left off.
    - Support per-NPC topics, one-time lines, repeat lines, and conditional branches.
    - Keep the model compatible with the existing talk and message UI flow.

2. **Quest flag system**
    - Add quest start, in-progress, completed, failed, and hidden states.
    - Store quest flags separately from dialogue text so the same NPC can drive multiple quests.
    - Support simple boolean flags first, then expand to value-based conditions later if needed.

3. **Branching NPC interactions**
    - Make NPC responses depend on quest state, world state, and virtue/reputation where relevant.
    - Allow dialogue choices to set flags, unlock new topics, or trigger map/world changes.
    - Reuse the existing message and talk dialogs instead of inventing a parallel UI path.

4. **Quest journal and feedback**
    - Add a readable journal view for active and completed quests.
    - Record quest updates when flags change, not only when a quest is explicitly completed.
    - Keep quest feedback consistent across dialogs, combat outcomes, and map events.

### Phase 3 deliverables

- Quest state and dialogue state that can persist with the rest of game state.
- NPC conversations that branch based on conditions instead of only returning static text.
- A basic journal or quest log for the player.
- Quest hooks that can feed into later virtue, travel, and dungeon systems.

**Depends on:** Phases 1-2.

## Phase 4: Travel and exploration

**Goal:** support Ultima-style world traversal.

- Add mount and vehicle travel states.
- Add boat/ship traversal and map transition rules.
- Gate movement by terrain, vehicle type, and story state.
- Extend map metadata to support travel restrictions and special exits.

### Phase 4 breakdown

1. **Travel state model**
    - Add explicit travel modes for walking, mounted travel, and vehicle travel.
    - Track whether the player is currently on foot, mounted, in a boat, or in another transport state.
    - Keep travel state separate from combat state so it can be reused across maps.

2. **Terrain-aware movement**
    - Use tile type and map metadata to decide whether a movement action is allowed.
    - Gate restricted terrain behind the correct travel mode instead of hardcoded special cases.
    - Reuse the existing map lookup and movement logic rather than adding a second traversal system.

3. **Boats, ships, and mounts**
    - Add vehicle entities or vehicle state representations for boats and mounts.
    - Allow entry/exit at valid docks, stables, and story-specific transition points.
    - Define what happens when a vehicle is dismissed, destroyed, or cannot be used on a map.

4. **Map transition rules**
    - Extend map exits so they can require a travel mode, item, or story flag.
    - Support special exits such as docks, cave entrances, and transport-only transitions.
    - Make transitions respect the same progression-state model used by quests and dungeon gates.

5. **Exploration feedback**
    - Add UI or state feedback for current travel mode and travel restrictions.
    - Make blocked travel explain why it failed instead of failing silently.
    - Keep map traversal behavior consistent between player movement and scripted map transitions.

### Phase 4 deliverables

- A reusable travel-mode model.
- Vehicle and mount support for exploration.
- Terrain and exit gating driven by map/state data.
- Unified travel feedback for blocked or special movement cases.

**Depends on:** Phase 1.

## Phase 5: Dungeons and progression gates

**Goal:** make world progression feel classic and structured.

- Add key/lock and dungeon-gate logic.
- Add map-state progression hooks for blocked paths, hidden exits, and story gates.
- Tie dungeon progression to quest completion and virtue thresholds where appropriate.

**Depends on:** Phases 1-3.

## Phase 6: Magic and combat depth

**Goal:** complete the tactical loop.

- Expand spell handling beyond the current small spell set.
- Add spell costs, targeting rules, and status effects.
- Expand combat feedback and enemy behavior variety.
- Add terrain-aware or context-aware combat rules where needed.

### Spell list for the roadmap

**Tier 1**

- Magic Arrow
- Heal
- Light
- Cure
- Fireball

**Tier 2**

- Sleep
- Poison
- Unlock Magic
- Telekinesis
- Shield

**Tier 3**

- Fire Field
- Energy Field
- Paralyze
- Resurrection
- Dispel Magic

**Tier 4**

- Recall / Return
- Gate Travel
- Invisibility
- Blink
- X-Ray
- Mass Heal

**Tier 5**

- Summon
- Time Stop
- Mass Dispel
- Full Restoration
- World-affecting spell / ritual hook

**Notes on the spell list**

- Keep the currently implemented spells (`Fireball`, `Heal`, `Resurrection`) as anchors.
- Use the tiers to drive spellbook filtering and progression.
- Gate stronger spells behind world state, resources, or virtue/reputation where appropriate.

**Depends on:** Phase 1.

## Phase 7: Economy and services

**Goal:** support town-loop gameplay.

- Add vendor/shop interaction, buy/sell pricing, and service NPCs.
- Add inns, healing, and training-style interactions if supported by the game loop.
- Make item/NPC/dialogue systems consistent with town services.

**Depends on:** Phases 1-3.

## Ultima 7 follow-on track

**Goal:** add the U7-style systemic interaction layer after the classic Ultima core is in place.

- Add scripted world simulation with triggers, conditions, and scheduled world actions.
- Add contextual object interaction: use-on-object, combine items, inspect, move, and manipulate world objects.
- Extend mouse-driven UI flows for object-heavy interaction while keeping keyboard support intact.
- Add richer container and furniture behavior where items can be nested, moved, combined, or transformed.
- Add world reactivity: doors, NPCs, weather, item state, and map objects should respond to triggers and flags.
- Add authored world scripts for map regions, NPCs, and service objects rather than hardcoded event logic.

### U7 breakdown

1. **Scripted world engine**
    - Build a trigger/action system that can react to time, location, quest flags, virtue, and object state.
    - Support one-shot, repeating, delayed, and conditional world events.
    - Let map, NPC, and object scripts share the same execution model.

2. **Object interaction layer**
    - Add `use on`, `combine`, `inspect`, and `transform` style interactions.
    - Expand inventory/container behavior so objects can be manipulated in world context, not only in menus.
    - Reuse the existing drag-and-drop UI as the interaction surface, since that part already exists.

3. **World simulation hooks**
    - Let NPC schedules, map objects, and service objects react to player actions and world state.
    - Support visible changes to the world: moving objects, opening paths, changing object state, and scripted
      responses.
    - Persist scripted world state alongside quests and virtues.

4. **Mouse-first contextual UX**
    - Add context-sensitive actions for objects, tiles, and NPCs.
    - Present available actions based on what the cursor is over and what the player is holding.
    - Keep keyboard shortcuts available for core travel/combat actions.

### U7 deliverables

- A reusable world-script engine.
- Contextual object interaction beyond basic inventory drag/drop.
- Scripted world state changes tied to player actions.
- U7-style object and world reactivity layered on top of the classic roadmap.

## Ranked missing entity checklist

**P0 - foundational**

- `Quest`
- `QuestStep`
- `QuestJournalEntry`
- `DialogueNode`
- `DialogueChoice`
- `ConversationState`
- `VirtueProfile`
- `ReputationRecord`
- `WorldScript`
- `WorldTrigger`
- `WorldAction`

**P1 - progression and traversal**

- `TravelMode`
- `Vehicle`
- `Boat`
- `Ship`
- `Mount`
- `DungeonGate`
- `MapGate`
- `LockableExit`

**P2 - U7-style interaction**

- `InteractableObject`
- `WorldObject`
- `UseTarget`
- `CombineTarget`
- `ServiceLocation`
- `VendorService`
- `InnService`
- `TrainerService`
- `Shrine`
- `Temple`

**Already covered in code**

- Role NPCs such as `Guard`, `Innkeeper`, `Storekeeper`, and `Cleric` are already represented by `NPCType`.
- Drag-and-drop inventory is already implemented in the UI layer.

## Delivery order

1. Foundation cleanup.
2. Virtue/reputation.
3. Dialogue/quests.
4. Travel/exploration.
5. Dungeons/progression gates.
6. Magic/combat depth.
7. Economy/services.
8. U7 follow-on systems.

## Notes

- The current engine already has useful scaffolding for map loading, NPC schedules, combat actions, spells, dialogs, and
  inventory.
- The biggest implementation risk is contract drift: new systems should avoid depending on stubbed `LifeForm` methods.
- If scope needs to be tightened later, cut from the bottom up: economy first, then combat depth, then travel, while
  keeping virtue and quest state intact.
