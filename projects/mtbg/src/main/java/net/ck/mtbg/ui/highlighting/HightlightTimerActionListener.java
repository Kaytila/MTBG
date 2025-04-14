package net.ck.mtbg.ui.highlighting;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.HighlightEvent;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is used for the highlighting timer. Highlight timer is used to draw the blinking shape
 * around player
 */
@Log4j2
@Getter
@Setter
public class HightlightTimerActionListener implements ActionListener
{

    /**
     * whenever the timer ticks, update the highlight count in grid canvas.
     * This defines how the highlighting frame is being drawn (i.e. how big).
     * if the timer is active and fired, make sure to post an event as well if we
     * use events
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ((UIStateMachine.getUiState().equals(UIState.OPENED)) || (UIStateMachine.getUiState().equals(UIState.ACTIVATED)))
        {
            if (GameConfiguration.debugTimers == true)
            {
                logger.debug("calling increase highlight count");
            }
            WindowBuilder.getGridCanvas().increaseHighlightCount();


            if (GameConfiguration.useEvents == true)
            {
                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("sending highlighting event");
                }
                EventBus.getDefault().post(new HighlightEvent(Game.getCurrent().getCurrentPlayer().getMapPosition()));
            }
        }
    }
}
