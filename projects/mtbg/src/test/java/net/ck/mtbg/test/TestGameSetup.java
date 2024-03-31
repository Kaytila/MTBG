package net.ck.mtbg.test;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.utils.GameUtils;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestGameSetup
{

	private static final Logger logger = LogManager.getLogger(TestGameSetup.class);

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
			GameUtils.initializeAllItems();
			GameUtils.initializeMaps();
			game.addPlayers(null);
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
			ImageManager.loadLifeFormImages();
			game.startThreads();
			GameUtils.initializeHighlightingTimer();
			GameUtils.initializeHitOrMissTimer();
			GameUtils.initializeMusicSystemNoThread();
			GameUtils.initializeSoundSystemNoThread();
		}
	}

}
