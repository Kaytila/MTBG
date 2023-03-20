package net.ck.mtbg.test;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.*;

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

	@BeforeAll
    public static void setUpBeforeClass() {

        System.out.println("TestWorldWrap: setupBeforeClass begin");
        TestGameSetup.SetupGameForTest();
        game = TestGameSetup.getGame();
        gameMap = game.getCurrentMap();
        System.out.println("TestWorldWrap: setupBeforeClass end");
    }

    @AfterAll
    public static void tearDownAfterClass() {
        System.out.println("TestWorldWrap shutting down everything hopefully");
        System.out.println("TestWorldWrap finished shutting down");
    }

    @BeforeEach
    public void setUp() {
        logger.error("setup test");
    }

    @AfterEach
    public void tearDown() {
        logger.error("teardown test");

    }

}
