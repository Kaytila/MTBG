package net.ck.game.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MouseActionListener implements ActionListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	MainWindow win;

	public MouseActionListener(MainWindow mainWindow)
	{
		win = mainWindow;
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

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		//logger.info("mouse pressed");
		win.setMousePressed(true);
		win.createMovement();
	}

	public Logger getLogger()
	{
		return logger;
	}
}
