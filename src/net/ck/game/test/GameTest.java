package net.ck.game.test;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.AIBehaviour;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.entities.NPCTypes;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.threading.ThreadController;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.keyboard.*;
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
		game.addPlayers(null);
	}

	@After
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
		game.getCurrentPlayer().setMapPosition(new Point(0, 0));
		game.getCurrentPlayer().moveTo(game.getCurrentMap().mapTiles[10][0]);

		AbstractKeyboardAction ac = (AbstractKeyboardAction) game.getCurrentPlayer().getQueuedActions().poll();
		//SpaceAction ac = new SpaceAction();
		PlayerAction action = new PlayerAction(ac);
		action.setHaveNPCAction(true);

		for (int i = 0; i < 10; i++)
		{
			//game.getCurrentPlayer().doAction(action);
			EventBus.getDefault().post(new AdvanceTurnEvent(action));
			logger.info("Player position: {}", game.getCurrentPlayer().getMapPosition());
			for (Thread t : ThreadController.getThreads())
			{
				ThreadController.sleep(100, ThreadNames.MAIN);
			}
		}
		assert (game.getCurrentPlayer().getMapPosition().x == 10);
		assert (game.getCurrentPlayer().getMapPosition().y == 0);
	}

	@Test
	public void testWandererEAST()
	{
		SpaceAction spaceAction = new SpaceAction();
		PlayerAction action = new PlayerAction(spaceAction);
		action.setHaveNPCAction(true);

		NPC n1 = new NPC();
		n1.setId(99);
		n1.setType(NPCTypes.WARRIOR);
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
		n1.setType(NPCTypes.WARRIOR);
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
		n1.setType(NPCTypes.WARRIOR);
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
		n1.setType(NPCTypes.WARRIOR);
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
		n1.setType(NPCTypes.WARRIOR);
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
