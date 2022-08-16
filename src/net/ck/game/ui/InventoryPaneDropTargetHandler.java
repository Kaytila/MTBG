package net.ck.game.ui;

import net.ck.game.items.AbstractItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

public class InventoryPaneDropTargetHandler implements DropTargetListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public InventoryPaneDropTargetHandler(InventoryPane inventoryPane)
	{
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
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

	@Override
	public void dragExit(DropTargetEvent dte)
	{
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
