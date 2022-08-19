package net.ck.game.sound;

import net.ck.game.backend.Game;
import net.ck.game.backend.GameConfiguration;
import net.ck.game.backend.GameState;
import net.ck.util.SoundUtils;
import net.ck.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;
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

    public DirectoryStream<Path> getSongDirectory()
    {
        return songDirectory;
    }

    public void setSongDirectory(DirectoryStream<Path> songDirectory)
    {
        this.songDirectory = songDirectory;
    }

    public List<File> getSongDirectories()
    {
        return songDirectories;
    }

    public void setSongDirectories(List<File> songDirectories)
    {
        this.songDirectories = songDirectories;
    }

    public SongListMap getResultMap()
    {
        return resultMap;
    }

    public void setResultMap(SongListMap resultMap)
    {
        this.resultMap = resultMap;
    }

    private SongListMap resultMap = new SongListMap();

    /**
     * <a href="https://stackoverflow.com/questions/37034624/play-and-stop-music-java">...</a> <a href="https://stackoverflow.com/questions/5529754/java-io-ioexception-mark-reset-not-supported/9324190">...</a>
     */
    public SoundPlayer()
    {
        super();
        getLogger().info("initialize sound player");
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
        //return Game.getCurrent().getGameState();
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
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    /**
     * reads all the files from the basePath2 directory variable.
     *
     * @param basePath2 - currently - it is "music", so all files below music are being read
     */
    private void readSoundDirectories(String basePath2)
    {
        try
        {
            setSongDirectories(Files.list(Paths.get(basePath2)).map(Path::toFile).collect(Collectors.toList()));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for (File f : getSongDirectories())
        {

            try
            {
                setSongDirectory(Files.newDirectoryStream(f.toPath()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            for (Path entry : getSongDirectory())
            {
                if (SoundUtils.detectFileType(entry).getBaseType().toString().contains("audio"))
                {
                    GameState state = GameState.WORLD;
                    switch (f.getName())
                    {
                        case "WORLD":
                            break;
                        case "TOWN":
                            state = GameState.TOWN;
                            break;
                        case "CASTLE":
                            state = GameState.CASTLE;
                            break;
                        case "COMBAT":
                            state = GameState.COMBAT;
                            break;
                        case "DUNGEON":
                            state = GameState.DUNGEON;
                            break;
                        case "VICTORY":
                            state = GameState.VICTORY;
                            break;
                        case "CRITICAL":
                            state = GameState.CRITICAL;
                            break;
                        case "OCEAN":
                            state = GameState.OCEAN;
                            break;
                        case "SHRINE":
                            state = GameState.SHRINE;
                            break;
                        case "SHOP":
                            state = GameState.SHOP;
                            break;
                        case "STONES":
                            state = GameState.STONES;
                            break;
                        case "DAWN":
                            state = GameState.DAWN;
                            break;
                        case "DUSK":
                            state = GameState.DUSK;
                            break;
                        default:
                            getLogger().error("unknown directory: {}", f.getName());
                    }
                    getResultMap().addSong(state, entry);
                    //getLogger().info("adding sound file {} to gamestate {}", entry.toString(), state);
                }

            }
        }
        //Game.getCurrent().stopGame();
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
                //TODO get game state out of here I think.
                //or perhaps not.
                if (getGameState() == null)
                {
                    getLogger().debug("game state is null yet, default to WORLD");
                    setGameState(GameState.DUSK);
                }

                if (isMusicIsRunning() == true)
                {
                    switch (getGameState())
                    {
                        case WORLD:
                            betterPlaySong(getResultMap().get(GameState.WORLD).get(betterSelectRandomSong(getResultMap().get(GameState.WORLD))));
                            break;
                        case TOWN:
                            betterPlaySong(getResultMap().get(GameState.TOWN).get(betterSelectRandomSong(getResultMap().get(GameState.TOWN))));
                            break;
                        case CASTLE:
                            betterPlaySong(getResultMap().get(GameState.CASTLE).get(betterSelectRandomSong(getResultMap().get(GameState.CASTLE))));
                            break;
                        case COMBAT:
                            betterPlaySong(getResultMap().get(GameState.COMBAT).get(betterSelectRandomSong(getResultMap().get(GameState.COMBAT))));
                            break;
                        case DUNGEON:
                            betterPlaySong(getResultMap().get(GameState.DUNGEON).get(betterSelectRandomSong(getResultMap().get(GameState.DUNGEON))));
                            break;
                        case VICTORY:
                            betterPlaySong(getResultMap().get(GameState.VICTORY).get(betterSelectRandomSong(getResultMap().get(GameState.VICTORY))));
                            break;
                        case CRITICAL:
                            betterPlaySong(getResultMap().get(GameState.CRITICAL).get(betterSelectRandomSong(getResultMap().get(GameState.CRITICAL))));
                            break;
                        case OCEAN:
                            betterPlaySong(getResultMap().get(GameState.OCEAN).get(betterSelectRandomSong(getResultMap().get(GameState.OCEAN))));
                            break;
                        case SHRINE:
                            betterPlaySong(getResultMap().get(GameState.SHRINE).get(betterSelectRandomSong(getResultMap().get(GameState.SHRINE))));
                            break;
                        case SHOP:
                            betterPlaySong(getResultMap().get(GameState.SHOP).get(betterSelectRandomSong(getResultMap().get(GameState.SHOP))));
                            break;
                        case STONES:
                            betterPlaySong(getResultMap().get(GameState.STONES).get(betterSelectRandomSong(getResultMap().get(GameState.STONES))));
                            break;
                        case DAWN:
                            betterPlaySong(getResultMap().get(GameState.DAWN).get(betterSelectRandomSong(getResultMap().get(GameState.DAWN))));
                            break;
                        case DUSK:
                            betterPlaySong(getResultMap().get(GameState.DUSK).get(betterSelectRandomSong(getResultMap().get(GameState.DUSK))));
                            break;
                        default:
                            betterPlaySong(getResultMap().get(GameState.WORLD).get(betterSelectRandomSong(getResultMap().get(GameState.WORLD))));
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

    private int betterSelectRandomSong(ArrayList<Path> list)
    {
        return (int) Math.floor(Math.random() * list.size());
    }

    /**
     * <a href="https://stackoverflow.com/questions/22741988/java-playing-sounds-in-order">https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback</a>
     *
     * @param filePath - song selection
     */
    private void betterPlaySong(Path filePath)
    {
        try
        {
            getLogger().info("playing song: {}", filePath.toString());
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
     * <a href="https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback">https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback</a>
     */
    public void stopMusic()
    {
        if (GameConfiguration.playMusic)
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
     * <a href="https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback">https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback</a>
     */
    public void startMusic()
    {
        if (GameConfiguration.playMusic)
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

    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
     */
    public void decreaseVolume()
    {
        FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        //logger.info("Current value: {}", gainControl.getValue());
        gainControl.setValue(gainControl.getValue() - 1.0f); // Reduce volume by 10 decibels.
    }

    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
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
