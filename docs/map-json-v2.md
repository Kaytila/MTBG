# Map JSON v2 Model

This document describes the Java model for the new `MapTile` JSON schema v2.

## Classes

- `net.ck.mtbg.map.json.MapJsonV2`
    - Root DTO with `schemaVersion` and `map`.
- `MapJsonV2.MapData`
    - Holds `name`, `size`, and `tiles`.
- `MapJsonV2.TileData`
    - Position-based tile DTO (`position` is the primary identity).
- `MapJsonV2.FlagsData`
    - Runtime-relevant booleans (`blocked`, `hidden`, `discovered`, ...).
- `MapJsonV2.ExitData`
    - Exit target map and target coordinates.
- `MapJsonV2.LegacyData`
    - Optional compatibility fields (`id`, `targetID`).

## Usage

Create DTOs from an in-memory map:

```java
MapJsonV2 json = MapJsonV2.fromMap(gameMap);
```

Serialize/deserialize with the streaming utility:

```java
String payload = MapJsonV2IO.toJson(json);
MapJsonV2 parsed = MapJsonV2IO.fromJson(payload);
```

Or use file-based IO:

```java
MapJsonV2IO.writeToFile(json, Path.of("map-v2.json"));
MapJsonV2 parsed = MapJsonV2IO.readFromFile(Path.of("map-v2.json"));
```

Integration helpers in `MapUtils`:

```java
MapUtils.exportCurrentMapAsJsonV2(Path.of("maps/current-map-v2.json"));
MapUtils.translateXmlMapToJsonV2(
    Path.of("maps/legacy-map.xml"),
    Path.of("maps/legacy-map-v2.json")
);
```

## Notes

- Tile identity in v2 is based on `position.x` and `position.y`.
- `legacy.id` and `legacy.targetID` are still included for backward compatibility.

