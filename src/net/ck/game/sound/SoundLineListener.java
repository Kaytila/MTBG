package net.ck.game.sound;

import net.ck.game.backend.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.Objects;

public class SoundLineListener implements LineListener
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    //TODO
    @Override
    public void update(LineEvent event)
    {
        logger.info("sound event: {}", event);
       if (event.getType().equals(LineEvent.Type.STOP))
       {
           logger.info("stop event");
           if (Game.getCurrent().getSoundSystemNoThread().isPaused())
           {
               logger.info("music paused, do not do anything about the stop");
           }
           else
           {
               logger.info("play new song");
               Game.getCurrent().getSoundSystemNoThread().playSong();
           }
       }
    }
}
