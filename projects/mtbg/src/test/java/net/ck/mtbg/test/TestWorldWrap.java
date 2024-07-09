package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.run.RunGame;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

@Log4j2
public class TestWorldWrap
{


    @BeforeAll
    public static void setUpBeforeClass()
    {

        System.out.println("TestWorldWrap: setupBeforeClass begin");
        RunGame.startGame(false);
        System.out.println("TestWorldWrap: setupBeforeClass end");
    }

    @AfterAll
    public static void tearDownAfterClass()
    {
        logger.debug("TestWorldWrap shutting down everything hopefully");
        logger.debug("TestWorldWrap finished shutting down");
    }

    @BeforeEach
    public void setUp()
    {
        logger.debug("setup test");
    }

    @AfterEach
    public void tearDown()
    {
        logger.debug("teardown test");
    }
}
