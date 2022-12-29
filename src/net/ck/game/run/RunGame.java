package net.ck.game.run;

import net.ck.game.backend.game.Game;
import net.ck.game.ui.MainWindow;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.CodeUtils;
import net.ck.util.CursorUtils;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class RunGame
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(RunGame.class));

	/**
	 * MainWindow is actually the listener and action class, the main application window is something else entirely. 
	 * Not sure how this is supposed to work in Swing, will need to ask someone about it
	 * 
	 */
	private static MainWindow window;

	private static int progress = 0;

	/**
	 * singleton by design, i am so good :P
	 */
	private static Game game;

	final static SplashScreen splash = SplashScreen.getSplashScreen();

	/**
	 * generate is used to create images or not. As these are already created, no need to do this
	 */
	final static boolean generate = false;
	public static Game getGame()
	{
		return game;
	}

	//TODO
	//https://stackoverflow.com/questions/29290178/gui-has-to-wait-until-splashscreen-finishes-executing
	public static void main(String[] args)
	{
		logger.info("starting game");
		boolean quickstart = false;
		for (String ar : args)
		{
			logger.info("vm args: {}", ar);
			if (ar.equalsIgnoreCase("quick"))
			{
				quickstart = true;
			}
		}

		if (quickstart)
		{
			logger.info("quickstart enabled, no splashscreen");
		}
		else
		{

			if (splash == null)
			{
				logger.error("SplashScreen.getSplashScreen() returned null");
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
					game.initializeAllItems();
					renderSplashFrame(g, size);
					game.initializeNPCs();
					renderSplashFrame(g, size);

					game.initializeMaps();
					renderSplashFrame(g, size);
					game.addPlayers();
					renderSplashFrame(g, size);
					//ImageUtils.checkImageSize(Game.getCurrent().getCurrentPlayer());
					game.addAnimatedEntities();
					renderSplashFrame(g, size);
					game.initializeAnimationSystem();
					renderSplashFrame(g, size);
					game.initializeBackgroundAnimationSystem();
					renderSplashFrame(g, size);
					game.initializeForegroundAnimationSystem();
					renderSplashFrame(g, size);
					game.initializeWeatherSystem();
					renderSplashFrame(g, size);
					game.initializeIdleTimer();
					renderSplashFrame(g, size);
					game.initializeQuequeTimer();
					renderSplashFrame(g, size);
					game.initializeMissileTimer();
					renderSplashFrame(g, size);
					game.initializeMusicTimer();
					renderSplashFrame(g, size);
					//game.initializeSoundSystem();

					renderSplashFrame(g, size);
					ImageUtils.initializeBackgroundImages();
					renderSplashFrame(g, size);
					ImageUtils.initializeForegroundImages();
					renderSplashFrame(g, size);
					game.startThreads();
					renderSplashFrame(g, size);
					//game.initializeSoundSystemNoThread();
					game.initializeHighlightingTimer();
					game.initializeMusicSystemNoThread();
					game.initializeSoundSystemNoThread();

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
			}
			//finish splash, open UI
			logger.info("splash finished");
			if (splash != null)
			{
				splash.close();
			}
			//make this synchronous to make sure the UI is finished.
			try
			{
				javax.swing.SwingUtilities.invokeAndWait(() -> setWindow(new MainWindow()));
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
			//initialize remaining stuff _after_ UI is definitely open
			game.initializeRest();
			//System.gc();
		}

	}

	public static void setGame(Game game)
	{
		RunGame.game = game;
	}

	/**
	 * @param g graphics context
	 * @param frame percentage to move to (i.e. move to 100 (%))
	 * @param size dimension of the size of the splash
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
		g.fillRect(0, height - 50, ((width / 100) * frame) , 20);
		logger.info("progress: {}", frame);
		splash.update();
	}

	/**
	 * renders the splash frame again with an addition in progress + 5 increments
	 * @param g graphics context
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
		g.fillRect(0, height - 50, ((width / 100) * progress) , 20);
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


	public static MainWindow getWindow()
	{
		return window;
	}

	public static void setWindow(MainWindow window)
	{
		RunGame.window = window;
	}

}
