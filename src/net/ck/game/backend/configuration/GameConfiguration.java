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
    public static final long turnwait = 5000;

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
    public static final boolean playSound = false;

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
    public static final Point imageSize = new Point(16, 16);

    /**
     * how many turns to wait until music switches back to before from victory fanfare music?
     */
    public static final int waitTurns = 3;

    /**
     * how many miliseconds to wait until music switches back to before from victory fanfare music?
     */
    public static final int victoryWait = 7000;

    /**
     * how long to wait until to pick the next command queued up
     */
    public static final int quequeWait = 700;

    /**
     * delay for missile timer to tick in ms
     */
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

    /**
     * use Events only for redrawing or do 60 fps :D
     */
    public static final boolean useEvents = true;

    /**
     * only draw each tile of the visible area once,
     * do not draw over again for background, foreground, npc, furniture, blocked, light
     */
    public static final boolean drawTileOnce = true;

    public static final boolean brightenUpImages = true;

    public static final boolean paintGridLines = true;


    public static final boolean debugASTAR = false;

    public static final boolean debugLOS = false;

    public static final boolean calculateBrightenUpImageInPaint = true;

    public static final int skippedPixelsForDrawingMissiles = 5;
}


