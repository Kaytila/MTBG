package net.ck.game.ui;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.WindowClosingAction;

public class InventoryDialog extends AbstractDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public InventoryDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action)
	{
		
		setTitle(title);
		this.setBounds(0, 0, 300, 300);
		this.setLayout(null);	
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
		this.addWindowListener(dispatchClosing);
		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 300);
		panel.setLayout(null);
		this.setContentPane(panel);		
		
		InventoryPane invP = new InventoryPane(owner, Game.getCurrent().getCurrentPlayer().getInventory(), this, action);		
		invP.setBounds(0, 0, 300, 200);
		this.add(invP.initializeScrollPane());
		
		InventoryPaneListCellRenderer listCellRenderer = new InventoryPaneListCellRenderer();
		invP.setCellRenderer(listCellRenderer);
		
		
		cancelButton = new CancelButton();
		okButton = new OKButton();
		okButton.setBounds(300 - 160, 300 - 70, 70, 30);
		cancelButton.setBounds(300 - 90, 300 - 70, 70, 30);
		this.add(cancelButton);
		this.add(okButton);
		this.setVisible(true);
	}

	public InventoryDialog()
	{	
	}

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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