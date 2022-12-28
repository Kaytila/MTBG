package net.ck.game.ui.dialogs;

import net.ck.game.ui.buttons.CancelButton;
import net.ck.game.ui.buttons.OKButton;
import net.ck.game.ui.components.StatsPane;
import net.ck.game.ui.renderers.StatsPaneListCellRenderer;
import net.ck.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class StatsDialog extends AbstractDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());	
	private StatsPane statsPane;

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
	
	public StatsDialog(Frame owner, String title, boolean modal)
	{
		setTitle(title);
		this.setBounds(0, 0, 300, 300);
		this.setLayout(null);	
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
			
		statsPane = new StatsPane();		
		statsPane.setBounds(0, 0, 300, 200);
		this.add(statsPane.initializeScrollPane());
		statsPane.setVisible(true);
		
		StatsPaneListCellRenderer listCellRenderer = new StatsPaneListCellRenderer(statsPane);
		statsPane.setCellRenderer(listCellRenderer);
		
		cancelButton = new CancelButton();
		okButton = new OKButton();
		okButton.setBounds(300 - 160, 300 - 70, 70, 30);
		cancelButton.setBounds(300 - 90, 300 - 70, 70, 30);
		this.add(cancelButton);
		this.add(okButton);
		this.setVisible(true);
	
	}

}
