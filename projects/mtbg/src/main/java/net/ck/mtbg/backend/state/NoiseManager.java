package net.ck.mtbg.backend.state;

import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.music.MusicPlayerNoThread;
import net.ck.mtbg.soundeffects.SoundPlayerNoThread;
import net.ck.mtbg.util.GameUtils;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

public class NoiseManager
{
	private static final Logger logger = LogManager.getLogger(NoiseManager.class);

	/**
	 * soundSystem is the class dealing with the music. currently only taking files from a directory and trying to play one random song at a time
	 */
	private static MusicPlayerNoThread musicSystemNoThread;


	private static SoundPlayerNoThread soundPlayerNoThread;

	public static MusicPlayerNoThread getMusicSystemNoThread()
	{
		return musicSystemNoThread;
	}

	public static void setMusicSystemNoThread(MusicPlayerNoThread musicSystemNoThread)
	{
		NoiseManager.musicSystemNoThread = musicSystemNoThread;
	}

	public static SoundPlayerNoThread getSoundPlayerNoThread()
	{
		return soundPlayerNoThread;
	}

	public static void setSoundPlayerNoThread(SoundPlayerNoThread soundPlayerNoThread)
	{
		NoiseManager.soundPlayerNoThread = soundPlayerNoThread;
	}


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
