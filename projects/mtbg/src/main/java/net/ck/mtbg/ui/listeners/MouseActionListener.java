package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MouseActionListener implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	Controller win;

	public MouseActionListener(Controller controller) {
		win = controller;
	}


	@Override
	public void actionPerformed(ActionEvent evt)
	{
		logger.info("mouse pressed");
		win.setMousePressed(true);
		win.createMovement();
	}
}
