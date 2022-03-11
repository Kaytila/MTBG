package net.ck.game.ui;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JGridCanvasDragSourceHandler implements DragSourceListener
{

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

	@Override
	public void dragEnter(DragSourceDragEvent dsde)
	{
		logger.info("drag Enter");
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde)
	{
		logger.info("drag over");
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde)
	{
		logger.info("dropActionChanged");
	}

	@Override
	public void dragExit(DragSourceEvent dse)
	{
		logger.info("dragExit");
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		logger.info("dragDropEnd");
	}
}
