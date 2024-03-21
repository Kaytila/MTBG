package net.ck.mtbg.backend.configuration;

import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.File;

/**
 * game configuration class - currently only using final public - but as this will need to go into a
 * configuration application somewhen somehow. lets see.
 * Good enough for now, makes things easier to read.
 */
@Log4j2
public class GameConfiguration
{
    /**
     * are there animations?
     */
    public static final boolean animated = true;
    public static final int animationBackGroundDelay = 1000;
    /**
     * how many animation cycles are there?
     */
    public static final int animationCycles = 7;
    public static final int animationForeGroundDelay = 500;
    public static final int animationLifeformDelay = 2000;
    /**
     * brighten up images or not - used for drawing bright, less bright and almost dark tiles
     */
    public static final boolean brightenUpImages = true;
    /**
     * calculate the brightened up image during drawing or before
     */
    public static final boolean calculateBrightenUpImageInPaint = true;
    public static final int characterEditorTileSize = 20;
    public static final int characterEditorTinyTileSize = 10;
    public static final int cutSceneImageRollOverBuffer = 1000;
    public static final int cutSceneImageRolloverDelay = 5000;
    public static final int cutSceneImageRolloverPeriod = 5000;
    public static final boolean debugASTAR = false;
    public static final boolean debugBrightenImages = false;
    public static final boolean debugLOS = false;
    public static final boolean debugPaint = false;
    /**
     * describes the dex value where player will move twice
     */
    public static final int dexterityThreshold = 30;
    public static final int dialogHeight = 300;
    public static final int dialogWidth = 300;
    /**
     * only draw each tile of the visible area once,
     * do not draw over again for background, foreground, npc, furniture, blocked, light
     */
    public static final boolean drawTileOnce = true;
    /**
     * this is used for drawing the circles in the equipment dialog
     */
    public static final int elipseSize = 25;
    public static final Font font = new Font("Helvetica Neue", Font.PLAIN, 20);
    /**
     * frames per second, if i really want to switch to this in the future
     */
    public static final int fps = 60;
    public static final int highlightDelay = 200;
    public static final int hitormissTimerDuration = 1000;
    /**
     * how big are the images, check this somewhere.
     */
    public static final Point imageSize = new Point(16, 16);
    /**
     * describes the max distance a lightsource may have an effect on the surroundings
     */
    public static final int lightSourceDistance = 2;
    /**
     * delay for missile timer to tick in ms
     */
    public static final int missileWait = 5;
    public static final int numberOfAdditionalImages = 4;
    /**
     * show how many rows and columns of tiles in the UI
     */
    public static final int numberOfTiles = 15;
    public static final String out = "assets";
    public static final String miscImages = out + File.separator + "graphics" + File.separator + "misc" + File.separator;
    public static final String cursorPath = miscImages + "CURSORS" + File.separator;
    public static final String imagesRootPath = out + File.separator + "graphics" + File.separator;
    public static final String npcImages = out + File.separator + "graphics" + File.separator + "lifeforms" + File.separator;
    public static final String musicPath = out + File.separator + "music";
    /**
     * where to find the maps
     */
    public static final String mapFileRootPath = out + File.separator + "maps";
    /**
     * where to find the npcs xml
     */
    public static final String npcFileRootPath = out + File.separator + "npcs";
    /**
     * where to find items xml
     */
    public static final String itemFileRootPath = out + File.separator + "items";
    public static final int padding = 5;
    public static final int lineHight = font.getSize() + padding;
    /**
     * paint grid lines or not
     */
    public static final boolean paintGridLines = true;
    /**
     * shall music be played?
     */
    public static final boolean playMusic = true;
    /**
     * do we want to play sound effects?
     */
    public static final boolean playSound = false;
    public static final String playerImages = out + File.separator + "graphics" + File.separator + "players" + File.separator + "player" + File.separator;
    /**
     * how long to wait until to pick the next command queued up
     */
    public static final int quequeWait = 700;
    public static final boolean quickstart = true;
    public static final String skillMenuImages = out + File.separator + "graphics" + File.separator + "skills" + File.separator;
    public static final String skillsFileRootPath = out + File.separator + "skills";
    /**
     * when drawing the missile, skip how many pixels per redraw:
     * less means slower draw, faster means skipping.
     */
    public static final int skippedPixelsForDrawingMissiles = 5;
    public static final String soundeffectsPath = out + File.separator + "soundeffects";
    public static final String spellMenuImages = out + File.separator + "graphics" + File.separator + "spells" + File.separator;
    /**
     * where to find the spells xml
     */
    public static final String spellsFileRootPath = out + File.separator + "spells";
    public static final String startMap = "outpost";
    public static final Point startPosition = new Point(2, 1);
    /**
     * how many nanoseconds are in a second?
     */
    public static final int targetTime = 1000000000 / fps;
    /**
     * Tile Size
     */
    public static final int tileSize = 32;
    public static final int UIwidth = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);
    public static final int UIheight = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);
    /**
     * how long shall the game wait until sending a pass message in ms
     */
    public static final long turnwait = 5000;
    public static final String txtMapRootFilePath = out + File.separator + "maps_txt" + File.separator;
    /**
     * use Events only for redrawing or do 60 fps :D
     */
    public static final boolean useEvents = false;
    /**
     * use a thread for Game or not - if not, where does it run?
     */
    public static final boolean useGameThread = true;
    public static final boolean useImageManager = true;
    /**
     * use an util timer or a thread
     */
    public static final boolean useTimerForMissiles = true;
    /**
     * use swing timers for background, foreground and lifeform animation
     */
    public static final boolean useTimersForAnimations = true;
    public static final boolean useUtilTimerForAnimation = true;
    /**
     * how many miliseconds to wait until music switches back to before from victory fanfare music?
     */
    public static final int victoryWait = 7000;
    /**
     * how many turns to wait until music switches back to before from victory fanfare music?
     */
    public static final int waitTurns = 3;
    public static final String weatherImagesPath = out + File.separator + "graphics" + File.separator + "weather" + File.separator;
    public static final String weatherTypesImagesPath = out + File.separator + "graphics" + File.separator + "weathertypes" + File.separator;
    /**
     * how many milliseconds to wait in case weather is async?
     */
    public static final int weatherWait = 10000;

}


