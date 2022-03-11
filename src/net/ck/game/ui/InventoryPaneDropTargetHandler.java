package net.ck.game.ui;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.items.AbstractItem;

public class InventoryPaneDropTargetHandler implements DropTargetListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public InventoryPaneDropTargetHandler(InventoryPane inventoryPane)
	{
		// TODO Auto-generated constructor stub
		//DragSource.getDefaultDragSource()
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
	public void dragEnter(DropTargetDragEvent dtde)
	{
		if (dtde.getTransferable().isDataFlavorSupported(ItemTransferable.ITEM_DATA_FLAVOR))
		{
			logger.info("Accept...");
			dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
		}
		else
		{
			logger.info("Drag...");
			dtde.rejectDrag();
		}
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		logger.info("location: {}", dtde.getLocation());
		logger.info("Dropped...");
		if (dtde.getTransferable().isDataFlavorSupported(ItemTransferable.ITEM_DATA_FLAVOR))
		{
			Transferable t =  dtde.getTransferable();
			if (t.isDataFlavorSupported(ItemTransferable.ITEM_DATA_FLAVOR))
			{
				try
				{
					Object transferData = t.getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
					if (transferData instanceof AbstractItem)
					{
						AbstractItem item = (AbstractItem) transferData;
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);	
						
					}
					else
					{
						dtde.rejectDrop();
					}
				}
				catch (UnsupportedFlavorException ex)
				{
					ex.printStackTrace();
					dtde.rejectDrop();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					dtde.rejectDrop();
				}
			}
			else
			{
				dtde.rejectDrop();
			}
		}
		
	}
}
