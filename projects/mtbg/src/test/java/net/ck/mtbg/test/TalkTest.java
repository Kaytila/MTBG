package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.run.RunGame;
import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@Log4j2
public class TalkTest
{

    @BeforeAll
    public static void setUpBeforeClass()
    {
        logger.info("GameTest: setupBeforeClass begin");
        System.setProperty("mtbg.testMode", "true");
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

    @BeforeEach
    public void setUp()
    {
        Game.getCurrent().addPlayers(null);
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
        String answer = Game.getCurrent().getCurrentPlayer().talk(n1, "hello");
        assertFalse(answer.isEmpty(), "Antwort auf bekanntes Stichwort sollte nicht leer sein");

        answer = Game.getCurrent().getCurrentPlayer().talk(n1, "fuck");
        assertNull(answer, "Unbekanntes Stichwort sollte null zurückgeben");
    }
}

