package net.ck.game.ui;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.NPC;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.WindowClosingAction;

public class AbstractDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	protected static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	public static final String dispatchWindowClosingActionMapKey = "WINDOW_CLOSING";
	public CancelButton cancelButton;
	public OKButton okButton;
	protected JRootPane root;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Logger getLogger()
	{
		return logger;
	}

	
	public AbstractDialog()
	{
		
	}
	
	public static AbstractDialog createDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action)
	{
		switch (title)
		{
			case "Z-Stats" :
			{
				StatsDialog dialog = new StatsDialog(owner, title, modal);
				//dialog.addButtons();
				return dialog;
			}

			case "Inventory" :
			{
				InventoryDialog dialog = new InventoryDialog(owner, title, modal, action);
				//dialog.addButtons();
				return dialog;
			}

			case "EQ" :
			{
				EQDialog dialog = new EQDialog(owner, title, modal);
				return dialog;
			}			
			
			default :
			{
				throw new IllegalArgumentException("not expected value during Dialog Creation: " + title);
			}
		}
	}

	public AbstractDialog(Frame owner, String title, boolean modal)
	{
		super(owner, title, true);
		setTitle(title);
		this.setBounds(0, 0, 300, 300);

		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	}
	


	public void addButtons()
	{
		cancelButton = new CancelButton();
		okButton = new OKButton();
		okButton.setBounds(getWidth() - 160, getHeight() - 70, 70, 30);
		cancelButton.setBounds(getWidth() - 90, getHeight() - 70, 70, 30);
	}

	public static TalkDialog createDialog(JFrame frame, String string, boolean b, AbstractKeyboardAction currentAction, NPC n)
	{
		TalkDialog dialog = new TalkDialog(frame, string, b, null, n);
		return dialog;
		
	}
}
