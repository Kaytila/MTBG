package net.ck.mtbg.animation.background;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.BackgroundRepresentationChanged;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

@Log4j2
@Getter
@Setter
public class BackgroundAnimationSystemActionListener implements ActionListener
{
    private final Random rand = new Random();
    private int currentBackgroundImage;

    public BackgroundAnimationSystemActionListener()
    {
        setCurrentBackgroundImage(0);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Game.getCurrent().isRunning() == true)
        {
            if ((UIStateMachine.getUiState().equals(UIState.ACTIVATED)) || (UIStateMachine.getUiState().equals(UIState.OPENED)))
            {
                if (GameConfiguration.animated == true)
                {
                    if (GameConfiguration.debugTimers == true)
                    {
                        logger.debug("switch background image");
                    }
                    setCurrentBackgroundImage((rand.nextInt(GameConfiguration.animationCycles)));
                }
                else
                {
                    if (GameConfiguration.debugTimers == true)
                    {
                        logger.debug("keep background image at 0");
                    }
                    setCurrentBackgroundImage(0);
                }
            }
            if ((UIStateMachine.getUiState().equals(UIState.ACTIVATED)) || (UIStateMachine.getUiState().equals(UIState.OPENED)))
            {
                if (GameConfiguration.debugTimers == true)
                {
                    logger.debug("post event that background image changed");
                }

                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("fire new background");
                }
                EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));
            }
        }
    }
}
