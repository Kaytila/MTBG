package net.ck.game.ui.listeners;

import net.ck.util.CodeUtils;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is used for the highlighting timer. Highlight timer is used to draw the blinking shape
 * around player
 */
public class HightlightTimerActionListener implements ActionListener {
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /**
     * whenever the timer ticks, update the highlight count in grid canvas.
     * This defines how the highlighting frame is being drawn (i.e. how big).
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //logger.info("calling increase highlight count");
        WindowBuilder.getGridCanvas().increaseHighlightCount();
    }
}
