package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.GameUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@Log4j2
@Getter
@Setter
public class OptionsDialogChangeListener implements ChangeListener
{

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting())
        {
            int fps = source.getValue();
            GameConfiguration.setVictoryWait(fps * 1000);
            logger.debug("victory wait: {}", GameConfiguration.getVictoryWait());
            GameUtils.initializeMusicTimer();
        }
    }
}
