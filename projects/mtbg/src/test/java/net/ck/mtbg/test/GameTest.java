package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.ai.AIBehaviour;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.backend.time.IdleTimer;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.TileTypes;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.ui.highlighting.HighlightTimer;
import net.ck.mtbg.util.communication.keyboard.gameactions.*;
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
        System.setProperty("mtbg.testMode", "true");
        RunGame.startGame(false);
        initializeTestTimers();

        Game.getCurrent().getCurrentMap().getLifeForms().clear();
        logger.info("GameTest: setupBeforeClass end");
    }

    private static void initializeTestTimers()
    {
        if (TimerManager.getIdleTimer() == null)
        {
            TimerManager.setIdleTimer(new IdleTimer(1, e ->
            {
            }));
        }
        if (TimerManager.getHighlightTimer() == null)
        {
            TimerManager.setHighlightTimer(new HighlightTimer(1, e ->
            {
            }));
        }
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
        initializeTestTimers();
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
            Game.getCurrent().advanceTurn(playerAction);
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
                Game.getCurrent().advanceTurn(action);
                logger.info("Player position: {}", Game.getCurrent().getCurrentPlayer().getMapPosition());
                logger.info("Turn number: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
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
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-east", 10, 10);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            NPC n1 = new NPC();
            n1.setId(99);
            n1.setType(NPCType.WARRIOR);
            Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
            n1.setMapPosition(new Point(3, 2));
            Game.getCurrent().getCurrentMap().mapTiles[3][2].setLifeForm(n1);
            Game.getCurrent().getCurrentMap().mapTiles[3][2].setBlocked(true);
            Game.getCurrent().getCurrentMap().mapTiles[4][2].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[4][2].setBlocked(false);
            n1.initialize();
            logger.info("npc position: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new EastAction());
            Game.getCurrent().advanceTurn(action);
            logger.info("npc position 2: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new EastAction());
            Game.getCurrent().advanceTurn(action);
            logger.info("npc position 3: {}", n1.getMapPosition());
            logger.info("now test wanderer east");
            var wanderAction = AIBehaviour.wanderAround(n1, 1);
            assertNotNull(wanderAction, "wanderAround sollte fuer gueltige Richtung nicht null liefern");
            n1.doAction(wanderAction);
            logger.info("npc position 4: {}", n1.getMapPosition());
            assertEquals(4, n1.getMapPosition().x, "NPC sollte nach Ost-Bewegung bei x=4 sein");
        }
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void testWandererWEST()
    {
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-west", 10, 10);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            NPC n1 = new NPC();
            n1.setId(90);
            n1.setType(NPCType.WARRIOR);
            Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
            n1.setMapPosition(new Point(4, 2));
            Game.getCurrent().getCurrentMap().mapTiles[4][2].setLifeForm(n1);
            Game.getCurrent().getCurrentMap().mapTiles[4][2].setBlocked(true);
            Game.getCurrent().getCurrentMap().mapTiles[3][2].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[3][2].setBlocked(false);
            Game.getCurrent().getCurrentMap().mapTiles[2][2].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[2][2].setBlocked(false);
            n1.initialize();
            logger.info("npc position 1: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new WestAction());
            Game.getCurrent().advanceTurn(action);

            logger.info("npc position 2: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new WestAction());
            Game.getCurrent().advanceTurn(action);

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
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void testWandererNORTH()
    {
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-north", 10, 10);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            NPC n1 = new NPC();
            n1.setId(90);
            n1.setType(NPCType.WARRIOR);
            Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
            n1.setMapPosition(new Point(5, 3));
            Game.getCurrent().getCurrentMap().mapTiles[5][3].setLifeForm(n1);
            Game.getCurrent().getCurrentMap().mapTiles[5][3].setBlocked(true);
            Game.getCurrent().getCurrentMap().mapTiles[5][2].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[5][2].setBlocked(false);
            Game.getCurrent().getCurrentMap().mapTiles[5][1].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[5][1].setBlocked(false);
            n1.initialize();
            logger.info("npc position 1: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new NorthAction());
            Game.getCurrent().advanceTurn(action);
            logger.info("npc position 2: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new NorthAction());
            Game.getCurrent().advanceTurn(action);
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
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void testWandererSOUTH()
    {
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-south", 10, 10);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            NPC n1 = new NPC();
            n1.setId(90);
            n1.setType(NPCType.WARRIOR);
            Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
            n1.setMapPosition(new Point(4, 3));
            Game.getCurrent().getCurrentMap().mapTiles[4][3].setLifeForm(n1);
            Game.getCurrent().getCurrentMap().mapTiles[4][3].setBlocked(true);
            Game.getCurrent().getCurrentMap().mapTiles[4][4].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[4][4].setBlocked(false);
            Game.getCurrent().getCurrentMap().mapTiles[4][5].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[4][5].setBlocked(false);
            n1.initialize();
            logger.info("npc position 1: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new SouthAction());
            Game.getCurrent().advanceTurn(action);
            logger.info("npc position 2: {}", n1.getMapPosition());
            n1.getQueuedActions().addEntry(new SouthAction());
            Game.getCurrent().advanceTurn(action);
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
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void testActionFrameWork()
    {
        ArrayList<Map> originalMaps = Game.getCurrent().getMaps();
        Map originalCurrentMap = Game.getCurrent().getCurrentMap();
        Map testMap = createOpenMap("game-test-action", 10, 10);
        try
        {
            Game.getCurrent().setMaps(new ArrayList<>(List.of(testMap)));
            Game.getCurrent().setCurrentMap(testMap);
            Game.getCurrent().addPlayers(new Point(0, 0));

            NPC n1 = new NPC();
            n1.setId(98);
            n1.setType(NPCType.WARRIOR);
            Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
            n1.setMapPosition(new Point(5, 2));
            Game.getCurrent().getCurrentMap().mapTiles[5][2].setLifeForm(n1);
            Game.getCurrent().getCurrentMap().mapTiles[5][2].setBlocked(true);
            Game.getCurrent().getCurrentMap().mapTiles[6][2].setLifeForm(null);
            Game.getCurrent().getCurrentMap().mapTiles[6][2].setBlocked(false);
            n1.initialize();
            logger.info("npc position before: {}", n1.getMapPosition());
            EastAction eastAction = new EastAction();
            n1.getQueuedActions().addEntry(eastAction);
            SpaceAction spaceAction = new SpaceAction();
            PlayerAction action = new PlayerAction(spaceAction);
            action.setHaveNPCAction(true);
            Game.getCurrent().advanceTurn(action);
            logger.info("npc position after: {}", n1.getMapPosition());
            assertEquals(6, n1.getMapPosition().x, "NPC sollte nach Ost-Aktion bei x=6 sein");
            assertEquals(2, n1.getMapPosition().y, "NPC y sollte nach Ost-Aktion bei 2 bleiben");
        }
        finally
        {
            Game.getCurrent().setMaps(originalMaps);
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }
}
