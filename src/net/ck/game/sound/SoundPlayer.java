package net.ck.game.sound;

import net.ck.game.backend.Game;
import net.ck.game.backend.GameState;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SoundPlayer is a threaded system which basically takes a list of songs and randomly plays one. TODO here: add an event "combat" or "town" or "exploring", tag songs accordingly and play the music
 * based on this. Copy of iMUSE system in easy so to speak.
 * 
 * @author Claus
 *
 */
public class SoundPlayer implements Runnable
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	private static String basePath = "music";
	public static String getBasePath()
	{
		return basePath;
	}

	public static void setBasePath(String basePath)
	{
		SoundPlayer.basePath = basePath;
	}

	private AudioInputStream audioinputStream;
	private InputStream inputStream;
	private DirectoryStream<Path> songDirectory;
	private List<File> songDirectories;

	private ArrayList<Path> result = new ArrayList<Path>();
	private Clip clip;
	private SourceDataLine line;

	private GameState gameState;


	public ArrayList<Path> getResult()
	{
		return result;
	}

	public void setResult(ArrayList<Path> result)
	{
		this.result = result;
	}

	private InputStream bufferedIn;
	private boolean musicIsRunning = false;

	public SourceDataLine getLine()
	{
		return line;
	}

	public void setLine(SourceDataLine line)
	{
		this.line = line;
	}

	public boolean isMusicIsRunning()
	{
		return musicIsRunning;
	}

	public void setMusicIsRunning(boolean musicIsRunning)
	{
		this.musicIsRunning = musicIsRunning;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	/**
	 * https://stackoverflow.com/questions/37034624/play-and-stop-music-java https://stackoverflow.com/questions/5529754/java-io-ioexception-mark-reset-not-supported/9324190
	 * 
	 *
	 */
	public SoundPlayer()
	{
		super();
		logger.info("initialize sound player");
		readSoundDirectories(getBasePath());
	}

	private void readSoundDirectories(String basePath2)
	{
		try
		{
			songDirectories = Files.list(Paths.get(basePath2)).map(Path::toFile).collect(Collectors.toList());

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (File f : songDirectories)
		{
			try
			{
				songDirectory = Files.newDirectoryStream(f.toPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			for (Path entry : songDirectory)
			{
				result.add(entry);
			}
		}

		for (Path e : result)
		{
			logger.info("adding sound file {} ", e.toString());
		}

	}

	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public void run()
	{
		while (Game.getCurrent().isRunning() == true)
		{
			if (gameState == null)
			{
				gameState = Game.getCurrent().getGameState();
			}



			if (line != null)
			{
				logger.info("line is not null");
				if (line.isRunning())
				{
					logger.info("line is running");
					if (gameState.equals(Game.getCurrent().getGameState()))
					{
						logger.info("same state - keep playing");
					}
					else
					{
						logger.info("different state");
						gameState = Game.getCurrent().getGameState();
					}
				}
				else
				{
					logger.info("line is not running");
					switch(Game.getCurrent().getGameState())
					{
						case WORLD:
							betterPlaySong(getResult().get(7));
							break;
						case TOWN:
							betterPlaySong(getResult().get(6));
							break;
						case CASTLE:
							betterPlaySong(getResult().get(0));
							break;
						case COMBAT:
							betterPlaySong(getResult().get(1));
							break;
						case DUNGEON:
							betterPlaySong(getResult().get(2));
							break;
						default:
							betterPlaySong(getResult().get(selectRandomSong()));
							break;
					}

				}
			}
			else
			{
				logger.info("line == null");
				switch(Game.getCurrent().getGameState())
				{
					case WORLD:
						betterPlaySong(getResult().get(7));
						break;
					case TOWN:
						betterPlaySong(getResult().get(6));
						break;
					case CASTLE:
						betterPlaySong(getResult().get(0));
						break;
					case COMBAT:
						betterPlaySong(getResult().get(1));
						break;
					case DUNGEON:
						betterPlaySong(getResult().get(2));
						break;
					default:
						betterPlaySong(getResult().get(selectRandomSong()));
						break;
				}
				//betterPlaySong(getResult().get(selectRandomSong()));
			}
		}
	}

	private int selectRandomSong()
	{
		return (int) Math.floor(Math.random() * result.size());
	}

	/**
	 * https://stackoverflow.com/questions/22741988/java-playing-sounds-in-order
	 * 
	 * @param filePath
	 */
	private void betterPlaySong(Path filePath)
	{
		if (musicIsRunning == true)
		{
			logger.info("playing song: {}", filePath.toString());
			byte[] buffer = new byte[4096];
			File file = new File(filePath.toString());
			try
			{
				AudioInputStream is = AudioSystem.getAudioInputStream(file);

				AudioFormat format = is.getFormat();
				line = AudioSystem.getSourceDataLine(format);
				line.open(format);
				line.start();

				while (is.available() > 0)
				{
					int len = is.read(buffer);
					line.write(buffer, 0, len);
				}
				line.drain();
				line.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}



	/**
	 * https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback
	 */
	public void stopMusic()
	{
		if (Game.getCurrent().isPlayMusic())
		{
			musicIsRunning = false;
			if (line != null)
			{
				if (line.isActive())
				{
					line.stop();
				}
			}
		}
	}

	/**
	 * https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback
	 */
	public void startMusic()
	{
		if (Game.getCurrent().isPlayMusic())
		{
			musicIsRunning = true;
			if (line != null)
			{
				if (!(line.isActive()))
				{
					line.start();
				}
			}
		}
		// playSong(getResult().get(selectRandomSong()));
	}

	public void restartMusic()
	{
		if (Game.getCurrent().isPlayMusic())
		{
			if (line != null)
			{
				line.flush();
			}
		}
	}


	/**
	 * https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
	 */
	public void decreaseVolume()
	{
		FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
	}

	/**
	 * https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
	 */
	public void increaseVolume()
	{
		FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(+10.0f); // increase volume by 10 decibels.
	}

	public void switchMusic(AbstractKeyboardAction action)
	{
		switch (action.getType())
		{
			case ATTACK :
				break;
			case EAST :
				break;
			case ENTER :
				break;
			case ESC :
				break;
			case INVENTORY :
				break;
			case NORTH :
				break;
			case NULL :
				break;
			case SOUTH :
				break;
			case SPACE :
				break;
			case TALK :
				break;
			case WEST :
				break;
			case ZSTATS :
				break;
			default :
				break;

		}
	}

}
