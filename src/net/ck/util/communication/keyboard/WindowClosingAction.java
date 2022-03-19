package net.ck.util.communication.keyboard;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import javax.swing.AbstractAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;

/**
 * WindowClosingAction is being used by AbstractDialog to send a close action to the dialog. Main use: get control and focus back to the MainFrame and distinguish between window is de-activated and
 * dialog is over the MainFrame. In the first case, pause the music (stop :(), in the second case, do not stop the music.
 * 
 * @author Claus
 *
 */
public class WindowClosingAction extends AbstractAction implements WindowListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger(getRealClass());
	private Window component;

	public WindowClosingAction(Window iD)
	{
		setComponent(iD);
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
	}


	/**
	 * close the dialog window tell the Controller the dialog is closed. Tell the frame to request focus again for safety matters, tell the frame to repaint
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		getComponent().dispatchEvent(new WindowEvent(getComponent(), WindowEvent.WINDOW_CLOSING));
		Game.getCurrent().getController().setDialogOpened(false);
		Game.getCurrent().getController().getFrame().requestFocus();
		Game.getCurrent().getController().getFrame().repaint();

	}

	public Window getComponent()
	{
		return component;
	}

	public void setComponent(Window component)
	{
		this.component = component;
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		Game.getCurrent().getController().setDialogOpened(true);
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		Game.getCurrent().getController().setDialogOpened(false);
		Game.getCurrent().getController().getFrame().requestFocus();
		Game.getCurrent().getController().getFrame().repaint();
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}
}
