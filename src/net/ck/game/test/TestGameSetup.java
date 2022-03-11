package net.ck.game.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.ck.game.backend.Game;
import net.ck.game.backend.entities.Player;
import net.ck.game.map.GameMap;
import net.ck.game.run.RunGame;
import net.ck.game.ui.MainWindow;
import net.ck.util.CursorUtils;
import net.ck.util.ImageUtils;

public class TestGameSetup
{

	private static final Logger logger = (Logger) LogManager.getLogger(TestGameSetup.class);

	private static Game game;


	public static Game getGame()
	{
		return game;
	}



	public static void setGame(Game game)
	{
		TestGameSetup.game = game;
	}



	public static Logger getLogger()
	{
		return logger;
	}



	public static void SetupGameForTest()
	{		
		game = Game.getCurrent();

		if (game != null)
		{
			game.initializeMaps();
			game.initializeAllItems();

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
		}
	}

}
