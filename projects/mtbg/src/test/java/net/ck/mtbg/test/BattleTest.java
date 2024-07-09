package net.ck.mtbg.test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.utils.MapUtils;
import org.junit.jupiter.api.*;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class BattleTest
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
    public void testNPCMeleeAttackingPlayer()
    {
        NPC n1 = new NPC();
        n1.setId(98);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(3, 2));
        n1.initialize();
        n1.attack(MapUtils.getMapTileByCoordinatesAsPoint(Game.getCurrent().getCurrentPlayer().getMapPosition()));
    }


    @Test
    public void testNPCRangeAttackingPlayer()
    {
        NPC n1 = new NPC();
        n1.setId(94);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 2));
        n1.initialize();
        n1.wieldWeapon(ItemManager.getWeaponList().get(3));
        n1.attack(MapUtils.getMapTileByCoordinatesAsPoint(Game.getCurrent().getCurrentPlayer().getMapPosition()));
    }


    @Test
    public void testPlayerRangeAttackingNPC()
    {
        NPC n1 = new NPC();
        n1.setId(95);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(4, 2));
        Game.getCurrent().getCurrentMap().mapTiles[4][2].setLifeForm(n1);
        n1.initialize();
        n1.wieldWeapon(ItemManager.getWeaponList().get(3));
        Game.getCurrent().getCurrentPlayer().attack(MapUtils.getMapTileByCoordinatesAsPoint(n1.getMapPosition()));
    }


    @Test
    public void testPlayerMeleeAttackingNPC()
    {
        NPC n1 = new NPC();
        n1.setId(93);
        n1.setType(NPCType.WARRIOR);
        Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
        n1.setMapPosition(new Point(3, 2));
        Game.getCurrent().getCurrentMap().mapTiles[3][2].setLifeForm(n1);
        n1.initialize();
        n1.wieldWeapon(ItemManager.getWeaponList().get(3));
        Game.getCurrent().getCurrentPlayer().attack(MapUtils.getMapTileByCoordinatesAsPoint(n1.getMapPosition()));
    }
}
