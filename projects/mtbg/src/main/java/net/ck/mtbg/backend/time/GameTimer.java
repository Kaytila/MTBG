package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class GameTimer extends Timer
{
    public GameTimer(int delay, ActionListener listener)
    {
        super(delay, listener);
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("delay:{}, listener: {}, running: {}", delay, listener, isRunning());
        }
    }
}
