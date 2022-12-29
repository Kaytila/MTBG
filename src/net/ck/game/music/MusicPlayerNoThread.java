package net.ck.game.music;

import net.ck.game.backend.state.GameState;
import net.ck.game.backend.state.GameStateMachine;
import net.ck.util.CodeUtils;
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
import java.util.stream.Collectors;

public class MusicPlayerNoThread
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private boolean gameStateChanged;

    public static String getMusicBasePath()
    {
        return musicBasePath;
    }

    private static final String musicBasePath = "music";



    private Clip currentMusic;
    private Clip currentSound;

    private int clipTime;

    private boolean paused;

    AudioInputStream audioInputStream = null;

    private DirectoryStream<Path> songDirectory;
    private List<File> songDirectories;

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


    public MusicPlayerNoThread()
    {
        super();
        logger.info("initialize music player no threaded");
        EventBus.getDefault().register(this);
        readMusicDirectories(getMusicBasePath());
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

    private void readMusicDirectories(String basePath2)
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
                            logger.error("unknown directory: {}", f.getName());
                    }
                    getResultMap().addSong(state, entry);
                    //getLogger().info("adding sound file {} to game state {}", entry.toString(), state);
                }

            }
        }
        //Game.getCurrent().stopGame();
    }

    /**
     * <a href="https://stackoverflow.com/questions/16915241/how-do-i-pause-a-clip-java">...</a>
     */
    public void playSong()
    {
        playSong(getResultMap().get(GameStateMachine.getCurrent().getCurrentState()).get(betterSelectRandomSong(getResultMap().get(GameStateMachine.getCurrent().getCurrentState()))));
    }

    private void playSong(Path path)
    {
        logger.info("playing song: {}", path);

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
            setCurrentMusic(AudioSystem.getClip());
            getCurrentMusic().addLineListener(new SoundLineListener());
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }
        try
        {
            getCurrentMusic().open(audioInputStream);
        }
        catch (LineUnavailableException | IOException e)
        {
            throw new RuntimeException(e);
        }
        getCurrentMusic().setFramePosition(0);

        getCurrentMusic().start();
        //getCurrentMusic().loop(Clip.LOOP_CONTINUOUSLY);
    }

    private int betterSelectRandomSong(ArrayList<Path> list)
    {
        return (int) Math.floor(Math.random() * list.size());
    }


    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
     */
    public void decreaseVolume()
    {
        //TODO decrease volume properly
        FloatControl gainControl = (FloatControl) currentMusic.getControl(FloatControl.Type.MASTER_GAIN);
        //logger.info("Current value: {}", gainControl.getValue());
        gainControl.setValue(gainControl.getValue() - 1.0f); // Reduce volume by 10 decibels.
    }

    /**
     * <a href="https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java">audio-volume-control-increase-or-decrease-in-java</a>
     */
    public void increaseVolume()
    {
        //TODO increase volume properly
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
        logger.info("changing music to: {}", gameStat.getGameState());
        gameStateChanged = true;
        if (currentMusic != null)
        {
            if (currentMusic.isRunning())
            {
                //currentMusic.stop();
                currentMusic.close();
            }
        }
        playSong(getResultMap().get(gameStat.getGameState()).get(betterSelectRandomSong(getResultMap().get(gameStat.getGameState()))));
        setGameStateChanged(false);
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

    /**
     * <a href="https://stackoverflow.com/questions/16915241/how-do-i-pause-a-clip-java">a href="https://stackoverflow.com/questions/16915241/how-do-i-pause-a-clip-java</a>
     * <a href="https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback">https://stackoverflow.com/questions/1550396/pause-a-sourcedataline-playback</a>
     */
    public void pauseMusic()
    {
        if (currentMusic == null)
        {
            return;
        }
        logger.info("pause music");
        setPaused(true);
        clipTime = currentMusic.getFramePosition();
        currentMusic.stop();
    }

    public void continueMusic()
    {
        if (currentMusic == null)
        {
            return;
        }
        logger.info("continue music");
        currentMusic.setFramePosition(clipTime);

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

    public boolean isGameStateChanged()
    {
        return gameStateChanged;
    }

    public void setGameStateChanged(boolean gameStateChanged)
    {
        this.gameStateChanged = gameStateChanged;
    }
}
