package net.ck.game.test;

import net.ck.game.backend.game.Game;
import net.ck.game.map.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

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

}
