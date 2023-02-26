package net.ck.game.backend.state;

import net.ck.game.music.MusicPlayerNoThread;
import net.ck.game.soundeffects.SoundPlayerNoThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
