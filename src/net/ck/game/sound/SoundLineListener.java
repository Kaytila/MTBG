package net.ck.game.sound;

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

    @Override
    public void update(LineEvent event)
    {
        logger.info("sound event: {}", event);
    }
}
