package net.ck.mtbg.run;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.listeners.Controller;
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
    private static Controller window;
    private static int progress = 0;
    /**
     * singleton by design, i am so good :P
     */
    private static Game game;

    //TODO
    //https://stackoverflow.com/questions/29290178/gui-has-to-wait-until-splashscreen-finishes-executing
    public static void main(String[] args)
    {
        logger.info("starting game");

        Dimension size = SplashScreen.getSplashScreen().getSize();
        logger.info("splash size: " + size);

        Graphics2D g = splash.createGraphics();

        logger.info("quickstart enabled, no splashscreen");
        logger.info("initialize game");
        CursorUtils.initializeCursors();
        game = Game.getCurrent();
        if (game != null)
        {
            if (generate)
            {
                ImageUtils.createWeatherTypesImages(WeatherTypes.RAIN);
                ImageUtils.createWeatherTypesImages(WeatherTypes.HAIL);
                ImageUtils.createWeatherTypesImages(WeatherTypes.SNOW);
            }
            GameUtils.initializeAllItems();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeSpells();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeSkills();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeMaps();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            MapUtils.translateTextMaps();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.setStartMap();
            game.addPlayers(GameConfiguration.startPosition);
            //ImageUtils.checkImageSize(Game.getCurrent().getCurrentPlayer());
            //game.addAnimatedEntities();
            GameUtils.initializeAnimationSystem();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeBackgroundAnimationSystem();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeForegroundAnimationSystem();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeWeatherSystem();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeIdleTimer();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeQuequeTimer();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeMissileThread();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeMusicTimer();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            ImageUtils.initializeBackgroundImages();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            ImageUtils.initializeForegroundImages();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            game.startThreads();
            GameUtils.initializeHighlightingTimer();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            GameUtils.initializeHitOrMissTimer();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            ImageManager.loadLifeFormImages();
            ImageManager.loadSpellMenuImages();
            ImageManager.loadSkillMenuImages();
            ImageManager.initializeAttributeImages();
            if (!(GameConfiguration.quickstart))
            {
                renderSplashFrame(g, size);
            }
            //ImageManager.loadAdditionalImages();

            if (progress < 100)
            {
                renderSplashFrame(g, 100, size);
            }
            else
            {
                renderSplashFrame(g, 100, size);
            }

        }
        else
        {
            logger.error("game is null, how did this happen?");
        }
        logger.info("splash finished");

        if (splash != null)
        {
            splash.close();
        }

        openTitleScreen();

        //openCharacterEditor();
    }


    public static void openGameUI()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                javax.swing.SwingUtilities.invokeAndWait(() -> setWindow(new Controller()));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            setWindow(new Controller());
        }
        UIStateMachine.setUiState(UIState.OPENED);
        UIStateMachine.setUiOpen(true);
        //make this synchronous to make sure the UI is finished.
        //initialize remaining stuff _after_ UI is definitely open
        GameUtils.initializeRest();
        GameUtils.initializeMusicSystemNoThread();
        GameUtils.initializeSoundSystemNoThread();
        EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
    }

    public static void openCharacterEditor()
    {
        if (SwingUtilities.isEventDispatchThread() == false)
        {
            try
            {
                logger.info("open character editor");
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
        try
        {
            logger.info("open character editor");
            javax.swing.SwingUtilities.invokeAndWait(() -> WindowBuilder.buildTitleScreen());
            UIStateMachine.setUiState(UIState.TITLE);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
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
        // logger.info("dimension: " + size);
        int width = size.width;
        int height = size.height;
        g.clearRect((width - 100), (height - 50), 200, 100);
        g.setComposite(AlphaComposite.SrcOver);
        // g.fillRect(848, 960, 50, 30);
        g.setPaintMode();
        g.setColor(Color.WHITE);

        g.drawString("Loading " + frame + "%", (width - 100), (height - 20));
        g.fillRect(0, height - 50, ((width / 100) * frame), 20);
        logger.info("progress: {}", frame);
        splash.update();
    }

    /**
     * renders the splash frame again with an addition in progress + 5 increments
     *
     * @param g    graphics context
     * @param size splash screen size
     */
    public static void renderSplashFrame(Graphics2D g, Dimension size)
    {
        // logger.info("dimension: " + size);
        int width = size.width;
        int height = size.height;
        g.clearRect((width - 100), (height - 50), 200, 100);
        g.setComposite(AlphaComposite.SrcOver);
        // g.fillRect(848, 960, 50, 30);
        g.setPaintMode();
        g.setColor(Color.WHITE);

        g.drawString("Loading " + (progress) + "%", (width - 100), (height - 20));
        g.fillRect(0, height - 50, ((width / 100) * progress), 20);
        logger.info("progress: {}", progress);
        splash.update();
        progress = progress + 5;
        try
        {
            Thread.sleep(8);
        }
        catch (InterruptedException e)
        {
            logger.error("exception");
        }
    }
}
