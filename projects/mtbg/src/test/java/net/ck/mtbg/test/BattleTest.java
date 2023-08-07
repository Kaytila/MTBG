package net.ck.mtbg.test;

import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.awt.*;

public class BattleTest
{
	private static final Logger logger = LogManager.getLogger(BattleTest.class);

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
	public void testNPCMeleeAttackingPlayer()
	{
		NPC n1 = new NPC();
		n1.setId(98);
		n1.setType(NPCType.WARRIOR);
		Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(3, 2));
		n1.initialize();
		n1.attack(MapUtils.getMapTileByCoordinatesAsPoint(game.getCurrentPlayer().getMapPosition()));
	}


	@Test
	public void testNPCRangeAttackingPlayer()
	{
		NPC n1 = new NPC();
		n1.setId(94);
		n1.setType(NPCType.WARRIOR);
		game.getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(4, 2));
		n1.initialize();
		n1.wieldWeapon(ItemManager.getWeaponList().get(3));
		n1.attack(MapUtils.getMapTileByCoordinatesAsPoint(game.getCurrentPlayer().getMapPosition()));
	}


	@Test
	public void testPlayerRangeAttackingNPC()
	{
		NPC n1 = new NPC();
		n1.setId(95);
		n1.setType(NPCType.WARRIOR);
		game.getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(4, 2));
		game.getCurrentMap().mapTiles[4][2].setLifeForm(n1);
		n1.initialize();
		n1.wieldWeapon(ItemManager.getWeaponList().get(3));
		game.getCurrentPlayer().attack(MapUtils.getMapTileByCoordinatesAsPoint(n1.getMapPosition()));
	}


	@Test
	public void testPlayerMeleeAttackingNPC()
	{
		NPC n1 = new NPC();
		n1.setId(93);
		n1.setType(NPCType.WARRIOR);
		game.getCurrentMap().getLifeForms().add(n1);
		n1.setMapPosition(new Point(3, 2));
		game.getCurrentMap().mapTiles[3][2].setLifeForm(n1);
		n1.initialize();
		n1.wieldWeapon(ItemManager.getWeaponList().get(3));
		game.getCurrentPlayer().attack(MapUtils.getMapTileByCoordinatesAsPoint(n1.getMapPosition()));
	}


}
