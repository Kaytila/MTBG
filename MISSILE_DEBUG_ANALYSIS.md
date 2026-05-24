# Missile Coordinate System - Comprehensive Analysis

## Key Configuration Values

- `tileSize` = **32 pixels**
- `numberOfTiles` = **15** (visible grid is 15x15)
- `imageSize` = **(16, 16)** ← **IMPORTANT: NPCs are 16x16 pixels**
- Canvas size = 15 * 32 = **480x480 pixels**

## Coordinate Systems

### 1. MAP COORDINATES (world/game coordinate system)

- Range: typically 0-1000+ for large maps
- Example: Player at map(50, 50)

### 2. UI COORDINATES (visible tile grid)

- Range: 0-14 (for 15x15 visible grid)
- Center: UI(7, 7) corresponds to player position
- Formula: `uiPos = playerUIPos + (mapPos - playerMapPos)`

### 3. SCREEN PIXEL COORDINATES (what's actually drawn)

- Range: 0-480 (for 15x15 * 32 tiles)
- For a tile at UI(row, col): Top-left pixel = `(row*32, col*32)`

## Rendering Formula

### Tile Rendering

For a tile at UI position (row, column):

```
screenPixel = (row * 32, column * 32)           // top-left
```

### NPC Rendering (Because imageSize = 16x16 < tileSize = 32)

For an NPC at UI position (row, column):

```
renderTopLeft = (row * 32 + 8, column * 32 + 8)  // offset = tileSize/4
npcVisualCenter = (row * 32 + 16, column * 32 + 16)  // 8 + imageSize/2
```

### Missile Rendering

Missile coordinates are **screen pixel centers**:

```
screenPixelCenter = (row * 32 + 16, column * 32 + 16)
drawTopLeft = (screenPixelCenter.x - missileImageWidth/2, screenPixelCenter.y - missileImageHeight/2)
```

## Missile Coordinate Calculation Flow

### For a Ranged Attack

1. **Get Attacker's Map Position**
   ```
   sourceMapPosition = attacker.getMapPosition()
   ```

2. **Calculate Attacker's UI Position**
    - If attacker is PLAYER: `sourceUIPosition = player.getUIPosition()` (cached)
    - If attacker is NPC: `sourceUIPosition = calculateUIPositionFromMapOffset(sourceMapPosition)`
      ```
      offset = sourceMapPosition - playerMapPosition
      sourceUIPosition = playerUIPosition + offset
      ```

3. **Convert UI Position to Screen Pixel (CENTER)**
   ```
   sourcePixel = (sourceUIPosition.x * 32 + 16, sourceUIPosition.y * 32 + 16)
   ```

4. **Missile represents screen pixels**
   ```
   missile.sourceCoordinates = sourcePixel
   missile.targetCoordinates = targetPixel
   ```

## EXPECTED vs ACTUAL

### Expected Flow (CORRECT):

1. NPC at map(55, 50), player at map(50, 50) with UI(7, 7)
2. offset = (55-50, 50-50) = (5, 0)
3. NPC UI = (7+5, 7+0) = (12, 7)
4. NPC render: top-left (12*32+8, 7*32+8) = (392, 232)
5. NPC visual center: (392+8, 232+8) = (400, 240)
6. Missile source: 12*32+16, 7*32+16 = (400, 240) ✓ **MATCHES**

## Potential Issues to Check

1. **calculateUIPositionFromMapOffset** returning wrong values
    - Should: return playerUIPos + (mapPos - playerMapPos)
    - Check: Player position might have changed since NPC was spawned

2. **Missile line calculation** (MapUtils.getLine)
    - Takes two screen pixel coordinates
    - Returns list of intermediate points
    - Should work correctly if inputs are correct

3. **NPC image rendering** vs **missile rendering**
    - NPC: drawn at (row*32+8, col*32+8) with size 16x16
    - Missile: drawn centered on screen pixel coordinate
    - If offset logic differs → visual mismatch

4. **Player movement between missile creation and rendering**
    - Missile created with specific source/target pixels
    - If player moves between frames, visible positions would diverge
    - Currently this shouldn't cause initial placement issue

## Debug Output Expectations

In logs with debugNPC=true and debugMapPosition=true:

### Healthy logs:

```
=== MISSILE CALCULATION DEBUG ===
attackerId=1, isPlayer=false
PLAYER: mapPos=java.awt.Point[x=50,y=50], uiPos=java.awt.Point[x=7,y=7], uiOffsetFromMap=java.awt.Point[x=-43,y=-43]
ATTACKER: mapPos=java.awt.Point[x=55,y=50], referencedMapPos=java.awt.Point[x=55,y=50], mapOffsetFromPlayer=java.awt.Point[x=5,y=0]
SOURCE_CALC: uiPos=java.awt.Point[x=12,y=7], screenPixel=java.awt.Point[x=400,y=240]
TARGET_CALC: uiPos=java.awt.Point[x=15,y=8], screenPixel=java.awt.Point[x=496,y=272]

missile render: center=java.awt.Point[x=400,y=240], drawTopLeft=(398, 238), imageSize=4x4
```

### What to look for:

1. Does `uiPos` match where NPC actually appears on screen?
2. Does `screenPixel` match the visual center of the NPC sprite?
3. Does missile render position match these calculations?


