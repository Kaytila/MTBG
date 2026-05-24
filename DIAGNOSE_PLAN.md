# Systematischer Diagnose-Plan: Missile-Positionsberechnung

## Summary der bisherigen Analyse

### Was funktioniert NICHT:

- Missile startet nicht von der korrekten Position auf dem Bildschirm
- Spieler und NPC berichten unterschiedliche Fehler
- Interne Logs zeigen Konsistenz, aber visuelles Rendering ist falsch

### Was wir überprüft haben:

1. ✅ Missile-Rendering: Centers korrekt (Offset um Hälfte der Bildgröße)
2. ✅ Koordinaten-Konsistenz: Berechnung stimmt mit Rendering überein (intern)
3. ✅ Turn-basierte Positionen: NPC-Snapshots werden erfasst
4. ❓ **NICHT überprüft**: Ist `calculateUIPositionFromMapOffset()` korrekt?
5. ❓ **NICHT überprüft**: Wird der Player-UI-Position korrekt aktualisiert?

---

## Die Kritische Frage

Das System funktioniert so:

```
1. Attack-Berechnung (AbstractEntity.attack):
   sourceUIPosition = calculateUIPositionFromMapOffset(sourceMapPos)
   
2. Pixel-Konvertierung:
   sourcePixel = sourceUIPosition.x * 32 + 16
   
3. Missile Animation (MissileTimerTask):
   currentPos = getLine(...)[0]  ← startend bei sourcePixel
   
4. Missile Rendering (MapCanvas.onMessageEvent):
   drawX = currentPos.x - imageWidth/2
   drawY = currentPos.y - imageHeight/2
```

**WENN `sourceUIPosition` FALSCH IST → ALLES IST FALSCH**

---

## Was ist `Player.getUIPosition()`?

Das ist der **ZENTRALE SCHLÜSSEL**. Er wird verwendet in:

```java
// In MapUtils.calculateUIPositionFromMapOffset():
Point playerUIPos = Game.getCurrent().getCurrentPlayer().getUIPosition();
return new

Point(playerUIPos.x +offSet.x, playerUIPos.y +offSet.y);
```

**Frage**: Wo wird diese Position gesetzt und aktualisiert?

Ich fand in Game.java (processNPCActions):

```java
e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
```

Aber **NIRGENDWO** wird `player.setUIPosition()` aufgerufen!

---

## VERMUTUNG: Player UIPosition wird NICHT aktualisiert

### Scenario:

1. Game startet: Player bei Map(50,50), UI(7,7) ← wird irgendwann gesetzt
2. Player bewegt sich: Map(55,50) ← aber UI-Position bleibt bei (7,7)!
3. NPC will angreifen:
   ```
   offset = npcMap - playerMap = (55-50, y) = (5, y)
   npcUI = playerUI + offset = (7+5, 7+y) = (12, 7+y)  ← FALSCH!
   weil playerUI sollte (7,7) sein für alle Positionen
   ```

### Aber:

- Wenn Player UI-Pos IMMER (7,7) sein sollte (als Mitte des 15x15 Grids)
- Dann ist die Berechnung korrekt
- ABER: Was wenn es wird irgendwann auf einen anderen Wert gesetzt?

---

## Fünf mögliche Fehlerquellen

### 1. Player UIPosition wird nicht als (7,7) initialisiert

Möglicherweise ist es null oder ein anderer Wert

### 2. Player UIPosition wird irgendwann aktualisiert und bleibt stecken

Z.B. auf (10, 10) nach einem Movement und wird nicht zurückgesetzt

### 3. Es gibt eine Race-Condition

Player-Position wird aktualisiert während Missile wird berechnet

### 4. calculateUIOffsetFromMapPoint() ist falsch

Wird verwendet um Tiles zu finden - wenn das falsch ist, ist auch UI-Position falsch

### 5. Es gibt einen globalen Offset in der Render-Pipeline

Z.B. Canvas viewport wird verschoben, aber Missile-Berechnung nicht

---

## Debug-Output der nächsten 5 Minuten

Ich habe bereits folgende Logs hinzugefügt. Bitte führen Sie folgendes aus:

### Schritt 1: Spiel starten

```
Beobachten Sie die Console für:
- "=== MISSILE CALCULATION DEBUG ===" Nachrichten
- "calculateUIPositionFromMapOffset:" Nachrichten
```

### Schritt 2: Player zur selben Map-Position bewegen, dann NPC angreifen lassen

Z.B.:

1. Drucken Sie die aktuelle Player-Position (z.B. Map(50,50))
2. Bewegen Sie den Player NICHT
3. NPC anschießen → Missile sollte von der korrekten Position starten

### Schritt 3: Player bewegen, dann NPC angreifen lassen

1. Player bewegt sich zu Map(55,50)
2. NPC anschießt → **Beobachten Sie, ob Missile-Ursprung falsch ist**

---

## Was ich aus den Logs überprüfen werde

### Aus AbstractEntity.attack():

```
PLAYER: mapPos=?, uiPos=?  ← sollte IMMER uiPos=(7,7) sein!
ATTACKER: mapPos=?, mapOffsetFromPlayer=?
SOURCE_CALC: uiPos=?, screenPixel=?
```

### Aus MapUtils.calculateUIPositionFromMapOffset():

```
mapPos=?, playerMapPos=?, mapOffset=?, playerUIPos=?, result=?, inBounds=?
```

### Kritische Vergleiche:

1. **playerUIPos** - sollte IMMER (7,7) sein wenn Player UI centered
2. **inBounds** - sollte IMMER true sein (Missile auf Bildschirm)
3. **screenPixel** - muss mit Attacker-Sprite-Center übereinstimmen

---

## Wenn alles richtig ist:

**Dann liegt das Problem in der Rendering-Pipeline:**

- MapCanvas.paintComponent() zeichnet Tiles falsch
- Viewport-Offset ist falsch
- UILense.identifyVisibleTilesBest() gibt falsche Tiles zurück

---

## Nächste sofortige Test:

1. **Starten Sie das Spiel und bewegen Sie NOTHING**
2. **Aktivieren Sie einen Angriff (NPC oder Player)**
3. **Kopieren Sie ALLE relevanten Logs**
4. **Senden Sie die Logs - ich werde die exakte Fehlerquelle identifizieren**

---

## Meine Hypothese (80% Sicherheit):

`Game.getCurrent().getCurrentPlayer().getUIPosition()` ist **NULL** oder **FALSCH INITIALISIERT**,
daher wird es durch 0 oder einen Standardwert ersetzt, was alle Berechnungen bricht.


