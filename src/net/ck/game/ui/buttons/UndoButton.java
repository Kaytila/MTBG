package net.ck.game.ui.buttons;

import net.ck.util.CodeUtils;
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
		this.setText("UNDO");
		setBounds(p.x, p.y, 70, 30);		
		this.setVisible(true);
		this.setFocusable(false);
		this.setMnemonic(KeyEvent.VK_U);
		this.setActionCommand("Undo");
		this.setEnabled(false);
		this.setDoubleBuffered(true);
		//this.setToolTipText(getLogger().getName());
	}

}
