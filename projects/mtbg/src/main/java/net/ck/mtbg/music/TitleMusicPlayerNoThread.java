package net.ck.mtbg.music;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Log4j2
@Getter
@Setter
/**
 * specific music player solely for title screen and credits and so on and character creation perhaps.
 * Could I update the music player to do this as well? Sure, but this would dramatically break things - so why bother
 * the extra class does not hurt.
 */
public class TitleMusicPlayerNoThread
{
    AudioInputStream audioInputStream = null;
    private Clip currentMusic;


    public void playSong(Path path)
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
            // getCurrentMusic().addLineListener(new SoundLineListener());
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

        //getCurrentMusic().start();
        getCurrentMusic().loop(Clip.LOOP_CONTINUOUSLY);
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

    public void pauseMusic()
    {
        if (currentMusic == null)
        {
            return;
        }
        logger.info("pause music");
        currentMusic.stop();
    }
}
