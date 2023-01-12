package net.ck.util.communication.keyboard;

import net.ck.game.backend.game.Game;
import net.ck.util.CodeUtils;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * WindowClosingAction is being used by AbstractDialog to send a close action to the dialog. Main use: get control and focus back to the MainFrame and distinguish between window is de-activated and
 * dialog is over the MainFrame. In the first case, pause the music (stop :(), in the second case, do not stop the music.
 * 
 * @author Claus
 *
 */
public class WindowClosingAction extends AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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
	public void actionPerformed(ActionEvent e) {
        logger.info("Window closing action sent");
        getComponent().dispatchEvent(new WindowEvent(getComponent(), WindowEvent.WINDOW_CLOSING));
        Game.getCurrent().getController().setDialogOpened(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        WindowBuilder.getFrame().repaint();
        //Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
        //logger.info("who has focus: {}", focusOwner);

    }

	public Window getComponent()
	{
		return component;
	}

	public void setComponent(Window component)
	{
		this.component = component;
	}
}
