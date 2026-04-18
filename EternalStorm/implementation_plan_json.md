# Implementation Plan - Data-Driven Content System (JSON)

This plan outlines the architecture for supporting "Data-Driven" game content. High-level goal: Allow defining standard tiles, items, and walls via JSON files while keeping the ability to use Kotlin classes for complex behavior.

## User Review Required

> [!IMPORTANT]
> **Extensibility**: If a JSON file is used, it will default to the standard logic. If a tile needs special behavior (e.g., custom rendering or interaction), a Kotlin class is still recommended. We can later add a `script` field to JSON if needed.

> [!TIP]
> **Priority Overrides**: Since we use the `ResourceSource` system, a mod can override a base JSON file by providing a JSON with the same path and higher priority, or even override a base Class-based tile by providing a JSON with the same tag.

## Proposed Changes

### Core Content Support

#### [NEW] [DataTileType.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Game/GameContent/TileTypes/DataTileType.kt)
A concrete implementation of `AbstractTileType` that initializes its properties from a JSON object.

#### [NEW] [DataItemType.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Game/GameContent/Items/ItemTypes/DataItemType.kt)
A concrete implementation of `AbstractItemType` that initializes its properties from a JSON object.

#### [NEW] [JsonContentParser.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Core/CoreControllers/Loaders/JsonContentParser.kt)
A utility class to parse Processing's `JSONObject` into game-specific data structures:
- `DropEntry` (parsing "item", "random", "table" types)
- `TileRenderConfig`
- `ItemRenderConfig`

---

### Loader Integration

#### [NEW] [JsonContentLoader.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Core/CoreControllers/Loaders/JsonContentLoader.kt)
A new controller/loader that:
1. Scans all `ResourceSource` directories for `Tiles/*.json` and `Items/*.json`.
2. Automatically registers them into `ObjectRegistration`.

#### [MODIFY] [MainContentRegistration.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Core/CoreControllers/Loaders/MainContentRegistration.kt)
Add a call to `JsonContentLoader.loadAll()` after manual registration to populate the registry with file-based content.

---

### Object Registration Support

#### [MODIFY] [ObjectRegistration.kt](file:///C:/Users/Alexey/Desktop/Eternal-Storm/EternalStorm/src/main/kotlin/Core/CoreControllers/ObjectRegistration.kt)
Add helper methods to register from JSON strings or files.

## Verification Plan

### Automated Tests
- Create a test `test_tile.json` in a temporary `ResourceSource`.
- Verify that `ObjectRegistration.getTileType("eternal_storm:test_tile")` returns a valid instance with the correct properties.
- Verify that the `texture` is correctly resolved through the namespaced system.

### Manual Verification
- Define one existing simple tile (e.g., `StoneTileType`) as a JSON file and remove the class.
- Verify that the stone block still appears correctly in-game and has all previous properties (strength, drop, etc.).
