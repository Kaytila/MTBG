package net.ck.game.backend.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;

/**
 * game configuration class - currently only using final public - but as this will need to go into a
 * configuration application somewhen somehow. lets see.
 * Good enough for now, makes things easier to read.
 */
public class GameConfiguration
{
    private static final Logger logger = LogManager.getLogger(GameConfiguration.class);

    /**
     * how long shall the game wait until sending a pass message in ms
     */
    public static final long turnwait = 3000;

    /**
     * frames per second, if i really want to switch to this in the future
     */
    public static final int fps = 60;


    /**
     * how many nanoseconds are in a second?
     */
    public static final int targetTime = 1000000000 / fps;

    /**
     * show how many rows and columns of tiles in the UI
     */
    public static final int numberOfTiles = 15;

    /**
     * shall music be played?
     */
    public static final boolean playMusic = false;

    /**
     * do we want to play sound effects?
     */
    public static final boolean playSound = true;

    /**
     * Tile Size
     */
    public static final int tileSize = 32;

    /**
     * are there animations?
     */
    public static final boolean animated = true;

    /**
     * how many animation cycles are there?
     */
    public static final int animationCycles = 7;

    /**
     * how big are the images, check this somewhere.
     */
    public static final Point imageSize = new Point(16,16);

    /**
     * wait turns defines how many turns sound engine will wait until it switches music
     */
    public static final int waitTurns = 3;

    public static final int victoryWait = 7000;

    public static final int quequeWait = 700;

    public static final int missileWait = 5;

    public static final int UIwidth = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);

    public static final int UIheight = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);

    public static final int highlightDelay = 200;

    public static final int animationLifeformDelay = 2000;

    public static final int animationForeGroundDelay = 500;

    public static final int animationBackGroundDelay = 1000;

    public static final int dialogWidth = 300;

    public static final int dialogHeight = 300;

    /**
     * this is used for drawing the circles in the equipment dialog
     */
    public static final int elipseSize = 25;


    public static final Font font = new Font("Helvetica Neue", Font.PLAIN, 20);

    public static final int padding = 5;

    public static final int lineHight = font.getSize() + padding;

    public static final String miscImages = "graphics" + File.separator + "misc" + File.separator;
    public static final String cursorPath = miscImages + "CURSORS" + File.separator;

    public static final String weatherImagesPath = "graphics" + File.separator + "weather" + File.separator;

    public static final String weatherTypesImagesPath = "graphics" + File.separator + "weathertypes" + File.separator;

    public static final String imagesRootPath = "graphics" + File.separator;

    public static final String playerImages = "graphics" + File.separator + "players" + File.separator + "player" + File.separator;

    public static final String npcImages = "graphics" + File.separator + "npcs" + File.separator;

    public static final String mapFileRootPath = "maps";

    public static final String npcFileRootPath = "npcs";

    public static final String itemFileRootPath = "items";

    public static final boolean useEvents = true;
}
