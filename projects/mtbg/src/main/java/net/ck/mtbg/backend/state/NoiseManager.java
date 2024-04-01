package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.music.MusicPlayerNoThread;
import net.ck.mtbg.soundeffects.SoundPlayerNoThread;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.utils.GameUtils;
import org.greenrobot.eventbus.EventBus;

@Log4j2
@Getter
@Setter
public class NoiseManager
{

    /**
     * soundSystem is the class dealing with the music. currently only taking files from a directory and trying to play one random song at a time
     */
    @Getter
    @Setter
    private static MusicPlayerNoThread musicSystemNoThread;
    @Getter
    @Setter
    private static SoundPlayerNoThread soundPlayerNoThread;


    public static void calculateMusictoRun()
    {
        if (GameStateMachine.getCurrent().getCurrentState() == GameState.COMBAT)
        {
            boolean stillaggro = false;
            for (LifeForm e : Game.getCurrent().getCurrentMap().getLifeForms())
            {
                if (e.isHostile())
                {
                    stillaggro = true;
                    break;
                }
            }

            logger.info("still aggro: {}", stillaggro);

            if (stillaggro == false)
            {
                EventBus.getDefault().post(new GameStateChanged(GameState.VICTORY));
                TimerManager.getMusicTimer().start();
            }
        }

        if (GameStateMachine.getCurrent().getCurrentState() == GameState.VICTORY)
        {
            if (GameUtils.checkVictoryGameStateDuration())
            {
                EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
                if (TimerManager.getMusicTimer().isRunning() == false)
                {
                    TimerManager.getMusicTimer().start();
                }
            }
        }
    }
}
