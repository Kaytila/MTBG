package net.ck.mtbg.ui.dnd;

import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

public class JGridCanvasDragSourceHandler implements DragSourceListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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
