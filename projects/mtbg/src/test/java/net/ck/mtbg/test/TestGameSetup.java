package net.ck.mtbg.test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.utils.GameUtils;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.ImageUtils;

@Log4j2
public class TestGameSetup
{


    @Getter
    @Setter
    private static Game game;


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
