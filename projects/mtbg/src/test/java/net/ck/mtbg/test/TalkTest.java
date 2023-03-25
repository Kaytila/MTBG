package net.ck.mtbg.test;

import net.ck.mtbg.backend.entities.NPC;
import net.ck.mtbg.backend.entities.NPCType;
import net.ck.mtbg.backend.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.*;

import java.awt.*;

public class TalkTest
{

    private static final org.apache.logging.log4j.core.Logger logger = (Logger) LogManager.getLogger(GameTest.class);
    static private Game game;

    @BeforeAll
    public static void setUpBeforeClass()
    {
        logger.info("GameTest: setupBeforeClass begin");
        TestGameSetup.SetupGameForTest();
        game = TestGameSetup.getGame();
        game.getCurrentMap().getLifeForms().clear();
        logger.info("GameTest: setupBeforeClass end");
    }

    @AfterAll
    public static void tearDownAfterClass()
    {
        logger.info("GameTest - shutting down everything hopefully");
        game.stopGame();
    }

    @BeforeEach
    public void setUp()
    {
        game.addPlayers(null);
    }

    @AfterEach
    public void tearDown()
    {
        logger.debug("clean up lifeforms");
        Game.getCurrent().getCurrentMap().getLifeForms().clear();
    }

    @Test
    public void testTalk()
    {
        NPC n1 = new NPC();
        n1.setId(98);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(5, 2));
        n1.initialize();
        n1.getMobasks().put("hello", "Hello!");
        String answer = game.getCurrentPlayer().talk(n1, "hello");
        assert (!(answer.isEmpty()));

        answer = game.getCurrentPlayer().talk(n1, "fuck");
        assert (answer == null);
    }
}

