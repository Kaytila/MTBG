package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.InventoryPane;
import net.ck.mtbg.ui.listeners.WindowClosingListener;
import net.ck.mtbg.ui.renderers.InventoryPaneListCellRenderer;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class InventoryDialog extends AbstractDialog
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	

	public InventoryDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action)
	{
		setTitle(title);
		this.setBounds(0, 0, 300, 300);
		this.setLayout(null);	
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
		final WindowClosingListener windowClosingListener = new WindowClosingListener();
		this.addWindowListener(windowClosingListener);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 300);
		panel.setLayout(null);
		this.setContentPane(panel);
		this.setUndecorated(true);
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


	public void paintBorder(Graphics g)
	{

	}

}