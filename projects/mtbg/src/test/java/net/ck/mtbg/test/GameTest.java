package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.entities.ai.AIBehaviour;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.communication.graphics.AdvanceTurnEvent;
import net.ck.mtbg.util.communication.keyboard.gameactions.*;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.*;

import java.awt.*;

@Log4j2
public class GameTest
{

    @BeforeAll
    public static void setUpBeforeClass()
    {
        logger.info("GameTest: setupBeforeClass begin");
        RunGame.startGame(false);

        Game.getCurrent().getCurrentMap().getLifeForms().clear();
        logger.info("GameTest: setupBeforeClass end");
    }

    @AfterAll
    public static void tearDownAfterClass()
    {
        logger.info("GameTest - shutting down everything hopefully");
        Game.getCurrent().stopGame();
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
    public void testMainLoopTenTimes()
    {
        logger.info("testMainLoopTenTimes start");
        SpaceAction action = new SpaceAction();

        PlayerAction playerAction = new PlayerAction(action);
        action.setHaveNPCAction(true);
        playerAction.setHaveNPCAction(true);

        for (int i = 0; i < 10; i++)
        {
            EventBus.getDefault().post(new AdvanceTurnEvent(playerAction));
            ThreadController.sleep(100, ThreadNames.MAIN);
        }
        logger.info("testMainLoopTenTimes end");
    }

    @Test
    public void movePlayer()
    {
        Game.getCurrent().getCurrentPlayer().setMapPosition(new Point(0, 0));
        Game.getCurrent().getCurrentPlayer().moveTo(Game.getCurrent().getCurrentMap().mapTiles[10][0]);

        AbstractKeyboardAction ac = (AbstractKeyboardAction) Game.getCurrent().getCurrentPlayer().getQueuedActions().poll();
        //SpaceAction ac = new SpaceAction();
        PlayerAction action = new PlayerAction(ac);
        action.setHaveNPCAction(true);

        for (int i = 0; i < 10; i++)
        {
            //game.getCurrentPlayer().doAction(action);
            EventBus.getDefault().post(new AdvanceTurnEvent(action));
            logger.info("Player position: {}", Game.getCurrent().getCurrentPlayer().getMapPosition());
            for (Thread t : ThreadController.getThreads())
            {
                ThreadController.sleep(100, ThreadNames.MAIN);
            }
        }
        assert (Game.getCurrent().getCurrentPlayer().getMapPosition().x == 10);
        assert (Game.getCurrent().getCurrentPlayer().getMapPosition().y == 0);
    }

    @Test
    public void testWandererEAST()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);

        NPC n1 = new NPC();
        n1.setId(99);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(3, 2));
        n1.initialize();
        logger.info("npc position: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new EastAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new EastAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        logger.info("now test wanderer east");
        n1.doAction(AIBehaviour.wanderAround(n1, 1));
        logger.info("npc position 4: {}", n1.getMapPosition());
        assert (n1.getMapPosition().x == 4);
    }


    @Test
    public void testWandererWEST()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 2));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new WestAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);

        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new WestAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);

        logger.info("npc position 3: {}", n1.getMapPosition());
        n1.doAction(AIBehaviour.wanderAround(n1, 3));
        logger.info("npc position 4: {}", n1.getMapPosition());

        assert (n1.getMapPosition().x == 3);
        n1.doAction(AIBehaviour.wanderAround(n1, 3));

        logger.info("npc position 5: {}", n1.getMapPosition());
        assert (n1.getMapPosition().x == 3);
    }

    @Test
    public void testWandererNORTH()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(5, 3));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new NorthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new NorthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        n1.doAction(AIBehaviour.wanderAround(n1, 0));
        logger.info("npc position 4: {}", n1.getMapPosition());
        assert (n1.getMapPosition().x == 5);
        n1.doAction(AIBehaviour.wanderAround(n1, 0));
        logger.info("npc position 5: {}", n1.getMapPosition());
        assert (n1.getMapPosition().y == 1);
    }

    @Test
    public void testWandererSOUTH()
    {
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        NPC n1 = new NPC();
        n1.setId(90);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 3));
        n1.initialize();
        logger.info("npc position 1: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new SouthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 2: {}", n1.getMapPosition());
        n1.getQueuedActions().addEntry(new SouthAction());
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(100, ThreadNames.MAIN);
        logger.info("npc position 3: {}", n1.getMapPosition());
        n1.doAction(AIBehaviour.wanderAround(n1, 2));
        logger.info("npc position 4: {}", n1.getMapPosition());
        assert (n1.getMapPosition().x == 4);
        n1.doAction(AIBehaviour.wanderAround(n1, 2));
        logger.info("npc position 5: {}", n1.getMapPosition());
        assert (n1.getMapPosition().y == 5);
    }

    @Test
    public void testActionFrameWork()
    {
        NPC n1 = new NPC();
        n1.setId(98);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(5, 2));
        n1.initialize();
        logger.info("npc position before: {}", n1.getMapPosition());
        EastAction eastAction = new EastAction();
        n1.getQueuedActions().addEntry(eastAction);
        SpaceAction spaceAction = new SpaceAction();
        PlayerAction action = new PlayerAction(spaceAction);
        action.setHaveNPCAction(true);
        EventBus.getDefault().post(new AdvanceTurnEvent(action));
        ThreadController.sleep(1000, ThreadNames.MAIN);
        logger.info("npc position afer: {}", n1.getMapPosition());
        assert (n1.getMapPosition().x == 6);
        assert (n1.getMapPosition().y == 2);
    }
}
