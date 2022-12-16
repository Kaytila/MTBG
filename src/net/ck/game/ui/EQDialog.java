package net.ck.game.ui;

import java.awt.Frame;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.util.communication.keyboard.WindowClosingAction;

public class EQDialog extends AbstractDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public EQDialog(Frame owner, String title, boolean modal)
	{
		int width = 600;
		int height = 600;
		setTitle(title);
		this.setBounds(0, 0, width, height);
		this.setLayout(null);
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
		cancelButton = new CancelButton();
		okButton = new OKButton();
		okButton.setBounds(width - 160, height - 70, 70, 30);
		cancelButton.setBounds(width - 90, height - 70, 70, 30);
		this.add(cancelButton);
		this.add(okButton);


		EQPanel headPanel = new EQPanel();
		headPanel.setBounds(0, 0, 50, 50);
		headPanel.setVisible(true);
		this.add(headPanel);

		EQPanel backpackPanel = new EQPanel();
		backpackPanel.setBounds(width - 60, 0, 50, 50);
		backpackPanel.setVisible(true);
		this.add(backpackPanel);

		EQPanel torsoPanel = new EQPanel();
		torsoPanel.setBounds(width - 60, height - 160, 50, 50);
		torsoPanel.setVisible(true);
		this.add(torsoPanel);

		this.setVisible(true);
	}

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
}
