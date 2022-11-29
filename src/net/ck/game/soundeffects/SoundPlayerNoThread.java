package net.ck.game.soundeffects;

import net.ck.util.SoundUtils;
import net.ck.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public Logger getLogger()
    {
        return logger;
    }

    private static final String soundBasePath = "soundeffects";

    private ArrayList<Path> soundEffects;

    private Clip currentSound;
    AudioInputStream audioInputStream = null;



    public SoundPlayerNoThread()
    {
        super();
        //getLogger().info("initialize sound player no threaded");
        EventBus.getDefault().register(this);
        soundEffects = new ArrayList<>();
        readSoundEffectDirectory(getSoundBasePath());
    }

    private void readSoundEffectDirectory(String soundBasePath)
    {
        try
        {
            for (File f : Files.list(Paths.get(soundBasePath)).map(Path::toFile).collect(Collectors.toList()))
            {
                //logger.info("File f: {}", f);
                if (SoundUtils.detectFileType(Paths.get(f.toURI())).getBaseType().toString().contains("audio"))
                {
                    //logger.info("Sound effect: {}", Paths.get(f.toURI()));
                    soundEffects.add(Paths.get(f.toURI()));
                }

            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        //Game.getCurrent().stopGame();
    }


    private String getSoundBasePath()
    {
        return soundBasePath;
    }

    @Subscribe
    public void onMessageEvent(GameStateChanged gameStat)
    {
        logger.info("nothing");
    }

    public synchronized  void playSoundEffect(SoundEffects type)
    {
        Path path = null;
        switch (type)
        {
            case WALK:
            path = soundEffects.get(21);
            break;

            case HIT:
                path = soundEffects.get(11);
            break;

            case BLOCKED:
                path = soundEffects.get(0);
            break;

            case ATTACK:
                path = soundEffects.get(12);
            break;
        }

        //logger.info("playing song: {}", path);

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
            currentSound = (AudioSystem.getClip());
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }
        try
        {
            currentSound.open(audioInputStream);
        }
        catch (LineUnavailableException | IOException e)
        {
            throw new RuntimeException(e);
        }
        currentSound.setFramePosition(0);

        currentSound.start();
    }

}
