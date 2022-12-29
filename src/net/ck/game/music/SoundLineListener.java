package net.ck.game.music;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundLineListener implements LineListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    //TODO
    @Override
    public void update(LineEvent event)
    {
        //logger.info("sound event: {}", event);
       if (event.getType().equals(LineEvent.Type.STOP))
       {
           if (Game.getCurrent().getMusicSystemNoThread().isGameStateChanged())
           {
               return;
           }
           //logger.info("stop event");
           if (Game.getCurrent().getMusicSystemNoThread().isPaused())
           {
               //logger.info("music paused, do not do anything about the stop");
           }
           else
           {
               //logger.info("play new song");
               Game.getCurrent().getMusicSystemNoThread().playSong();
           }
       }
    }
}
