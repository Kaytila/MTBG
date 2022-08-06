package net.ck.game.sound;

import net.ck.game.backend.Game;
import net.ck.game.backend.GameState;
import net.ck.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.sound.sampled.*;
import java.io.File;
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
 */
public class SoundPlayer implements Runnable
{

    private static String basePath = "music";
    private final Logger logger = LogManager.getLogger(getRealClass());


    private DirectoryStream<Path> songDirectory;
    private List<File> songDirectories;
    private ArrayList<Path> result = new ArrayList<>();

    private SourceDataLine line;
    private GameState gameState;
    private boolean musicIsRunning;

    /**
     * https://stackoverflow.com/questions/37034624/play-and-stop-music-java https://stackoverflow.com/questions/5529754/java-io-ioexception-mark-reset-not-supported/9324190
     */
    public SoundPlayer()
    {
        super();
        logger.info("initialize sound player");
        EventBus.getDefault().register(this);
        readSoundDirectories(getBasePath());
        //betterPlaySong(getResult().get(selectRandomSong()));
    }

    public static String getBasePath()
    {
        return basePath;
    }

    public static void setBasePath(String basePath)
    {
        SoundPlayer.basePath = basePath;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }

    public ArrayList<Path> getResult()
    {
        return result;
    }


    public void setResult(ArrayList<Path> result)
    {
        this.result = result;
    }

    public SourceDataLine getLine()
    {
        return line;
    }

    public void setLine(SourceDataLine line)
    {
        this.line = line;
    }

    public synchronized boolean isMusicIsRunning()
    {
        //System.out.println(musicIsRunning);
        return musicIsRunning;
    }

    public synchronized void setMusicIsRunning(boolean music)
    {
        logger.info("Music is running: {}", music);
        this.musicIsRunning = music;
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
     * reads all the files from the basePath2 directory variable.
     * @param basePath2 - currently - it is "music", so all files below music are being read
     */
    private void readSoundDirectories(String basePath2)
    {
        TikaConfig tika = null;

        try
        {
            tika = new TikaConfig();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            songDirectories = Files.list(Paths.get(basePath2)).map(Path::toFile).collect(Collectors.toList());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for (File f : songDirectories)
        {
            try
            {
                songDirectory = Files.newDirectoryStream(f.toPath());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            for (Path entry : songDirectory)
            {
                try
                {
                    Metadata metadata = new Metadata();
                    //TODO check mime type properly that only real music is added to the result list
                    //TODO result list needs organizing
                    //TODO result lists need organizing and some mapping to game types somehow
                    MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(entry, metadata), metadata);
                    System.out.println("File " + entry + " is " + mimetype);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                result.add(entry);
            }
        }

        for (Path e : result)
        {
            logger.info("adding sound file {} ", e.toString());
        }
        Game.getCurrent().stopGame();
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
            try
            {
                if (getGameState() == null)
                {
                    logger.debug("game state is null yet, default to WORLD");
                    setGameState(GameState.WORLD);
                }

                if (isMusicIsRunning() == true)
                {
                    switch (getGameState())
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
                else
                {
                    //logger.info("quiet, no music");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
     * @param filePath - song selection
     */
    private void betterPlaySong(Path filePath)
    {
        try
        {
               logger.info("playing song: {}", filePath.toString());
                byte[] buffer = new byte[4096];

                File file = new File(filePath.toString());


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
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback
     */
    public void stopMusic()
    {
        if (Game.getCurrent().isPlayMusic())
        {
            setMusicIsRunning(false);
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
            setMusicIsRunning(true);
            if (line != null)
            {
                if (!(line.isActive()))
                {
                    line.start();
                }
            }
        }
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
        //logger.info("Current value: {}", gainControl.getValue());
        gainControl.setValue(gainControl.getValue() - 1.0f); // Reduce volume by 10 decibels.
    }

    /**
     * https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
     */
    public void increaseVolume()
    {
        FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        //logger.info("Current value: {}", gainControl.getValue());
        if ((gainControl.getValue() + 1.0f) < 6.0f)
        {
            gainControl.setValue(gainControl.getValue() + 1.0f); // increase volume by 10 decibels.
        }
    }

    @Subscribe
    public void onMessageEvent(GameStateChanged gameStat)
    {
        if (getGameState() != null)
        {
            if (getGameState().equals(gameStat.getGameState()))
            {
                logger.info("same state, dont stop");
            }
            else
            {
                setGameState(gameStat.getGameState());
                logger.info("changing music to: {}", gameStat.getGameState());

                if (line != null)
                {
                    logger.info("stopping current song");
                    line.stop();
                    line.flush();
                }
                else
                {
                    logger.info("nothing running");
                }
            }
        }
        else
        {
            logger.info("it was quiet");
            logger.info("changing music to: {}", gameStat.getGameState());
            setGameState(gameStat.getGameState());
        }
    }

}
