package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.run.RunGame;
import org.junit.jupiter.api.*;

@Log4j2
public class SearchGetDropTest
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
    public void testSearch()
    {

    }

    @Test
    public void testGet()
    {
        Weapon club1 = ItemManager.getWeaponList().get(2);
        Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().add(club1);
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory() != null);
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().getSize() > 0);
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().get(0).equals(club1));
        Game.getCurrent().getCurrentPlayer().getItem(Game.getCurrent().getCurrentMap().mapTiles[4][2]);
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory() != null);
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().getSize() == 0);
        assert (Game.getCurrent().getCurrentPlayer().getInventory().contains(club1));
    }

    @Test
    public void testDrop()
    {
        AbstractItem item = Game.getCurrent().getCurrentPlayer().getInventory().get(0);
        Game.getCurrent().getCurrentPlayer().dropItem(item, Game.getCurrent().getCurrentMap().mapTiles[4][2]);
        assert (!(Game.getCurrent().getCurrentPlayer().getInventory().get(0).equals(item)));
        assert (Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().contains(item));
    }

}
