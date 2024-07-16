package net.ck.mtbg.util.communication.keyboard;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.UILense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * WindowClosingAction is being used by AbstractDialog to send a close action to the dialog. Main use: get control and focus back to the MainFrame and distinguish between window is de-activated and
 * dialog is over the MainFrame. In the first case, pause the music (stop :(), in the second case, do not stop the music.
 *
 * @author Claus
 */
@Getter
@Setter
@Log4j2
public class WindowClosingAction extends AbstractAction
{
    private Window component;

    public WindowClosingAction(Window iD)
    {
        setComponent(iD);
    }

    /**
     * close the dialog window tell the Controller the dialog is closed. Tell the frame to request focus again for safety matters, tell the frame to repaint
     * https://stackoverflow.com/questions/720208/how-to-find-out-which-object-currently-has-focus?rq=1
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //TODO
        logger.info("Window closing action sent");
        getComponent().dispatchEvent(new WindowEvent(getComponent(), WindowEvent.WINDOW_CLOSING));
        UIStateMachine.setDialogOpened(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        UILense.getCurrent().identifyVisibleTilesBest();
        UILense.getCurrent().identifyBufferedTiles();
        MapUtils.calculateTiles(WindowBuilder.getGridCanvas().getGraphics());
        MapUtils.calculateVisibleTileImages(WindowBuilder.getGridCanvas().getGraphics());
        WindowBuilder.getGridCanvas().paint();
        WindowBuilder.getFrame().repaint();
    }
}
