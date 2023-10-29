package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.StatsPane;
import net.ck.mtbg.ui.renderers.StatsPaneListCellRenderer;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class StatsDialog extends AbstractDialog
{
	private final Logger    logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private       StatsPane statsPane;

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
		this.setUndecorated(true);
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
