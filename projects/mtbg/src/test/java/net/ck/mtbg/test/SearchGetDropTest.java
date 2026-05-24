package net.ck.mtbg.test;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.run.RunGame;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testSearch()
    {
        // Smoke-Test: Search auf der aktuellen Spieler-Kachel darf nicht werfen
        assertDoesNotThrow(() -> Game.getCurrent().getCurrentPlayer().search(),
                "search() sollte keine Exception werfen");
    }

    @Test
    public void testGet()
    {
        Weapon club1 = ItemManager.getWeaponList().get(2);
        Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().add(club1);

        assertNotNull(Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory(),
                "Tile-Inventar sollte nicht null sein");
        assertTrue(Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().getSize() > 0,
                "Tile-Inventar sollte Gegenstand enthalten");
        assertEquals(club1, Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().get(0),
                "Erster Gegenstand im Tile-Inventar sollte club1 sein");

        Game.getCurrent().getCurrentPlayer().getItem(Game.getCurrent().getCurrentMap().mapTiles[4][2]);

        assertNotNull(Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory(),
                "Tile-Inventar sollte nach Aufheben nicht null sein");
        assertEquals(0, Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().getSize(),
                "Tile-Inventar sollte nach Aufheben leer sein");
        assertTrue(Game.getCurrent().getCurrentPlayer().getInventory().contains(club1),
                "Spieler-Inventar sollte club1 nach Aufheben enthalten");
    }

    @Test
    public void testDrop()
    {
        AbstractItem item = Game.getCurrent().getCurrentPlayer().getInventory().get(0);
        Game.getCurrent().getCurrentPlayer().dropItem(item, Game.getCurrent().getCurrentMap().mapTiles[4][2]);

        assertFalse(Game.getCurrent().getCurrentPlayer().getInventory().contains(item),
                "Gegenstand sollte nach Drop nicht mehr im Spieler-Inventar sein");
        assertTrue(Game.getCurrent().getCurrentMap().mapTiles[4][2].getInventory().contains(item),
                "Gegenstand sollte nach Drop im Tile-Inventar liegen");
    }

}
