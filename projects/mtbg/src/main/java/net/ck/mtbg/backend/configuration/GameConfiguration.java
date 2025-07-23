package net.ck.mtbg.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.File;

/**
 * game configuration class - currently only using final public - but as this will need to go into a
 * configuration application somewhen somehow. lets see.
 * Good enough for now, makes things easier to read.
 */
@Log4j2
@ToString
public class GameConfiguration
{
    /**
     * are there animations?
     */
    public static final boolean animated = true;

    /**
     * delay for the background animation
     */
    public static final int animationBackGroundDelay = 1000;
    /**
     * how many animation cycles are there?
     */
    public static final int animationCycles = 7;

    /**
     * delay for the fore ground animation
     */
    public static final int animationForeGroundDelay = 1500;

    /**
     * delay for the life form animation
     */
    public static final int animationLifeformDelay = 2000;

    /**
     * base health for NPCs, multiplied with levels
     */
    public static final int baseHealth = 10;
    /**
     * brighten up images or not - used for drawing bright, less bright and almost dark tiles
     */
    public static final boolean brightenUpImages = false;
    /**
     * calculate the brightened up image during drawing or before
     */
    public static final boolean calculateBrightenUpImageInPaint = true;
    /**
     * tile size for the character editor
     */
    public static final int characterEditorTileSize = 20;

    /**
     * tile size for the tiny character editor
     */
    public static final int characterEditorTinyTileSize = 10;


    /*
    ============================================================
    CUTSCENE
     */

    public static final int cutSceneImageRollOverBuffer = 1000;
    public static final int cutSceneImageRolloverDelay = 5000;
    public static final int cutSceneImageRolloverPeriod = 5000;

    /*
    ========================================================================================
    all debug flags
     */
    public static final boolean debugASTAR = false;
    public static final boolean debugAutoMap = false;
    public static final boolean debugBrightenImages = false;
    public static final boolean debugDiscovered = false;
    public static final boolean debugEnvironmentalStoryTeller = false;
    public static final boolean debugEvents = false;
    public static final boolean debugGameState = false;
    public static final boolean debugLOS = false;
    public static final boolean debugMap = false;
    public static final boolean debugMapParser = false;
    public static final boolean debugMapPosition = false;
    public static final boolean debugNPC = false;
    public static final boolean debugPC = false;
    public static final boolean debugPaint = false;
    public static final boolean debugSchedule = false;
    /**
     * debug the splash startup
     */
    public static final boolean debugSplash = false;
    /**
     * debug the startup procedure
     */
    public static final boolean debugStartUp = false;
    public static final boolean debugTime = false;
    public static final boolean debugTimers = false;
    public static final boolean debugTurn = false;
    public static final boolean debugWorld = false;
    /*
    ========================================================================================
     */
    /**
     * describes the dex value where player or npc will move twice
     */
    public static final int dexterityThreshold = 30;
    /**
     * standard dialog height
     */
    public static final int dialogHeight = 300;
    /**
     * standard dialog width
     */
    public static final int dialogWidth = 300;
    /**
     * draw the furniture/inventory on the automap or not?
     */
    public static final boolean drawFurnitureOnAutoMap = true;
    /**
     * draw the labels as string on the automap
     */
    public static final boolean drawLabelsOnAutomap = false;
    /**
     * only draw each tile of the visible area once,
     * do not draw over again for background, foreground, npc, furniture, blocked, light
     */
    public static final boolean drawTileOnce = true;
    /**
     * how big is the scrollbar used for the map canvas in map editor? to be used later
     */
    public static final int editorScrollbarSize = 0;
    /**
     * this is used for drawing the circles in the equipment dialog
     */
    public static final int elipseSize = 25;
    /**
     * what font are we using?
     */
    public static final Font font = new Font("Helvetica Neue", Font.PLAIN, 20);
    /**
     * frames per second, if i really want to switch to this in the future
     */
    public static final int fps = 60;
    /**
     * delay for the highlighting
     */
    public static final int highlightDelay = 200;
    /**
     * hit or miss timeer duration
     */
    public static final int hitormissTimerDuration = 1000;
    /**
     * how big are the images, check this somewhere.
     */
    public static final Point imageSize = new Point(16, 16);
    /**
     * start location for the map editor - needs to be updated based on installation
     */
    public static final String mapEditorLocation = "C:\\Users\\Claus\\eclipse-workspace\\MyTurnBasedGame\\projects\\mtbg\\assets\\maps";
    /**
     * describes the max distance a lightsource may have an effect on the surroundings
     */
    public static final int maxLightSourceDistance = 4;
    /**
     * delay for missile timer to tick in ms
     */
    public static final int missileWait = 5;
    /**
     * max visibility range around player during night
     */
    public static final int nightVisibility = 2;
    /**
     * how many additional images are there per lifeform
     */
    public static final int numberOfAdditionalImages = 4;
    /**
     * show how many rows and columns of tiles in the UI
     */
    public static final int numberOfTiles = 15;
    /**
     * max visibility around player during dawn
     */
    public static final int dawnVisibility = GameConfiguration.numberOfTiles / 4;
    /**
     * max visibility around player during dusk
     */
    public static final int duskVisibility = GameConfiguration.numberOfTiles / 4;
    /**
     * general path to the assets
     */
    public static final String out = "assets";
    /**
     * path to the misc images
     */
    public static final String miscImages = out + File.separator + "graphics" + File.separator + "misc" + File.separator;
    /**
     * path to the cursors
     */
    public static final String cursorPath = miscImages + "CURSORS" + File.separator;
    /**
     * general root path to the graphics
     */
    public static final String imagesRootPath = out + File.separator + "graphics" + File.separator;
    /**
     * where are the lifeforms
     */
    public static final String npcImages = out + File.separator + "graphics" + File.separator + "lifeforms" + File.separator;
    /**
     * where is the music
     */
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
    /**
     * what is the padding
     */
    public static final int padding = 5;
    /**
     * what is the line height?
     */
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
    /**
     * where are the player images
     */
    public static final String playerImages = out + File.separator + "graphics" + File.separator + "players" + File.separator + "player" + File.separator;
    /**
     * size of the normal buttons - this is important as they are depending on an underlying PNG to draw text on
     */
    public static final Dimension preferredButtonSize = new Dimension(70, 30);
    /**
     * how big are the buttons on the title screen?
     */
    public static final Dimension preferredTitleButtonSize = new Dimension(150, 30);
    /**
     * how long to wait until to pick the next command queued up
     */
    public static final int quequeWait = 700;
    /**
     * ignore the startup screen
     */
    public static final boolean quickstart = true;
    /**
     * where are the images for the skills
     */
    public static final String skillMenuImages = out + File.separator + "graphics" + File.separator + "skills" + File.separator;
    /**
     * where are the skills xml file
     */
    public static final String skillsFileRootPath = out + File.separator + "skills";
    /**
     * when drawing the missile, skip how many pixels per redraw:
     * less means slower draw, faster means skipping.
     */
    public static final int skippedPixelsForDrawingMissiles = 5;
    /**
     * where are the sound effects
     */
    public static final String soundeffectsPath = out + File.separator + "soundeffects";
    /**
     * where are the images for the spells menu
     */
    public static final String spellMenuImages = out + File.separator + "graphics" + File.separator + "spells" + File.separator;
    /**
     * where to find the spells xml
     */
    public static final String spellsFileRootPath = out + File.separator + "spells";
    /**
     * which map to start on
     */
    public static final String startMap = "outpost";
    /**
     * what is the start position on the start map for a new game?
     */
    public static final Point startPosition = new Point(3, 1);
    /**
     * whats the start time for a new game?
     */
    public static final Point startTime = new Point(9, 0);
    /**
     * how many nanoseconds are in a second?
     */
    public static final int targetTime = 1000000000 / fps;
    /**
     * Tile Size
     */
    public static final int tileSize = 32;
    /**
     * how high is the whole UI
     */
    public static final int UIheight = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);
    /**
     * how big shall the tiles be on the automap?
     */
    public static final int autoMapTileSize = GameConfiguration.tileSize / 2;
    /**
     * how wide is the whole UI
     */
    public static final int UIwidth = ((GameConfiguration.numberOfTiles * GameConfiguration.tileSize) + 200);
    /**
     * Title Screen String
     */
    public static final String titleString = "HELLO, this is my DEMO";
    /**
     * in case there are text maps available, shall these be translated?
     */
    public static final boolean translateTextMaps = false;
    /**
     * how long shall the game wait until sending a pass message in ms
     */
    public static final long turnwait = 5000;
    /**
     * where are the maps that are designed as TXT
     */
    public static final String txtMapRootFilePath = out + File.separator + "maps_txt" + File.separator;
    /**
     * use Events only for redrawing or do 60 fps :D
     */
    public static final boolean useEvents = false;
    /**
     * use a thread for Game or not - if not, where does it run?
     */
    public static final boolean useGameThread = true;
    /**
     * use an util timer or a thread
     */
    public static final boolean useTimerForMissiles = true;
    /**
     * use swing timers for background, foreground and lifeform animation
     */
    public static final boolean useTimersForAnimations = true;
    /**
     * util timer or swing timer or thread?
     */
    public static final boolean useUtilTimerForAnimation = true;
    /**
     * vertical spacer used on the title screen
     */
    public static final int verticalTitleSpacer = 20;

    /**
     * how many turns to wait until music switches back to before from victory fanfare music?
     */
    public static final int waitTurns = 3;

    /**
     * path to weather images
     */
    public static final String weatherImagesPath = out + File.separator + "graphics" + File.separator + "weather" + File.separator;

    /**
     * path to weather type images
     */
    public static final String weatherTypesImagesPath = out + File.separator + "graphics" + File.separator + "weathertypes" + File.separator;
    /**
     * how many milliseconds to wait in case weather is async?
     */
    public static final int weatherWait = 10000;

    /**
     * how many miliseconds to wait until music switches back to before from victory fanfare music?
     */
    public static int victoryWait = 7000;

    /**
     * what is the title track to be played?
     */
    public static String titleTrack = "";// = out + File.separator + "music" + File.separator + "STONES" + File.separator + "stones5.wav";

    /**
     * does the music in game loop?
     */
    @Getter
    @Setter
    public static boolean loopMusic = true;

    /**
     * does the music in the title screen loop?
     */
    @Getter
    @Setter
    public static boolean loopTitleMusic = true;
}


