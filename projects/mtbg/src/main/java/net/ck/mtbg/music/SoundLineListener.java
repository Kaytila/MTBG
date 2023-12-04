package net.ck.mtbg.music;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.NoiseManager;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

@Log4j2
public class SoundLineListener implements LineListener
{
    @Override
    public void update(LineEvent event)
    {
        //logger.info("sound event: {}", event);
       if (event.getType().equals(LineEvent.Type.STOP))
       {
           if (NoiseManager.getMusicSystemNoThread().isGameStateChanged())
           {
               return;
           }
           //logger.info("stop event");
           if (NoiseManager.getMusicSystemNoThread().isPaused())
           {
               //logger.info("music paused, do not do anything about the stop");
           }
           else
           {
               //logger.info("play new song");
               NoiseManager.getMusicSystemNoThread().playSong();
           }
       }
    }
}
