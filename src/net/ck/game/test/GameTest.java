package net.ck.game.test;

import net.ck.game.backend.entities.AIBehaviour;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.entities.NPCTypes;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.util.MapUtils;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.keyboard.EastAction;
import net.ck.util.communication.keyboard.NorthAction;
import net.ck.util.communication.keyboard.SouthAction;
import net.ck.util.communication.keyboard.WestAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.junit.*;

import java.awt.*;

public class GameTest
{

	static private Game game;
	private static final Logger logger = (Logger) LogManager.getLogger(GameTest.class);



	@BeforeClass
	public static void setUpBeforeClass()
	{
		logger.info("GameTest: setupBeforeClass begin");
		TestGameSetup.SetupGameForTest();
		game = TestGameSetup.getGame();
		game.getCurrentMap().getLifeForms().clear();
		logger.info("GameTest: setupBeforeClass end");
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		logger.info("GameTest - shutting down everything hopefully");
		game.stopGame();
	}

	@Before
	public void setUp()
	{

	}

	@After
	public void tearDown()
	{

	}

	/**
	 * @Test public void testGameMapDefinition() { logger.error("test game map
	 *       definition start"); // 1 - 2 - 3 // | | | // 4 - 5 - 6 MapTile tile =
	 *       MapUtil.getMapTileByID(gameMap, 1);
	 * 
	 *       assertEquals(MapUtil.getIDOfMapTileEast(tile), 2);
	 *       assertEquals(MapUtil.getIDOfMapTileSouth(tile), 4); logger.error("test
	 *       game map definition end"); }
	 **/
	/**
	 * @Test public void testGameMapSize() { logger.error("test game map start");
	 *       assertEquals(game.getGameMap().getTiles().size(), 6);
	 *       logger.error("test game map end"); }
	 **/
	@Test
	public void testMainLoopTenTimes()
	{
		logger.info("testMainLoopTenTimes start");
			for (int i = 0; i < 10; i++)
			{
				EventBus.getDefault().post(new AdvanceTurnEvent(true));
				game.getThreadController().sleep(100, ThreadNames.MAIN);
			}
		logger.info("testMainLoopTenTimes end");
	}

	/**
	 * @Test public void testRetract() { logger.error("test Retract start");
	 *       game.runTurn(); game.runTurn(); game.runTurn(); int turnNumberBefore =
	 *       game.getTurnNumber(); game.retractTurn(); int turnNumberAfter =
	 *       game.getTurnNumber(); assertTrue(turnNumberBefore > turnNumberAfter);
	 *       assertEquals(turnNumberBefore, turnNumberAfter + 1); logger.error("test
	 *       Retract end"); }
	 **/
	/**
	 * @Test public void testTurnCounterIncrease() { logger.error("turn counter
	 * increase test start"); for (int i = 0; i < 10; i++) { game.runTurn();
	 * assertEquals(game.getTurnNumber(), i + 1); } logger.error("turn counter
	 * increase test finish"); }
	 **/
	@Test
	public void movePlayer()
	{
		game.getCurrentPlayer().setMapPosition(new Point(0, 0));
		game.getCurrentPlayer().moveTo(game.getCurrentMap().mapTiles[10][0]);
		for (int i = 0; i < 10; i++)
		{
			EventBus.getDefault().post(new AdvanceTurnEvent(true));
			logger.info("Player position: {}", game.getCurrentPlayer().getMapPosition());
			for (Thread t : game.getThreadController().getThreads())
			{
				game.getThreadController().sleep(100, ThreadNames.MAIN);
			}
		}
		assert (game.getCurrentPlayer().getMapPosition().x == 10);
		assert (game.getCurrentPlayer().getMapPosition().y == 0);
	}

	@Test
	public void testWandererEAST()
	{
		NPC n1 = new NPC();
		n1.setId(99);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(3, 2));
		n1.initialize();
		logger.info("npc position: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new EastAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 2: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new EastAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 3: {}", n1.getMapPosition());
		logger.info("now test wanderer east");
		n1.doAction(AIBehaviour.wanderAround(n1, 1));
		logger.info("npc position 4: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 4);
		Game.getCurrent().getCurrentMap().getLifeForms().clear();
	}


	@Test
	public void testWandererWEST()
	{
		NPC n1 = new NPC();
		n1.setId(90);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(4, 2));
		n1.initialize();
		logger.info("npc position 1: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new WestAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 2: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new WestAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 3: {}", n1.getMapPosition());
		n1.doAction(AIBehaviour.wanderAround(n1, 3));
		logger.info("npc position 4: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 3);
		n1.doAction(AIBehaviour.wanderAround(n1, 3));
		logger.info("npc position 5: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 3);
		Game.getCurrent().getCurrentMap().getLifeForms().clear();
	}

	@Test
	public void testWandererNORTH()
	{
		NPC n1 = new NPC();
		n1.setId(90);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(4, 3));
		n1.initialize();
		logger.info("npc position 1: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new NorthAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 2: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new NorthAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 3: {}", n1.getMapPosition());
		n1.doAction(AIBehaviour.wanderAround(n1, 0));
		logger.info("npc position 4: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 4);
		n1.doAction(AIBehaviour.wanderAround(n1, 0));
		logger.info("npc position 5: {}", n1.getMapPosition());
		assert (n1.getMapPosition().y == 1);
		Game.getCurrent().getCurrentMap().getLifeForms().clear();
	}

	@Test
	public void testWandererSOUTH()
	{
		NPC n1 = new NPC();
		n1.setId(90);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(4, 3));
		n1.initialize();
		logger.info("npc position 1: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new SouthAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 2: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new SouthAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);
		logger.info("npc position 3: {}", n1.getMapPosition());
		n1.doAction(AIBehaviour.wanderAround(n1, 2));
		logger.info("npc position 4: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 4);
		n1.doAction(AIBehaviour.wanderAround(n1, 2));
		logger.info("npc position 5: {}", n1.getMapPosition());
		assert (n1.getMapPosition().y == 5);
		Game.getCurrent().getCurrentMap().getLifeForms().clear();
	}

	@Test
	public void testActionFrameWork()
	{
		NPC n1 = new NPC();
		n1.setId(98);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(2, 2));
		n1.initialize();
		logger.info("npc position before: {}", n1.getMapPosition());
		n1.getQueuedActions().addEntry(new EastAction());
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		game.getThreadController().sleep(100, ThreadNames.MAIN);

		logger.info("npc position afer: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 3);
		assert (n1.getMapPosition().y == 2);
	}

	@Test
	public void testMelee()
	{
		game.addPlayers(null);
		for (int i = 0; i < game.getCurrentPlayer().getInventory().getSize(); i++)
		{
			//logger.info("inventory: {}", game.getCurrentPlayer().getInventory().get(i));
		}

		//game.getCurrentPlayer().equipItem();
		NPC n1 = new NPC();
		n1.setId(98);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(3, 2));
		n1.initialize();
		n1.attack(MapUtils.getMapTileByCoordinatesAsPoint(game.getCurrentPlayer().getMapPosition()));
	}
}
