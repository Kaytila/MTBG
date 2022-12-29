package net.ck.game.ui.listeners;

import net.ck.game.ui.MainWindow;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MouseActionListener implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	MainWindow win;

	public MouseActionListener(MainWindow mainWindow)
	{
		win = mainWindow;
	}


	@Override
	public void actionPerformed(ActionEvent evt)
	{
		logger.info("mouse pressed");
		win.setMousePressed(true);
		win.createMovement();
	}
}
