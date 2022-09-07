package net.ck.game.sound;

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
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SoundPlayerNoThread
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    private static String basePath = "music";

    private Clip currentMusic;
    private Clip currentSound;

    private long clipTime;

    private boolean paused;

    AudioInputStream audioInputStream = null;

    private DirectoryStream<Path> songDirectory;
    private List<File> songDirectories;
    private ArrayList<Path> result = new ArrayList<>();


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


    public SoundPlayerNoThread()
    {
        super();
        getLogger().info("initialize sound player no threaded");
        EventBus.getDefault().register(this);
        readSoundDirectories(getBasePath());
        //betterPlaySong(getResult().get(selectRandomSong()));
        EventBus.getDefault().post(new GameStateChanged(GameState.WORLD));
        //playSong(getResultMap().get(GameState.WORLD).get(betterSelectRandomSong(getResultMap().get(GameState.WORLD))));
    }

    public static String getBasePath()
    {
        return basePath;
    }

    public static void setBasePath(String basePath)
    {
        SoundPlayerNoThread.basePath = basePath;
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


    public void playSong()
    {
        logger.info("i mean it, play song");
        playSong(getResultMap().get(getGameState()).get(betterSelectRandomSong(getResultMap().get(getGameState()))));
    }

    //TODO it appears clips are not meant to be run in threads
    private void playSong(Path path)
    {
        logger.info("playing song: {}", path);
        /*clipTime = 0;
        if (getCurrentMusic() != null)
        {
            if (getCurrentMusic().isRunning())
            {
                return;
            }
        }*/
        try
        {
            audioInputStream = AudioSystem.getAudioInputStream(new File(path.toUri()));

        }
        catch (UnsupportedAudioFileException e)
        {
            logger.error("cannot play file: {}", path);
        }
        catch (IOException e)
        {
            logger.error("IO Exception: {} for {}", e, path);
        }

             try
            {
                currentMusic = AudioSystem.getClip();
                currentMusic.addLineListener(new SoundLineListener());
            }
            catch (LineUnavailableException e)
            {
                throw new RuntimeException(e);
            }
            try
            {
                currentMusic.open(audioInputStream);
            }
            catch (LineUnavailableException | IOException e)
            {
                throw new RuntimeException(e);
            }
            currentMusic.setFramePosition(0);
            currentMusic.start();

    }

    private int betterSelectRandomSong(ArrayList<Path> list)
    {
        return (int) Math.floor(Math.random() * list.size());
    }

    /**
     * https://stackoverflow.com/questions/16915241/how-do-i-pause-a-clip-java
     */

    public void stopMusic()
    {
        if (GameConfiguration.playMusic)
        {
            setMusicIsRunning(false);
            if (currentMusic != null)
            {
                if (currentMusic.isActive())
                {
                    clipTime = currentMusic.getMicrosecondLength();
                    currentMusic.stop();
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
            if (currentMusic != null)
            {
                if (!(currentMusic.isActive()))
                {
                    logger.info("restarting clip");
                    currentMusic.start();
                }
            }
            else
            {
                logger.info("result map for state: {}", getResultMap().get(getGameState()));
                if (getGameState() != null)
                {
                    playSong(getResultMap().get(getGameState()).get(betterSelectRandomSong(getResultMap().get(getGameState()))));
                }
            }
        }
    }

    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
     */
    public void decreaseVolume()
    {
        FloatControl gainControl = (FloatControl) currentMusic.getControl(FloatControl.Type.MASTER_GAIN);
        //logger.info("Current value: {}", gainControl.getValue());
        gainControl.setValue(gainControl.getValue() - 1.0f); // Reduce volume by 10 decibels.
    }

    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
     */
    public void increaseVolume()
    {
        FloatControl gainControl = (FloatControl) currentMusic.getControl(FloatControl.Type.MASTER_GAIN);
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
                if (currentMusic != null)
                {
                    if (currentMusic.isRunning())
                    {
                        //currentMusic.stop();
                        currentMusic.close();
                    }
                }
                playSong(getResultMap().get(gameStat.getGameState()).get(betterSelectRandomSong(getResultMap().get(gameStat.getGameState()))));
            }
        }
        else
        {
            logger.info("changing music to: {}", gameStat.getGameState());
            setGameState(gameStat.getGameState());
            if (currentMusic != null)
            {
                if (currentMusic.isRunning())
                {
                    //currentMusic.stop();
                    currentMusic.close();
                }
            }
            playSong(getResultMap().get(gameStat.getGameState()).get(betterSelectRandomSong(getResultMap().get(gameStat.getGameState()))));
        }
    }

    public Clip getCurrentMusic()
    {
        return currentMusic;
    }

    public void setCurrentMusic(Clip currentMusic)
    {
        this.currentMusic = currentMusic;
    }

    public Clip getCurrentSound()
    {
        return currentSound;
    }

    public void setCurrentSound(Clip currentSound)
    {
        this.currentSound = currentSound;
    }

    public void pauseMusic()
    {
        logger.info("pause music");
        setPaused(true);
        clipTime = currentMusic.getFramePosition(); //getMicrosecondLength();
        currentMusic.stop();
    }

    public void continueMusic()
    {
        logger.info("continue music");
        currentMusic.setFramePosition((int) clipTime);//setMicrosecondPosition(clipTime);

        try
        {
            currentMusic.open();

        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }

        currentMusic.start();
        setPaused(false);
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }
}
