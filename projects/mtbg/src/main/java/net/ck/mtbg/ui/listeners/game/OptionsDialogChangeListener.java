package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.state.NoiseManager;
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
            logger.debug("we are in the state changed");
            //TODO loudness slider
            JSlider source = (JSlider) e.getSource();

            if (source.getToolTipText().equalsIgnoreCase("victory"))
            {
                if (!source.getValueIsAdjusting())
                {
                    int fps = source.getValue();
                    GameConfiguration.victoryWait = (fps * 1000);
                    logger.debug("victory wait: {}", GameConfiguration.victoryWait);
                    GameUtils.initializeMusicTimer();
                }
            }
            if (source.getToolTipText().equalsIgnoreCase("volume"))
            {
                if (!source.getValueIsAdjusting())
                {
                    int volume = source.getValue();

                    if (volume < GameConfiguration.volume)
                    {
                        NoiseManager.getMusicSystemNoThread().decreaseVolume();
                    }

                    else if (volume > GameConfiguration.volume)
                    {
                        NoiseManager.getMusicSystemNoThread().increaseVolume();
                    }
                    GameConfiguration.setVolume(volume);
                    logger.debug("new volume: {}", GameConfiguration.volume);
                    GameUtils.initializeMusicTimer();
                }
            }
        }


    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource().getClass().equals(JCheckBox.class))
        {
            logger.debug("we are in the item state changed");
            JCheckBox source = (JCheckBox) e.getSource();
            if (source.getText().equalsIgnoreCase("Loop Music"))
            {
                logger.debug("loop music");
                if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    GameConfiguration.setLoopMusic(false);
                }

                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    GameConfiguration.setLoopMusic(true);

                }
                GameUtils.initializeMusicSystemNoThread();
                logger.debug("Loop music state: {}", GameConfiguration.loopMusic);
            }

            if (source.getText().equalsIgnoreCase("Loop Title Music"))
            {
                logger.debug("loop title music");
                if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    GameConfiguration.setLoopTitleMusic(false);
                }

                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    GameConfiguration.setLoopTitleMusic(true);
                }
                logger.debug("Loop title music state: {}", GameConfiguration.loopTitleMusic);
            }

        }
    }
}
