package net.ck.game.test;

import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.ck.game.backend.Game;
import net.ck.game.map.GameMap;

public class GameTest
{

	static private Game game;
	private static final Logger logger = (Logger) LogManager.getLogger(GameTest.class);



	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		logger.info("GameTest: setupBeforeClass begin");
		TestGameSetup.SetupGameForTest();
		game = TestGameSetup.getGame();		
		logger.info("GameTest: setupBeforeClass end");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		logger.info("GameTest - shutting down everything hopefully");
		game = null;
		logger.info("GameTest - finished shutting down");
	}

	@Before
	public void setUp() throws Exception
	{

	}

	@After
	public void tearDown() throws Exception
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
		logger.error("testMainLoopTenTimes start");
		try
		{
			for (int i = 0; i < 10; i++)
			{
				game.advanceTurn(true);
				
				for (Thread t : game.getThreadController().getThreads())
				{
					if (t.getName().equalsIgnoreCase("main"))
					{
						t.sleep(1000);
					}
				}
			}
		} catch (Exception e)
		{
			fail("game loop does not work for ten turns!");
		}
		logger.error("testMainLoopTenTimes end");
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
	 *       increase test start"); for (int i = 0; i < 10; i++) { game.runTurn();
	 *       assertEquals(game.getTurnNumber(), i + 1); } logger.error("turn counter
	 *       increase test finish"); }
	 **/
}
