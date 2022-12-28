package net.ck.game.ui.dnd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

public class InventoryPaneDragSourceHandler implements DragSourceListener
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
	}
	
	@Override
	public void dragOver(DragSourceDragEvent dsde)
	{

	}
	
	@Override
	public void dropActionChanged(DragSourceDragEvent dsde)
	{

	}
	
	@Override
	public void dragExit(DragSourceEvent dse)
	{
	}
	@Override
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
	}
}
