package net.ck.game.run;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

import net.ck.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.Player;
import net.ck.game.ui.MainWindow;
import net.ck.util.CursorUtils;
import net.ck.util.ImageUtils;

public class RunGame
{

	private static final Logger logger = (Logger) LogManager.getLogger(RunGame.class);

	/**
	 * MainWindow is actually the listener and action class, the main application window is something else entirely. 
	 * Not sure how this is supposed to work in Swing, will need to ask someone about it
	 * 
	 */
	private static MainWindow window;

	/**
	 * singleton by design, i am so good :P
	 */
	private static Game game;

	public static Game getGame()
	{
		return game;
	}

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
			final SplashScreen splash = SplashScreen.getSplashScreen();
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
				for (int i = 0; i < 100; i++)
				{
					renderSplashFrame(g, i, size);
					splash.update();
					try
					{
						Thread.sleep(8);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
			logger.info("splash finished");
		}
		logger.info("initialize game");
		
		//ImageUtils.createImage(Color.black, new Point(200, 200), "graphics" + File.separator + "weathertypes" + File.separator + "NONE.png");
		//MapUtils.createMap(111, 111, TileTypes.OCEAN);
		//ImageUtils.createOceanImages();		
		CursorUtils.initializeCursors();
		game = Game.getCurrent();
		//MapUtils.importUltima4MapFromCSV();
		//game.stopGame();
		
		/*try
		{
			logger.info(Thread.currentThread().getName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}*/


		if (game != null)
		{
			//ImageUtils.createWeatherTypesImages(WeatherTypes.RAIN);
			//ImageUtils.createWeatherTypesImages(WeatherTypes.HAIL);
			//ImageUtils.createWeatherTypesImages(WeatherTypes.SNOW);			
			game.initializeAllItems();
			game.initializeNPCs();
			game.listNPCs();
			//game.stopGame();
			
			game.initializeMaps();				
			game.addPlayers();
			ImageUtils.checkImageSize((Player) Game.getCurrent().getCurrentPlayer());
			game.addAnimatedEntities();
			game.initializeAnimationSystem();
			game.initializeBackgroundAnimationSystem();
			game.initializeForegroundAnimationSystem();
			game.initializeWeatherSystem();
			game.initializeTurnTimerTimer();
			game.initializeMusic();
			
			ImageUtils.initializeBackgroundImages();
			ImageUtils.initializeForegroundImages();
			
			javax.swing.SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{					
					setWindow(new MainWindow());
				}
			});
			
			// Thread ga = new Thread(game);
			// ga.start();
		}
		else
		{
			logger.error("game is null, how did this happen?");
		}
	}

	public static void setGame(Game game)
	{
		RunGame.game = game;
	}

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
