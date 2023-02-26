package net.ck.game.test;

import net.ck.game.backend.game.Game;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Weapon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.*;

public class SearchGetDropTest
{


    private static final Logger logger = (Logger) LogManager.getLogger(SearchGetDropTest.class);
    static private Game game;

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
    public void testSearch()
    {

    }

    @Test
    public void testGet()
    {
        Weapon club1 = Game.getCurrent().getWeaponList().get(2);
        game.getCurrentMap().mapTiles[4][2].getInventory().add(club1);
        assert (game.getCurrentMap().mapTiles[4][2].getInventory() != null);
        assert (game.getCurrentMap().mapTiles[4][2].getInventory().getSize() > 0);
        assert (game.getCurrentMap().mapTiles[4][2].getInventory().get(0).equals(club1));
        Game.getCurrent().getCurrentPlayer().getItem(game.getCurrentMap().mapTiles[4][2]);
        assert (game.getCurrentMap().mapTiles[4][2].getInventory() != null);
        assert (game.getCurrentMap().mapTiles[4][2].getInventory().getSize() == 0);
        assert (Game.getCurrent().getCurrentPlayer().getInventory().contains(club1));
    }

    @Test
    public void testDrop()
    {
        AbstractItem item = Game.getCurrent().getCurrentPlayer().getInventory().get(0);
        Game.getCurrent().getCurrentPlayer().dropItem(item, game.getCurrentMap().mapTiles[4][2]);
        assert (!(Game.getCurrent().getCurrentPlayer().getInventory().get(0).equals(item)));
        assert (game.getCurrentMap().mapTiles[4][2].getInventory().contains(item));
    }

}
