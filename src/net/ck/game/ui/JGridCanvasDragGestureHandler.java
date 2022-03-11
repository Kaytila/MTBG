package net.ck.game.ui;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JGridCanvasDragGestureHandler implements DragGestureListener
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public JGridCanvasDragGestureHandler(JGridCanvas gridCanvas)
	{
		// TODO Auto-generated constructor stub
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

	public Logger getLogger()
	{
		return logger;
	}
	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		logger.info("dragGestureRecognized");

	}
}
