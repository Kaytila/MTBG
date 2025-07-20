package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.GameUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@Log4j2
@Getter
@Setter
public class OptionsDialogChangeListener implements ChangeListener, ItemListener
{

    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource().getClass().equals(JSlider.class))
        {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting())
            {
                int fps = source.getValue();
                GameConfiguration.victoryWait = (fps * 1000);
                logger.debug("victory wait: {}", GameConfiguration.victoryWait);
                GameUtils.initializeMusicTimer();
            }
        }


    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource().getClass().equals(JCheckBox.class))
        {
            JCheckBox source = (JCheckBox) e.getSource();

            //TODO
        }
    }
}
