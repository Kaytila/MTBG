package net.ck.mtbg.test;

import net.ck.mtbg.backend.actions.NPCAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.entities.*;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.queuing.ScheduleActivity;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.communication.keyboard.gameactions.*;
import net.ck.mtbg.util.communication.time.GameTimeChangeType;
import net.ck.mtbg.util.communication.time.GameTimeChanged;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NPCTest
{
    private ArrayList<Map> originalMaps;
    private Map originalCurrentMap;
    private Point originalPlayerPosition;
    private GameTime originalGameTime;

    @BeforeAll
    static void setUpBeforeClass()
    {
        RunGame.startGame(false);
    }

    @AfterAll
    static void tearDownAfterClass()
    {
        Game.getCurrent().setRunning(false);
    }

    private static Map createMap(String name, int width, int height)
    {
        Map map = new Map();
        map.setName(name);
        map.setSize(new Point(width, height));
        map.mapTiles = new MapTile[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                MapTile tile = new MapTile(x, y);
                tile.setMapPosition(new Point(x, y));
                tile.setType(TileTypes.GRASS);
                tile.setBlocked(false);
                map.mapTiles[x][y] = tile;
            }
        }
        return map;
    }

    private static void placeLifeForm(Map map, LifeForm lifeForm, int x, int y)
    {
        // Nur das ALTE Feld auf DERSELBEN Karte räumen (Bounds-sicher)
        Point oldPos = lifeForm.getMapPosition();
        if (oldPos != null
                && oldPos.x >= 0 && oldPos.y >= 0
                && oldPos.x < map.getSize().x && oldPos.y < map.getSize().y)
        {
            MapTile previousTile = map.mapTiles[oldPos.x][oldPos.y];
            if (previousTile.getLifeForm() == lifeForm)
            {
                previousTile.setLifeForm(null);
                previousTile.setBlocked(false);
            }
        }
        lifeForm.setMapPosition(new Point(x, y));
        map.mapTiles[x][y].setLifeForm(lifeForm);
        map.mapTiles[x][y].setBlocked(true);
        if (!map.getLifeForms().contains(lifeForm))
        {
            map.getLifeForms().add(lifeForm);
        }
    }

    private static Weapon createWeapon(int id, String name, WeaponTypes type)
    {
        Weapon weapon = new Weapon();
        weapon.setId(id);
        weapon.setName(name);
        weapon.setType(type);
        return weapon;
    }

    @BeforeEach
    void captureGameState()
    {
        originalMaps = Game.getCurrent().getMaps();
        originalCurrentMap = Game.getCurrent().getCurrentMap();
        originalPlayerPosition = new Point(Game.getCurrent().getCurrentPlayer().getMapPosition());
        originalGameTime = Game.getCurrent().getGameTime();
    }

    @AfterEach
    void restoreGameState()
    {
        if (originalMaps != null)
        {
            Game.getCurrent().setMaps(originalMaps);
        }
        if (originalCurrentMap != null)
        {
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
        if (originalPlayerPosition != null)
        {
            Game.getCurrent().getCurrentPlayer().setMapPosition(new Point(originalPlayerPosition));
        }
        if (originalGameTime != null)
        {
            Game.getCurrent().setGameTime(originalGameTime);
        }
    }

    @Test
    void defaultConstructorSetsExpectedNpcDefaults()
    {
        NPC npc = new NPC();

        assertAll(
                () -> assertNotNull(npc.getQueuedActions()),
                () -> assertEquals(LifeFormState.ALIVE, npc.getState()),
                () -> assertEquals(GameConfiguration.baseHealth + (npc.getLevel() * 10), npc.getHealth()),
                () -> assertFalse(npc.isPlayer()),
                () -> assertNotNull(npc.getWeapon()),
                () -> assertNotNull(npc.getMobasks())
        );
    }

    @Test
    void initializeSetsOriginalMapPositionWhenMissing()
    {
        NPC npc = new NPC();
        npc.setType(NPCType.WARRIOR);
        npc.setMapPosition(new Point(2, 3));
        npc.setOriginalMapPosition(null);

        npc.initialize();

        assertEquals(new Point(2, 3), npc.getOriginalMapPosition());
    }

    @Test
    void initializeCanBeCalledMultipleTimesWithoutEventBusRegistrationError()
    {
        NPC npc = new NPC();
        npc.setType(NPCType.WARRIOR);
        npc.setMapPosition(new Point(1, 1));

        assertDoesNotThrow(npc::initialize,
                "Erster initialize()-Aufruf darf keine EventBus-Exception werfen");
        assertDoesNotThrow(npc::initialize,
                "Zweiter initialize()-Aufruf darf keine doppelte EventBus-Registrierung auslösen");
    }

    @Test
    void pointConstructorSetsOriginalAndCurrentMapPosition()
    {
        NPC npc = new NPC(5, new Point(4, 1));

        assertEquals(new Point(4, 1), npc.getMapPosition());
        assertEquals(new Point(4, 1), npc.getOriginalMapPosition());
    }

    @Test
    void wieldWeaponRequiresWeaponInInventoryAndEmptyHand()
    {
        NPC npc = new NPC();
        Weapon sword = createWeapon(101, "test-sword", WeaponTypes.MELEE);
        Weapon bow = createWeapon(102, "test-bow", WeaponTypes.RANGED);
        npc.setWeapon(null);
        npc.getInventory().add(sword);
        npc.getInventory().add(bow);

        assertTrue(npc.wieldWeapon(sword));
        assertSame(sword, npc.getWeapon());
        assertFalse(npc.getInventory().contains(sword));
        assertFalse(npc.wieldWeapon(bow));
    }

    @Test
    void setOriginalTargetMapPositionStoresPoint()
    {
        NPC npc = new NPC();
        Point target = new Point(7, 8);

        npc.setOriginalTargetMapPosition(target);

        assertEquals(target, npc.getOriginalTargetMapPosition());
    }

    @Test
    void onMessageEventActivatesEligibleScheduleActivity()
    {
        NPC npc = new NPC();
        Schedule schedule = new Schedule(npc);
        schedule.setActive(true);

        ScheduleActivity activity = new ScheduleActivity();
        GameTime startTime = new GameTime();
        startTime.setCurrentHour(0);
        startTime.setCurrentMinute(0);
        activity.setStartTime(startTime);
        activity.setActive(false);
        schedule.add(activity);
        npc.setSchedule(schedule);

        Game.getCurrent().setGameTime(new GameTime());
        npc.onMessageEvent(new GameTimeChanged(GameTimeChangeType.MINUTE));

        assertTrue(activity.isActive());
    }

    @Test
    void lookTogglesFurnitureBurningState()
    {
        Map map = createMap("look-map", 4, 4);
        useMaps(map);

        NPC npc = createNpc(11, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        FurnitureItem torch = new FurnitureItem();
        torch.setBurning(false);
        MapTile tile = map.mapTiles[1][2];
        tile.setType(TileTypes.GRASS);
        tile.setFurniture(torch);

        npc.look(tile);
        assertTrue(torch.isBurning());

        npc.look(tile);
        assertFalse(torch.isBurning());
    }

    @Test
    void lookMovesNpcWhenLookingAtSignpost()
    {
        Map map = createMap("signpost-map", 4, 4);
        useMaps(map);

        NPC npc = createNpc(12, new Point(0, 0));
        placeLifeForm(map, npc, 0, 0);

        MapTile signpost = map.mapTiles[1][0];
        signpost.setType(TileTypes.SIGNPOST);
        signpost.setTargetCoordinates(new Point(2, 2));

        npc.look(signpost);

        assertEquals(new Point(2, 2), npc.getMapPosition());
        assertNull(map.mapTiles[0][0].getLifeForm());
        assertSame(npc, map.mapTiles[2][2].getLifeForm());
    }

    @Test
    void sayAndSearchDoNotThrow()
    {
        NPC npc = new NPC();

        assertAll(
                () -> assertDoesNotThrow(() -> npc.say("Hallo")),
                () -> assertDoesNotThrow(npc::search)
        );
    }

    @Test
    void toXmlIncludesTargetPositionWhenPresent()
    {
        NPC npc = createNpc(13, new Point(1, 2));
        npc.setTargetMapPosition(new Point(3, 4));

        String xml = npc.toXML();

        assertAll(
                () -> assertTrue(xml.contains("<id>13</id>")),
                () -> assertTrue(xml.contains("<mapPosition>1,2</mapPosition>")),
                () -> assertTrue(xml.contains("<targetPosition>3,4</targetPosition>"))
        );
    }

    @Test
    void doActionMovesNpcEastAndMarksSuccess()
    {
        Map map = createMap("action-map", 5, 5);
        useMaps(map);

        NPC npc = createNpc(14, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        NPCAction action = new NPCAction(new EastAction());
        npc.doAction(action);

        assertAll(
                () -> assertTrue(action.isSuccess()),
                () -> assertEquals(new Point(2, 1), npc.getMapPosition()),
                () -> assertNull(map.mapTiles[1][1].getLifeForm()),
                () -> assertSame(npc, map.mapTiles[2][1].getLifeForm())
        );
    }

    @Test
    void doActionSupportsGetAndDrop()
    {
        Map map = createMap("inventory-map", 5, 5);
        useMaps(map);

        NPC npc = createNpc(15, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);
        Weapon item = createWeapon(201, "loot-blade", WeaponTypes.MELEE);
        MapTile tile = map.mapTiles[1][2];
        tile.getInventory().add(item);

        NPCAction getAction = new NPCAction(new GetAction(new Point(1, 2)));
        npc.doAction(getAction);
        assertTrue(getAction.isSuccess());
        assertTrue(npc.getInventory().contains(item));
        assertFalse(tile.getInventory().contains(item));

        DropAction dropAction = new DropAction();
        dropAction.setGetWhere(new Point(1, 2));
        dropAction.setAffectedItem(item);
        NPCAction action = new NPCAction(dropAction);
        npc.doAction(action);

        assertAll(
                () -> assertTrue(action.isSuccess()),
                () -> assertFalse(npc.getInventory().contains(item)),
                () -> assertTrue(tile.getInventory().contains(item))
        );
    }

    @Test
    void doActionLookDelegatesToLookBehaviour()
    {
        Map map = createMap("look-action-map", 4, 4);
        useMaps(map);

        NPC npc = createNpc(16, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        FurnitureItem furniture = new FurnitureItem();
        furniture.setBurning(false);
        MapTile tile = map.mapTiles[1][2];
        tile.setType(TileTypes.GRASS);
        tile.setFurniture(furniture);

        LookAction lookAction = new LookAction();
        lookAction.setGetWhere(new Point(1, 2));
        NPCAction action = new NPCAction(lookAction);
        npc.doAction(action);

        assertFalse(action.isSuccess());
        assertTrue(furniture.isBurning());
    }

    @Test
    void isRangedChecksWeaponAndInventory()
    {
        NPC npc = new NPC();
        Weapon melee = createWeapon(301, "club", WeaponTypes.MELEE);
        Weapon ranged = createWeapon(302, "bow", WeaponTypes.RANGED);
        npc.getInventory().getInventory().clear();
        npc.setWeapon(melee);
        npc.getInventory().add(ranged);

        assertTrue(npc.isRanged());

        npc.getInventory().remove(ranged);
        assertFalse(npc.isRanged());

        npc.setWeapon(ranged);
        assertTrue(npc.isRanged());
    }

    @Test
    void switchWeaponEquipsRequestedWeaponType()
    {
        NPC npc = new NPC();
        Weapon melee = createWeapon(401, "axe", WeaponTypes.MELEE);
        Weapon ranged = createWeapon(402, "crossbow", WeaponTypes.RANGED);
        npc.setWeapon(null);
        npc.getInventory().getInventory().clear();
        npc.getInventory().add(melee);
        npc.getInventory().add(ranged);

        npc.switchWeapon(WeaponTypes.RANGED);
        assertSame(ranged, npc.getWeapon());
        assertTrue(npc.getInventory().contains(melee));

        npc.switchWeapon(WeaponTypes.MELEE);
        assertSame(melee, npc.getWeapon());
        assertTrue(npc.getInventory().contains(ranged));
    }

    @Test
    void moveToMovesNpcOneStepAlongCalculatedPath()
    {
        Map map = createMap("path-map", 6, 6);
        useMaps(map);

        NPC npc = createNpc(17, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        boolean result = npc.moveTo(map.mapTiles[3][1]);

        assertFalse(result);
        assertEquals(new Point(2, 1), npc.getMapPosition());
    }

    // ─── Edge Cases ──────────────────────────────────────────────────────────────

    @Test
    void npcSwitchMapMovesNpcWithoutChangingCurrentMap()
    {
        Map sourceMap = createMap("source", 4, 4);
        Map targetMap = createMap("target", 4, 4);
        useMaps(sourceMap, targetMap);

        NPC npc = createNpc(18, new Point(1, 1));
        placeLifeForm(sourceMap, npc, 1, 1);
        sourceMap.mapTiles[1][1].setTargetMap("target");
        sourceMap.mapTiles[1][1].setTargetCoordinates(new Point(2, 2));

        boolean switched = npc.switchMap();

        assertAll(
                () -> assertTrue(switched),
                () -> assertSame(sourceMap, Game.getCurrent().getCurrentMap()),
                () -> assertEquals(new Point(2, 2), npc.getMapPosition()),
                () -> assertFalse(sourceMap.getLifeForms().contains(npc)),
                () -> assertTrue(targetMap.getLifeForms().contains(npc)),
                () -> assertNull(sourceMap.mapTiles[1][1].getLifeForm()),
                () -> assertSame(npc, targetMap.mapTiles[2][2].getLifeForm())
        );
    }

    @Test
    void lookForExitReturnsEnterActionOnlyOnExitTiles()
    {
        Map map = createMap("exit-map", 4, 4);
        useMaps(map);

        NPC npc = createNpc(19, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        assertNull(npc.lookForExit());

        map.mapTiles[1][1].setTargetCoordinates(new Point(2, 2));
        assertInstanceOf(EnterAction.class, npc.lookForExit());
    }

    @Test
    void getPropertiesExposesLevelAndPatrolling()
    {
        NPC npc = new NPC();
        npc.setLevel(3);
        npc.setPatrolling(true);

        List<NPCProperty> properties = new ArrayList<>(npc.getProperties());

        assertAll(
                () -> assertEquals(2, properties.size()),
                () -> assertTrue(properties.stream().anyMatch(p -> "level".equals(p.name()) && Integer.valueOf(3).equals(p.value()))),
                () -> assertTrue(properties.stream().anyMatch(p -> "patrolling".equals(p.name()) && Boolean.TRUE.equals(p.value())))
        );
    }

    @Test
    void doActionSpaceSetsSuccessToTrue()
    {
        Map map = createMap("space-map", 4, 4);
        useMaps(map);
        NPC npc = createNpc(30, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        NPCAction action = new NPCAction(new SpaceAction());
        npc.doAction(action);

        assertTrue(action.isSuccess(), "SPACE-Action sollte success=true setzen");
    }

    @Test
    void doActionAttackReducesVictimHealthWhenHit()
    {
        Map map = createMap("attack-map", 5, 5);
        useMaps(map);

        NPC npc = createNpc(31, new Point(1, 0));
        placeLifeForm(map, npc, 1, 0);
        npc.initialize();
        // Dex=50 vs Spieler-Dex=20 → Treffchance=170% → garantierter Treffer
        npc.getAttributes().get(AttributeTypes.DEXTERITY).setValue(50);

        int healthBefore = Game.getCurrent().getCurrentPlayer().getHealth();

        AttackAction attackAction = new AttackAction();
        attackAction.setGetWhere(new Point(0, 0));          // Spieler liegt auf (0,0)
        NPCAction action = new NPCAction(attackAction);
        npc.doAction(action);

        int healthAfter = Game.getCurrent().getCurrentPlayer().getHealth();
        assertTrue(action.isSuccess(), "ATTACK-Action bei Treffer sollte success=true sein");
        assertEquals(healthBefore - 5, healthAfter, "Spieler-Health sollte um 5 gesunken sein");
    }

    @Test
    void doActionEnterOnExitTileMovesNpcToTargetMap()
    {
        Map source = createMap("enter-source", 4, 4);
        Map target = createMap("enter-target", 4, 4);
        useMaps(source, target);

        NPC npc = createNpc(32, new Point(1, 1));
        placeLifeForm(source, npc, 1, 1);
        source.mapTiles[1][1].setTargetMap("enter-target");
        source.mapTiles[1][1].setTargetCoordinates(new Point(2, 2));

        NPCAction action = new NPCAction(new EnterAction());
        npc.doAction(action);

        assertAll(
                () -> assertEquals(new Point(2, 2), npc.getMapPosition(), "NPC sollte auf Zielkarte bei (2,2) sein"),
                () -> assertFalse(source.getLifeForms().contains(npc), "NPC sollte Quellkarte verlassen haben"),
                () -> assertTrue(target.getLifeForms().contains(npc), "NPC sollte auf Zielkarte sein"),
                () -> assertSame(source, Game.getCurrent().getCurrentMap(), "Aktuelle Karte darf nicht wechseln")
        );
    }

    @Test
    void doActionTalkSuccessRemainsFalse()
    {
        Map map = createMap("talk-map", 4, 4);
        useMaps(map);
        NPC npc = createNpc(33, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        NPCAction action = new NPCAction(new TalkAction());
        npc.doAction(action);

        assertFalse(action.isSuccess(), "TALK-Action setzt success nie auf true");
    }

    @Test
    void doActionSearchRunsWithoutThrowing()
    {
        Map map = createMap("search-action-map", 4, 4);
        useMaps(map);
        NPC npc = createNpc(34, new Point(1, 1));
        placeLifeForm(map, npc, 1, 1);

        NPCAction action = new NPCAction(new SearchAction());
        assertDoesNotThrow(() -> npc.doAction(action), "SEARCH-Action sollte keine Exception werfen");
    }

    @Test
    void doActionBlockedTileNpcStaysInPlace()
    {
        Map map = createMap("blocked-map", 5, 5);
        useMaps(map);
        NPC npc = createNpc(35, new Point(2, 2));
        placeLifeForm(map, npc, 2, 2);
        map.mapTiles[3][2].setBlocked(true);  // Ost-Kachel blockiert

        NPCAction action = new NPCAction(new EastAction());
        npc.doAction(action);

        assertFalse(action.isSuccess(), "Bewegung auf blockierte Kachel sollte fehlschlagen");
        assertEquals(new Point(2, 2), npc.getMapPosition(), "NPC sollte auf blockierter Kachel stehen bleiben");
        assertSame(npc, map.mapTiles[2][2].getLifeForm(), "NPC sollte noch auf Ausgangskachel stehen");
    }

    @Test
    void doActionOpenableDoorOpensDoorAndMovesNpc()
    {
        Map map = createMap("openable-door-map", 5, 5);
        useMaps(map);
        NPC npc = createNpc(39, new Point(2, 2));
        placeLifeForm(map, npc, 2, 2);

        MapTile eastTile = map.mapTiles[3][2];
        eastTile.setType(TileTypes.WOODDOORCLOSED);

        NPCAction action = new NPCAction(new EastAction());
        npc.doAction(action);

        assertAll(
                () -> assertTrue(action.isSuccess(), "Bewegung durch geschlossene, öffnbare Tür sollte erfolgreich sein"),
                () -> assertEquals(TileTypes.WOODDOOROPEN, eastTile.getType(), "Tür sollte geöffnet worden sein"),
                () -> assertEquals(new Point(3, 2), npc.getMapPosition(), "NPC sollte durch die geöffnete Tür ziehen"),
                () -> assertNull(map.mapTiles[2][2].getLifeForm(), "Startkachel sollte nach Bewegung leer sein"),
                () -> assertSame(npc, eastTile.getLifeForm(), "Zielkachel sollte den NPC enthalten")
        );
    }

    @Test
    void doActionNorthBorderNpcStaysInPlace()
    {
        Map map = createMap("north-border-map", 5, 5);
        useMaps(map);
        NPC npc = createNpc(36, new Point(2, 0));
        placeLifeForm(map, npc, 2, 0);

        NPCAction action = new NPCAction(new NorthAction());
        npc.doAction(action);

        assertFalse(action.isSuccess(), "Bewegung über Norden-Rand sollte fehlschlagen");
        assertEquals(new Point(2, 0), npc.getMapPosition(), "NPC am Nordrand sollte nicht bewegen");
    }

    @Test
    void doActionWestBorderNpcStaysInPlace()
    {
        Map map = createMap("west-border-map", 5, 5);
        useMaps(map);
        NPC npc = createNpc(37, new Point(0, 2));
        placeLifeForm(map, npc, 0, 2);

        NPCAction action = new NPCAction(new WestAction());
        npc.doAction(action);

        assertFalse(action.isSuccess(), "Bewegung über Westen-Rand sollte fehlschlagen");
        assertEquals(new Point(0, 2), npc.getMapPosition(), "NPC am Westrand sollte nicht bewegen");
    }

    @Test
    void doActionIgnoredWhenNpcIsDead()
    {
        Map map = createMap("dead-npc-map", 5, 5);
        useMaps(map);
        NPC npc = createNpc(38, new Point(2, 2));
        placeLifeForm(map, npc, 2, 2);
        npc.setState(LifeFormState.DEAD);

        NPCAction action = new NPCAction(new EastAction());
        npc.doAction(action);

        assertAll(
                () -> assertFalse(action.isSuccess(), "Toter NPC sollte keine erfolgreiche Action melden"),
                () -> assertEquals(new Point(2, 2), npc.getMapPosition(), "Toter NPC sollte sich nicht bewegen"),
                () -> assertSame(npc, map.mapTiles[2][2].getLifeForm(), "Kachel-Lifeform sollte unverändert sein")
        );
    }

    @Test
    void wieldWeaponReturnsFalseWhenWeaponNotInInventory()
    {
        NPC npc = new NPC();
        npc.setWeapon(null);
        npc.getInventory().getInventory().clear();
        Weapon sword = createWeapon(501, "not-in-inventory-sword", WeaponTypes.MELEE);
        // sword absichtlich nicht ins Inventar gelegt

        boolean result = npc.wieldWeapon(sword);

        assertFalse(result, "wieldWeapon ohne Waffe im Inventar sollte false zurückgeben");
        assertNull(npc.getWeapon(), "Waffenhand sollte leer bleiben");
    }

    @Test
    void isRangedReturnsFalseWithNoWeaponAndEmptyInventory()
    {
        NPC npc = new NPC();
        npc.setWeapon(null);
        npc.getInventory().getInventory().clear();

        assertFalse(npc.isRanged(), "isRanged sollte false sein wenn kein Fernkampf-Inventar");
    }

    @Test
    void copyConstructorCopiesKeyFields()
    {
        NPC original = new NPC();
        original.setId(50);
        original.setLevel(3);
        original.setType(NPCType.WARRIOR);
        original.setMapPosition(new Point(3, 4));
        original.setOriginalMapPosition(new Point(3, 4));
        original.setPatrolling(true);
        original.setHostile(true);
        original.getMobasks().put("hallo", "Guten Tag!");

        NPC copy = new NPC(original);

        assertAll(
                () -> assertEquals(original.getId(), copy.getId(), "ID sollte kopiert werden"),
                () -> assertEquals(original.getLevel(), copy.getLevel(), "Level sollte kopiert werden"),
                () -> assertEquals(original.getType(), copy.getType(), "Typ sollte kopiert werden"),
                () -> assertTrue(copy.isPatrolling(), "Patrolling sollte kopiert werden"),
                () -> assertTrue(copy.isHostile(), "Hostile sollte kopiert werden"),
                () -> assertTrue(copy.getMobasks().containsKey("hallo"), "Mobasks sollten kopiert werden")
        );
    }

    @Test
    void scheduleActivityNotActivatedBeforeStartTime()
    {
        NPC npc = new NPC();
        Schedule schedule = new Schedule(npc);
        schedule.setActive(true);

        ScheduleActivity activity = new ScheduleActivity();
        GameTime startTime = new GameTime();
        startTime.setCurrentHour(23);
        startTime.setCurrentMinute(59);
        activity.setStartTime(startTime);
        activity.setActive(false);
        schedule.add(activity);
        npc.setSchedule(schedule);

        GameTime earlyTime = new GameTime();
        earlyTime.setCurrentHour(0);
        earlyTime.setCurrentMinute(0);
        Game.getCurrent().setGameTime(earlyTime);
        npc.onMessageEvent(new GameTimeChanged(GameTimeChangeType.MINUTE));

        assertFalse(activity.isActive(), "Aktivität sollte NICHT vor ihrer Startzeit aktiv werden");
    }

    @Test
    void scheduleActivityIgnoredWhenScheduleInactive()
    {
        NPC npc = new NPC();
        Schedule schedule = new Schedule(npc);
        schedule.setActive(false);   // Schedule deaktiviert

        ScheduleActivity activity = new ScheduleActivity();
        GameTime startTime = new GameTime();
        startTime.setCurrentHour(0);
        startTime.setCurrentMinute(0);
        activity.setStartTime(startTime);
        activity.setActive(false);
        schedule.add(activity);
        npc.setSchedule(schedule);

        Game.getCurrent().setGameTime(new GameTime());
        npc.onMessageEvent(new GameTimeChanged(GameTimeChangeType.MINUTE));

        assertFalse(activity.isActive(), "Aktivität bei inaktivem Schedule sollte nicht starten");
    }

    @Test
    void decreaseHealthSetsUnconsciousAtExactZero()
    {
        NPC npc = new NPC();
        npc.setHealth(10);

        npc.decreaseHealth(10);   // 10 - 10 = 0

        assertEquals(LifeFormState.UNCONSCIOUS, npc.getState(), "Health=0 sollte UNCONSCIOUS setzen");
        assertEquals(0, npc.getHealth());
        assertFalse(npc.isHostile(), "Bewusstloser NPC sollte nicht hostile sein");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    @Test
    void decreaseHealthSetsDead()
    {
        NPC npc = new NPC();
        npc.setHealth(10);

        npc.decreaseHealth(15);   // 10 - 15 = -5 < 0

        assertEquals(LifeFormState.DEAD, npc.getState(), "Health<0 sollte DEAD setzen");
        assertEquals(-1, npc.getHealth(), "Health bei Tod sollte auf -1 gesetzt werden");
        assertFalse(npc.isHostile(), "Toter NPC sollte nicht hostile sein");
    }

    @Test
    void switchWeaponToUnavailableTypeKeepsWeaponNull()
    {
        NPC npc = new NPC();
        npc.setWeapon(null);
        npc.getInventory().getInventory().clear();
        Weapon melee = createWeapon(601, "npc-sword", WeaponTypes.MELEE);
        npc.getInventory().add(melee);

        npc.switchWeapon(WeaponTypes.RANGED);  // kein Fernkampf im Inventar

        assertNull(npc.getWeapon(), "Waffe sollte null sein wenn gewünschter Typ nicht verfügbar");
        assertTrue(npc.getInventory().contains(melee), "Vorhandene Waffe sollte im Inventar bleiben");
    }

    @Test
    void getPropertiesAlwaysContainsBothEntriesRegardlessOfPatrolling()
    {
        NPC npc = new NPC();
        npc.setLevel(2);
        npc.setPatrolling(false);

        List<NPCProperty> properties = new ArrayList<>(npc.getProperties());

        assertAll(
                () -> assertEquals(2, properties.size(), "getProperties sollte immer 2 Einträge liefern"),
                () -> assertTrue(properties.stream().anyMatch(p -> "level".equals(p.name()) && Integer.valueOf(2).equals(p.value()))),
                () -> assertTrue(properties.stream().anyMatch(p -> "patrolling".equals(p.name()) && Boolean.FALSE.equals(p.value())))
        );
    }

    private NPC createNpc(int id, Point position)
    {
        NPC npc = new NPC();
        npc.setId(id);
        npc.setType(NPCType.WARRIOR);
        npc.setMapPosition(new Point(position));
        npc.setOriginalMapPosition(new Point(position));
        return npc;
    }

    private void useMaps(Map... maps)
    {
        ArrayList<Map> gameMaps = new ArrayList<>(List.of(maps));
        Game.getCurrent().setMaps(gameMaps);
        Game.getCurrent().setCurrentMap(maps[0]);
        placeLifeForm(maps[0], Game.getCurrent().getCurrentPlayer(), 0, 0);
    }
}

