package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.InventoryPane;
import net.ck.mtbg.ui.renderers.InventoryPaneListCellRenderer;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ContainerDialog extends AbstractDialog
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	public ContainerDialog()
	{

	}
	
	/**
	 * Might be that I can reuse the full inventory dialog again
	 * https://stackoverflow.com/questions/8976874/show-two-dialogs-on-top-of-each-other-using-java-swing
	 * @param owner
	 * @param title
	 * @param modal
	 * @param inventory
	 */
	public ContainerDialog(Frame owner, String title, boolean modal, Inventory inventory)
	{
		setTitle(title);
		this.setBounds(0, 0, 300, 300);
		this.setLayout(null);
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 300, 300);
		panel.setLayout(null);		
		this.setContentPane(panel);		
		
		InventoryPane invP = new InventoryPane(owner, inventory, this, null);
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
}
