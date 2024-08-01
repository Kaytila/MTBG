package net.ck.mtbg.soundeffects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.SoundUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;

@Getter
@Setter
@Log4j2
@ToString
public class SoundPlayerNoThread
{
    AudioInputStream audioInputStream = null;
    private ArrayList<Path> soundEffects;
    private Hashtable<SoundEffects, Path> effectsList;
    private Clip currentSound;


    public SoundPlayerNoThread()
    {
        super();
        //getLogger().info("initialize sound player no threaded");
        // EventBus.getDefault().register(this);
        soundEffects = new ArrayList<>();
        effectsList = new Hashtable<>(SoundEffects.values().length);
        readSoundEffectDirectory(GameConfiguration.soundeffectsPath);
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
                    String shortName = f.getName().substring(0, f.getName().length() - 4);
                    for (SoundEffects ef : SoundEffects.values())
                    {
                        if (ef.toString().equals(shortName))
                        {
                            effectsList.put(ef, Paths.get(f.toURI()));
                        }
                    }

                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        //Game.getCurrent().stopGame();
        for (SoundEffects ef : effectsList.keySet())
        {
            logger.debug("key: {}, value: {}", ef, effectsList.get(ef));
        }
    }

    /*@Subscribe
    public void onMessageEvent(GameStateChanged gameStat)
    {
        logger.info("nothing");
    }*/

    public synchronized void playSoundEffect(SoundEffects type)
    {
        Path path = effectsList.get(type);

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
