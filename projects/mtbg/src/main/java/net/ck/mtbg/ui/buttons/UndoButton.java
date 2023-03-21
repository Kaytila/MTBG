package net.ck.mtbg.ui.buttons;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class UndoButton extends JButton 
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	public UndoButton(Point p)
	{
		logger.info("creating button");
		this.setText("DEBUG");
		setBounds(p.x, p.y, 70, 30);
		this.setVisible(true);
		this.setFocusable(false);
		this.setMnemonic(KeyEvent.VK_U);
		this.setActionCommand("Debug");
		this.setEnabled(true);
		this.setDoubleBuffered(true);
		//this.setToolTipText(getLogger().getName());
	}

}
