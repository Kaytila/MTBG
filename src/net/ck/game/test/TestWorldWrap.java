package net.ck.game.test;

import net.ck.game.backend.game.Game;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestWorldWrap
{

	static private Game game;
	private static Map gameMap;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	@BeforeClass
    public static void setUpBeforeClass() {

        System.out.println("TestWorldWrap: setupBeforeClass begin");
        TestGameSetup.SetupGameForTest();
        game = TestGameSetup.getGame();
        gameMap = game.getCurrentMap();
        System.out.println("TestWorldWrap: setupBeforeClass end");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("TestWorldWrap shutting down everything hopefully");
        System.out.println("TestWorldWrap finished shutting down");
    }

    @Before
    public void setUp() {
        logger.error("setup test");
    }

    @After
    public void tearDown() {
        logger.error("teardown test");

    }

	@Test
	public void testWrap1()
	{
		logger.error("running wrap test1: check if tile 3 to the east is not null");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 3);
		assertNotNull(tile);
		assertTrue(tile.getEast() != null);
	}

	@Test
	public void testWrap2()
	{
		logger.error("running wrap test2: check if tile 1 to the west is not null");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 1);
		assertNotNull(tile);
		assertTrue(tile.getWest() != null);
	}

	@Test
	public void testWrap3()
	{
		logger.error("running wrap test3: check if tile 1 to the west has tile 3");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 1);
		assertNotNull(tile);
		MapTile otherTile = tile.getWest();
		logger.error("otherTile: " + otherTile);
		logger.error(otherTile.getId());
		assertTrue(otherTile.getId() == 3);
	}

	@Test
	public void testWrap4()
	{
		logger.error("running wrap test4: check if tile 3 to the east has tile 1");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 3);
		assertNotNull(tile);
		MapTile otherTile = tile.getEast();
		logger.error("otherTile: " + otherTile);
		logger.error(otherTile.getId());
		assertTrue(otherTile.getId() == 1);
	}

	@Test
	public void testWrap5()
	{
		logger.error("running wrap test5: check if tile 4 to the west has tile 6");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 4);
		MapTile otherTile = tile.getWest();
		logger.error("otherTile: " + otherTile);
		logger.error(otherTile.getId());
		assertTrue(otherTile.getId() == 6);
	}

	@Test
	public void testWrap6()
	{
		logger.error("running wrap test6: check if tile 6 to the east has tile 4");
		MapTile tile = MapUtils.getMapTileByID(gameMap, 6);
		MapTile otherTile = tile.getEast();
		logger.error("otherTile: " + otherTile);
		logger.error(otherTile.getId());
		assertTrue(otherTile.getId() == 4);
	}
}
