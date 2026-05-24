# Kritische Erkenntnisse - Missile-Positionsberechnung

## Das Kernproblem

Nach gründlicher Analyse des Codes habe ich ZWEI UNTERSCHIEDLICHE RENDERING-SYSTEME identifiziert:

### System 1: NORMAL MISSILE ANIMATION (AKTIV)

- **Datei**: MapCanvas.onMessageEvent(MissilePositionChanged)
- **Koordinatensystem**: SCREEN PIXELS (absolute Bildschirmkoordinaten)
- **Berechnung**:
  ```
  sourcePosition = (sourceUIPos.x * 32 + 16, sourceUIPos.y * 32 + 16)
  targetPosition = (targetUIPos.x * 32 + 16, targetUIPos.y * 32 + 16)
  getLine(sourcePosition, targetPosition)  ← in PIXEL-KOORDINATEN
  drawImage(missile, currentPos.x - width/2, currentPos.y - height/2)
  ```

### System 2: TILE-BASED MISSILE RENDERING (INAKTIV @SuppressWarnings)

- **Datei**: MapCanvas.paintMissilesTileBased() (NICHT VERWENDET)
- **Koordinatensystem**: UI-POSITIONEN (Gitter 0-14)
- **Berechnung**:
  ```
  m.setCurrentPosition(calculateUIPositionFromMapOffset(...))  ← In UI-KOORDINATEN!
  getLine(uiPosition, uiPosition)  ← in UI-KOORDINATEN
  drawImage(missile, uiX * 32 + 16, uiY * 32 + 16)
  ```

## Potenzielle Fehlerquelle

Das **normale Rendering-System** arbeitet mit SCREEN-PIXEL-KOORDINATEN. Aber was wenn:

1. **NPCs werden möglicherweise nicht korrekt auf dem Bildschirm positioniert**:
    - NPCs sind 16x16 Bilder (nicht 32x32)
    - Sie werden mit OFFSET (tileSize/4 = 8) gezeichnet
    - Ihre visuelle Mitte ist bei: (row*32 + 16, column*32 + 16)
    - **Missile sollte von dieser Mitte starten**

2. **Aber was wenn der ATTACKER eine falsche UI-Position hat?**
    - Wenn calculateUIPositionFromMapOffset() für einen NPC FALSCH BERECHNET
    - Dann wird die Missile von der FALSCHEN SCREEN-POSITION aus gestartet

## Was ich überprüfen muss

### Hypothesis 1: calculateUIPositionFromMapOffset gibt FALSCHE UI-POSITIONEN zurück

Die Formel:

```java
uiPos =playerUIPos +(mapPos -playerMapPos)
```

Falls die Missile von UI-Position außerhalb des sichtbaren Bereichs (0-14) berechnet wird:

- UI-Position könnte negativ sein: UI(-5, 3) z.B.
- Screen-Pixel würde dann: -5 * 32 + 16 = -144 sein
- Missile würde LINKS/außerhalb des Fensters gezeichnet

### Hypothesis 2: NPC-Sprite und Missile-Ursprung verwenden unterschiedliche "Zentrum"-Definitionen

- NPC-Sprite-Zentrum: (row*32 + 8 + 8, col*32 + 8 + 8) = (row*32 + 16, col*32 + 16)
- Missile-Berechnung sollte: (sourceUIPos.x * 32 + 16, sourceUIPos.y * 32 + 16)
- **Diese sollten identisch sein - IST ABER DIE UI-POSITION KORREKT?**

### Hypothesis 3: Es gibt ein OFF-BY-ONE oder TRANSFORM-Problem

- Player-Position ändert sich zwischen Attack-Berechnung und Rendering
- Missile wird mit altem Player-Position berechnet
- Wird aber mit neuer Player-Position gerendert

## Nächste Debug-Schritte

Müssen folgende Logs WÄHREND eines Missile-Angriffs erfassen:

1. **In AbstractEntity.attack()** (Missile-ERSTELLUNG):
    - playerMapPos, playerUIPos
    - attackerMapPos, attackerUIPos (calculated)
    - sourcePosition (screen pixel)
    - targetPosition (screen pixel)

2. **In MapCanvas.paintComponent()** (Tile-RENDERING):
    - Wo wird der Attacker gerender?
    - row, column Werte für Attacker-Sprite

3. **In MapCanvas.onMessageEvent(MissilePositionChanged)** (Missile-RENDERING):
    - m.getCurrentPosition() vs. erwartet (sollte mit Attacker-Sprite-Zentrum übereinstimmen)
    - drawTopLeft (tatsächlich gezeichnete Position)

## Test-Szenario

1. Player auf Map(50,50) mit UI(7,7)
2. NPC auf Map(55,50)
3. NPC greift Player an: Missile sollte VON (400, 240) kommen

**Wenn Missile erscheint bei (200, 240), (400, 120) oder andere Offset:**
→ Dann ist die calculateUIPositionFromMapOffset FALSCH


