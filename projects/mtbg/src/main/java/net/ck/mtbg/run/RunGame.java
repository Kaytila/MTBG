package net.ck.mtbg.run;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.*;
import net.ck.mtbg.weather.WeatherTypes;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class RunGame
{
    final static SplashScreen splash = SplashScreen.getSplashScreen();
    /**
     * generate is used to create images or not. As these are already created, no need to do this
     */
    final static boolean generate = false;

    /**
     * MainWindow is actually the listener and action class, the main application window is something else entirely.
     * Not sure how this is supposed to work in Swing, will need to ask someone about it
     */
    @Getter
    @Setter


    /**
     * progress value, used in the progress bar
     */
    private static int progress = 0;

    /**
     * singleton by design, i am so good :P
     */
    private static Game game;

    //TODO
    //https://stackoverflow.com/questions/29290178/gui-has-to-wait-until-splashscreen-finishes-executing

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        RunGame.startGame(true);
    }

    /**
     * actual starting up of the game
     *
     * @param startTitle - indicates whether title screen shall be launched or not
     *                   keep it false for test setup
     */
    public static void startGame(boolean startTitle)
    {
        SplashScreen splash = SplashScreen.getSplashScreen();
        Graphics2D g = null;
        Dimension size = null;

        logger.debug("quickstart: {}", GameConfiguration.quickstart);
        logger.debug("splash: {}", splash);
        if (!GameConfiguration.quickstart && splash != null)
        {
            size = splash.getSize();
            g = splash.createGraphics();
        }


        if (GameConfiguration.debugStartUp == true)
        {
            logger.debug("starting game");
        }

        if (GameConfiguration.quickstart == true)
        {
            logger.info("quickstart enabled, no splashscreen");

        }
        if (GameConfiguration.debugStartUp == true)
        {
            logger.debug("initialize game");
        }
        game = Game.getCurrent();
        if (game != null)
        {
            if (generate)
            {
                ImageUtils.createWeatherTypesImages(WeatherTypes.RAIN);
                ImageUtils.createWeatherTypesImages(WeatherTypes.HAIL);
                ImageUtils.createWeatherTypesImages(WeatherTypes.SNOW);
            }


            java.util.List<Runnable> steps = java.util.List.of(
                    CursorUtils::initializeCursors,
                    GameUtils::initializeAllItems,
                    GameUtils::initializeSpells,
                    GameUtils::initializeSkills,
                    GameUtils::initializeNPCs,
                    GameUtils::initializeMaps,
                    GameUtils::setStartMap,
                    () -> game.addPlayers(GameConfiguration.startPosition),
                    GameUtils::initializeAnimationSystem,
                    GameUtils::initializeBackgroundAnimationSystem,
                    GameUtils::initializeForegroundAnimationSystem,
                    GameUtils::initializeWeatherSystem,
                    GameUtils::initializeIdleTimer,
                    GameUtils::initializeQuequeTimer,
                    GameUtils::initializeMissileThread,
                    GameUtils::initializeMusicTimer,
                    ImageUtils::initializeBackgroundImages,
                    ImageUtils::initializeForegroundImages,
                    () ->
                    {
                        game.startThreads();
                    },
                    GameUtils::initializeHighlightingTimer,
                    GameUtils::initializeHitOrMissTimer,
                    ImageManager::loadLifeFormImages,
                    ImageManager::loadSpellMenuImages,
                    ImageManager::loadSkillMenuImages,
                    ImageManager::initializeAttributeImages
            );

            int stepCount = steps.size();
            for (int i = 0; i < stepCount; i++)
            {
                if (GameConfiguration.debugStartUp == true)
                {
                    logger.debug("initialize {}", steps.get(i).toString());
                }
                steps.get(i).run();
                if (GameConfiguration.quickstart == false && splash != null && g != null && size != null)
                {
                    int percent = (int) (((i + 1) * 100.0) / stepCount);
                    renderSplashFrame(g, percent, size);
                }
            }
            if (GameConfiguration.translateTextMaps == true)
            {
                MapUtils.translateTextMaps();
            }

            renderSplashFrame(g, 100, size);
        }
        else
        {
            logger.error("game is null, how did this happen?");
        }
        if (!(GameConfiguration.quickstart))
        {
            if (GameConfiguration.debugSplash == true)
            {
                logger.debug("splash finished");
            }
        }

        if (splash != null)
        {
            splash.close();
        }

        if (startTitle == true)
        {
            openTitleScreen();
        }
        else
        {
            GameUtils.initializeRest();
        }
    }


    public static void openGameUI()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                if (GameConfiguration.debugStartUp == true)
                {
                    logger.debug("open game");
                }
                javax.swing.SwingUtilities.invokeAndWait(() -> WindowBuilder.buildGameWindow());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            WindowBuilder.buildGameWindow();
        }
        UIStateMachine.setUiState(UIState.OPENED);
        UIStateMachine.setUiOpen(true);
        //make this synchronous to make sure the UI is finished.
        //initialize remaining stuff _after_ UI is definitely open

        GameUtils.initializeRest();
        GameUtils.initializeMusicSystemNoThread();
        GameUtils.initializeSoundSystemNoThread();
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire new game state");
        }

        SwingUtilities.invokeLater(() ->
                EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()))
        );

    }

    public static void openCharacterEditor()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                if (GameConfiguration.debugStartUp == true)
                {
                    logger.debug("open character editor");
                }
                javax.swing.SwingUtilities.invokeAndWait(() -> WindowBuilder.buildCharacterEditor());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            WindowBuilder.buildCharacterEditor();
        }
    }


    public static void openTitleScreen()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                if (GameConfiguration.debugStartUp == true)
                {
                    logger.debug("open title screen");
                }
                javax.swing.SwingUtilities.invokeAndWait(() -> WindowBuilder.buildTitleScreen());
                UIStateMachine.setUiState(UIState.TITLE);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static void setGame(Game game)
    {
        RunGame.game = game;
    }

    /**
     * @param g     graphics context
     * @param frame percentage to move to (i.e. move to 100 (%))
     * @param size  dimension of the size of the splash
     */
    public static void renderSplashFrame(Graphics2D g, int frame, Dimension size)
    {
        if (GameConfiguration.debugSplash == true)
        {
            logger.info("dimension: " + size);
        }
        int width = size.width;
        int height = size.height;
        g.clearRect((width - 100), (height - 50), 200, 100);
        g.setComposite(AlphaComposite.SrcOver);
        g.setPaintMode();
        g.setColor(Color.WHITE);
        g.drawString("Loading " + frame + "%", (width - 100), (height - 20));
        g.fillRect(0, height - 50, ((width / 100) * frame), 20);
        if (GameConfiguration.debugSplash == true)
        {
            logger.info("progress: {}", frame);
        }
        splash.update();
    }


    public static void openMapEditor()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                if (GameConfiguration.debugStartUp == true)
                {
                    logger.debug("open character editor");
                }
                javax.swing.SwingUtilities.invokeAndWait(() -> WindowBuilder.buildMapEditor());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            WindowBuilder.buildMapEditor();
        }
    }
}
