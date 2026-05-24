package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.ai.AIBehaviour;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.communication.graphics.AdvanceTurnEvent;
import net.ck.mtbg.util.communication.keyboard.gameactions.*;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
public class GameTest
{

    @BeforeAll
    public static void setUpBeforeClass()
    {
        logger.info("GameTest: setupBeforeClass begin");
        RunGame.startGame(false);

        Game.getCurrent().getCurrentMap().getLifeForms().clear();
        logger.info("GameTest: setupBeforeClass end");
    }

    @AfterAll
    public static void tearDownAfterClass()
    {
        logger.info("GameTest - shutting down everything hopefully");
        Game.getCurrent().setRunning(false);
    }

    private static Map createOpenMap(String name, int width, int height)
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
                tile.setLifeForm(null);
                tile.setBlocked(false);
                map.mapTiles[x][y] = tile;
            }
        }
        return map;
    }

    @BeforeEach
    public void setUp()
    {
        //Game.getCurrent().addPlayers(null);
    }

    @AfterEach
    public void tearDown()
    {
        logger.debug("clean up lifeforms");
        for (var row : Game.getCurrent().getCurrentMap().mapTiles)
        {
            for (var tile : row)
            {
                if (tile.getLifeForm() != null)
                {
                    tile.setLifeForm(null);
                    tile.setBlocked(false);
                }
            }
        }
        Game.getCurrent().getCurrentMap().getLifeForms().clear();
    }

    @Test
    public void testMainLoopTenTimes()
    {
        logger.info("testMainLoopTenTimes start");
        SpaceAction action = new SpaceAction();

        PlayerAction playerAction = new PlayerAction(action);
        action.setHaveNPCAction(true);
        playerAction.setHaveNPCAction(true);

        for (int i = 0; i < 10; i++)
        {
            EventBus.getDefault().post(new AdvanceTurnEvent(playerAction));
        }
        logger.info("testMainLoopTenTimes end");
    }

    @Test
    public void movePlayer()
    {
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-move", 15, 5);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            for (int i = 0; i < 10; i++)
            {
                PlayerAction action = new PlayerAction(new EastAction());
                action.setHaveNPCAction(true);
                EventBus.getDefault().post(new AdvanceTurnEvent(action));
                logger.info("Player position: {}", Game.getCurrent().getCurrentPlayer().getMapPosition());
            }

            assertEquals(10, Game.getCurrent().getCurrentPlayer().getMapPosition().x, "Spieler sollte x=10 erreicht haben");
            assertEquals(0, Game.getCurrent().getCurrentPlayer().getMapPosition().y, "Spieler y-Position sollte 0 bleiben");
        }
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void testWandererEAST()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);

        NPC n1 = new NPC();
        n1.setId(99);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(3, 2));
        n1.initialize();
        logger.info("npc position: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new EastAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new EastAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        logger.info("now test wanderer east");
        var wanderAction = AIBehaviour.wanderAround(n1, 1);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 4: {}", n1.getMapPosition());
        assertEquals(4, n1.getMapPosition().x, "NPC sollte nach Ost-Bewegung bei x=4 sein");
    }

    @Test
    public void testWandererWEST()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 2));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new WestAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);

        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new WestAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);

        logger.info("npc position 3: {}", n1.getMapPosition());
        var wanderAction = AIBehaviour.wanderAround(n1, 3);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 4: {}", n1.getMapPosition());
        assertEquals(3, n1.getMapPosition().x, "NPC sollte nach West-Bewegung bei x=3 sein");

        wanderAction = AIBehaviour.wanderAround(n1, 3);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 5: {}", n1.getMapPosition());
        assertEquals(2, n1.getMapPosition().x, "NPC sollte beim zweiten WEST-Schritt auf x=2 laufen");
    }

    @Test
    public void testWandererNORTH()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(5, 3));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new NorthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new NorthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        var wanderAction = AIBehaviour.wanderAround(n1, 0);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 4: {}", n1.getMapPosition());
        assertEquals(5, n1.getMapPosition().x, "NPC x sollte nach Nord-Bewegung bei 5 bleiben");
        wanderAction = AIBehaviour.wanderAround(n1, 0);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 5: {}", n1.getMapPosition());
        assertEquals(1, n1.getMapPosition().y, "NPC y sollte nach Nord-Bewegung bei 1 sein");
    }

    @Test
    public void testWandererSOUTH()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 3));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new SouthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new SouthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        var wanderAction = AIBehaviour.wanderAround(n1, 2);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 4: {}", n1.getMapPosition());
        assertEquals(4, n1.getMapPosition().x, "NPC x sollte nach Süd-Bewegung bei 4 bleiben");
        wanderAction = AIBehaviour.wanderAround(n1, 2);
        assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
        n1.doAction(wanderAction);
        logger.info("npc position 5: {}", n1.getMapPosition());
        assertEquals(5, n1.getMapPosition().y, "NPC y sollte nach Süd-Bewegung bei 5 sein");
    }

    @Test
    public void testActionFrameWork()
    {
        NPC n1 = new NPC();
        n1.setId(98);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(5, 2));
        Game.getCurrent().getCurrentMap().mapTiles[5][2].setLifeForm(n1);
        Game.getCurrent().getCurrentMap().mapTiles[5][2].setBlocked(true);
        // Zielkachel explizit freimachen, um Restzustand aus vorherigen Tests auszuschliessen.
        Game.getCurrent().getCurrentMap().mapTiles[6][2].setLifeForm(null);
        Game.getCurrent().getCurrentMap().mapTiles[6][2].setBlocked(false);
        n1.initialize();
        logger.info("npc position before: {}", n1.getMapPosition());
        EastAction eastAction = new EastAction();
        n1.getQueuedActions().addEntry(eastAction);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(1000, ThreadNames.MAIN);
        logger.info("npc position after: {}", n1.getMapPosition());
        assertEquals(6, n1.getMapPosition().x, "NPC sollte nach Ost-Aktion bei x=6 sein");
        assertEquals(2, n1.getMapPosition().y, "NPC y sollte nach Ost-Aktion bei 2 bleiben");
    }
}
