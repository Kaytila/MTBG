# CRITICAL BUG HYPOTHESIS

## Das vermutete Hauptproblem

Es gibt **zwei unterschiedliche Graphics Contexts**:

1. **paintComponent(Graphics g)** → verwendet den Standard Graphics Context
    - NPCs werden hier bei (10*32, 12*32) = (320, 384) + offset (8, 8) = **(328, 392)** gerendert

2. **onMessageEvent(MissilePositionChanged)** → verwendet `this.getGraphics()`
    - Missile wird hier bei **drawX, drawY** gezeichnet

**Das Problem:** `this.getGraphics()` kann einen ANDEREN Koordinatensystem-Kontext haben!

---

## Hypothese 1: Canvas hat einen Scroll/Viewport

Wenn die MapCanvas in einen JScrollPane eingebettet ist, dann:

- `paintComponent()` malt relativ zum Canvas-Ursprung (0,0)
- `getGraphics()` könnte in Fenster-koordinaten malen
- **Offset-Fehler!**

---

## Hypothese 2: getGraphics() ist NULL nach Brief

```java
this.getGraphics().

drawImage(...)
```

Wenn `this.getGraphics()` NULL ist oder alt ist, würde Image gar nicht gezeichnet oder an falscher Position.

---

## Hypothese 3: ROW/COLUMN sind vertauscht!

In UILense.identifyVisibleTilesBest():

```java
for(int row = 0;
row<GameConfiguration.numberOfTiles;row++)
        {
        for(
int column = 0;
column<GameConfiguration.numberOfTiles;column++)
        {
int x = row - offSet.x;
int y = column - offSet.y;
mapTiles[row][column]=currentMap.mapTiles[x][y];
        }
        }
```

Und in paintComponent:

```java
g.drawImage(...,row *tileSize, column *tileSize, this)
```

**Frage: Sind row und column x/y korrekt zugeordnet?**

Die Logic scheint zu sagen:

- row → x-Koordinate (links-rechts)
- column → y-Koordinate (oben-unten)

Das ist richtig! ABER...

---

## Nächster Test

Bitte führen Sie folgende Schritte aus und kopieren Sie die NEUEN Logs:

1. **Starten Sie das Spiel**
2. **Triggern Sie einen Missile-Angriff** (lass NPC schießen)
3. **Schauen Sie auf die Console für**:
   ```
   EXPECTED_NPC_RENDER: spriteDrawTopLeft=[????,????], spriteCenter=[????,????]
   MISSILE_VS_NPC_CENTER: missileSourcePixel=[????,????], expectedNPCCenter=[????,????]
   ```

4. **Wenn diese Werte UNTERSCHIEDLICH sind** → gefunden!

---

## Falls die Werte unterschiedlich sind

Dann kennen wir den exakten Bug:

- Missile berechnet sich für Position A
- NPC rendert sich für Position B
- `A ≠ B`

Und dann können wir sagen WO die Korrektur sein muss.


