package net.ck.mtbg.music;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.backend.state.GameStateMachine;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.utils.SoundUtils;
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


@Getter
@Setter
@Log4j2
public class MusicPlayerNoThread
{
    AudioInputStream audioInputStream = null;
    private boolean gameStateChanged;
    private Clip currentMusic;

    private int clipTime;
    private boolean paused;
    private DirectoryStream<Path> songDirectory;
    private List<File> songDirectories;

    private boolean musicIsRunning;

    private SongListMap resultMap = new SongListMap();


    public MusicPlayerNoThread()
    {
        super();
        logger.info("initialize music player no threaded");
        EventBus.getDefault().register(this);
        readMusicDirectories(GameConfiguration.musicPath);
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
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        getCurrentMusic().setFramePosition(0);

        getCurrentMusic().start();
        if (GameConfiguration.loopMusic == true)
        {
            getCurrentMusic().loop(Clip.LOOP_CONTINUOUSLY);
        }
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
        if (gameStat.getGameState() != null)
        {
            playSong(getResultMap().get(gameStat.getGameState()).get(betterSelectRandomSong(getResultMap().get(gameStat.getGameState()))));
            setGameStateChanged(false);
        }
        else
        {
            playSong(getResultMap().get(GameState.WORLD).get(betterSelectRandomSong(getResultMap().get(GameState.WORLD))));
            logger.error("why the hell is this null?");
        }
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
}
