package net.ck.mtbg.run;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.listeners.Controller;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.*;
import net.ck.mtbg.weather.WeatherTypes;

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
	/*	boolean quickstart = false;
		Point startPosition = null;
		for (String ar : args)
		{
			logger.info("vm args: {}", ar);
			if (ar.equalsIgnoreCase("quick"))
			{
				quickstart = true;
			}

			if (ar.startsWith("startPosition"))
			{
				logger.debug("starting position: {}", ar);
				String[] parts = ar.split(":");
				logger.debug("pos 0: {}", parts[0]);
				logger.debug("pos 1: {}", parts[1]);
				String[] pos = parts[1].split("@");
				startPosition = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));

			}
		}*/

        if (GameConfiguration.quickstart)
        {
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
                GameUtils.initializeSpells();
                GameUtils.initializeSkills();
                GameUtils.initializeMaps();
                MapUtils.translateTextMaps();
                game.addPlayers(GameConfiguration.startPosition);
                //ImageUtils.checkImageSize(Game.getCurrent().getCurrentPlayer());
                //game.addAnimatedEntities();
                GameUtils.initializeAnimationSystem();
                GameUtils.initializeBackgroundAnimationSystem();
                GameUtils.initializeForegroundAnimationSystem();
                GameUtils.initializeWeatherSystem();
                GameUtils.initializeIdleTimer();
                GameUtils.initializeQuequeTimer();
                GameUtils.initializeMissileThread();
                GameUtils.initializeMusicTimer();
                ImageUtils.initializeBackgroundImages();
                ImageUtils.initializeForegroundImages();
                game.startThreads();
                GameUtils.initializeHighlightingTimer();
                GameUtils.initializeHitOrMissTimer();
                GameUtils.initializeMusicSystemNoThread();
                GameUtils.initializeSoundSystemNoThread();
                ImageManager.loadLifeFormImages();
                ImageManager.loadSpellMenuImages();
                ImageManager.loadSkillMenuImages();
                ImageManager.initializeAttributeImages();
                //ImageManager.loadAdditionalImages();
            }
            else
            {
                logger.error("game is null, how did this happen?");
            }

            try
            {
                javax.swing.SwingUtilities.invokeAndWait(() -> setWindow(new Controller()));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            UIStateMachine.setUiOpen(true);
            //make this synchronous to make sure the UI is finished.
            //initialize remaining stuff _after_ UI is definitely open
            GameUtils.initializeRest();

        }
        else
        {

            if (splash == null)
            {
                logger.error("SplashScreen.getSplashScreen() returned null");
                System.exit(-1);
            }
            else
            {
                Dimension size = SplashScreen.getSplashScreen().getSize();
                logger.info("splash size: " + size);

                Graphics2D g = splash.createGraphics();
                if (g == null)
                {
                    logger.error("g is null");
                    return;
                }

                renderSplashFrame(g, size);

                logger.info("initialize game");
                CursorUtils.initializeCursors();
                renderSplashFrame(g, size);
                game = Game.getCurrent();
                renderSplashFrame(g, size);
                if (game != null)
                {
                    if (generate)
                    {
                        ImageUtils.createWeatherTypesImages(WeatherTypes.RAIN);
                        ImageUtils.createWeatherTypesImages(WeatherTypes.HAIL);
                        ImageUtils.createWeatherTypesImages(WeatherTypes.SNOW);
                    }
                    GameUtils.initializeAllItems();
                    renderSplashFrame(g, size);
                    GameUtils.initializeMaps();
                    MapUtils.translateTextMaps();
                    renderSplashFrame(g, size);
                    game.addPlayers(GameConfiguration.startPosition);
                    renderSplashFrame(g, size);
                    //ImageUtils.checkImageSize(Game.getCurrent().getCurrentPlayer());
                    //game.addAnimatedEntities();
                    renderSplashFrame(g, size);
                    GameUtils.initializeAnimationSystem();
                    renderSplashFrame(g, size);
                    GameUtils.initializeBackgroundAnimationSystem();
                    renderSplashFrame(g, size);
                    GameUtils.initializeForegroundAnimationSystem();
                    renderSplashFrame(g, size);
                    GameUtils.initializeWeatherSystem();
                    renderSplashFrame(g, size);
                    GameUtils.initializeIdleTimer();
                    renderSplashFrame(g, size);
                    GameUtils.initializeQuequeTimer();
                    renderSplashFrame(g, size);
                    GameUtils.initializeMissileThread();
                    renderSplashFrame(g, size);
                    GameUtils.initializeMusicTimer();
                    renderSplashFrame(g, size);
                    renderSplashFrame(g, size);
                    ImageUtils.initializeBackgroundImages();
                    renderSplashFrame(g, size);
                    ImageUtils.initializeForegroundImages();
                    renderSplashFrame(g, size);
                    game.startThreads();
                    renderSplashFrame(g, size);
                    GameUtils.initializeHighlightingTimer();
                    GameUtils.initializeHitOrMissTimer();
                    GameUtils.initializeMusicSystemNoThread();
                    GameUtils.initializeSoundSystemNoThread();
                    ImageManager.loadLifeFormImages();
                    ImageManager.loadSpellMenuImages();
                    ImageManager.loadSkillMenuImages();
                    ImageManager.initializeAttributeImages();
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
                g.dispose();
            }
            //finish splash, open UI
            logger.info("splash finished");

            if (splash != null)
            {
                splash.close();
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
                try
                {
                    javax.swing.SwingUtilities.invokeAndWait(() -> setWindow(new Controller()));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            UIStateMachine.setUiOpen(true);
            //make this synchronous to make sure the UI is finished.
            //initialize remaining stuff _after_ UI is definitely open
            GameUtils.initializeRest();

            //System.gc();
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
