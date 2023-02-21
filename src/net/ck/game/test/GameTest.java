package net.ck.game.test;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.AIBehaviour;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.entities.NPCTypes;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.keyboard.EastAction;
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
	public void testWanderer()
	{
		NPC n1 = new NPC();
		n1.setId(99);
		n1.setType(NPCTypes.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(2, 2));
		n1.initialize();
		logger.info("npc position: {}", n1.getMapPosition());
		n1.doAction(new PlayerAction(new EastAction()));
		n1.doAction(new PlayerAction(new EastAction()));

		logger.info("npc position: {}", n1.getMapPosition());
		logger.info("now test wanderer east");
		n1.doAction(AIBehaviour.initializeWanderer(n1, 1));
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		logger.info("npc position: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 3);
		n1.move(2, 2);
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		n1.move(1, 2);
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		n1.move(0, 2);
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		AIBehaviour.initializeWanderer(n1, 3);
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		logger.info("npc position: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 0);
		AIBehaviour.initializeWanderer(n1, 3);
		EventBus.getDefault().post(new AdvanceTurnEvent(true));
		logger.info("npc position: {}", n1.getMapPosition());
		assert (n1.getMapPosition().x == 0);
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
}
