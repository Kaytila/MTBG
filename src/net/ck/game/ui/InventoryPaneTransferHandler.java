package net.ck.game.ui;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.Inventory;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Utility;

public class InventoryPaneTransferHandler extends TransferHandler
{

	private static final long serialVersionUID = 1L;

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private InventoryPane sourceInventoryPane;
	private InventoryPane targetInventoryPane;

	private Utility utilityItem = null;

	public InventoryPaneTransferHandler()
	{
		//logger.info("constructor");
	}
	
	public boolean canImport(TransferHandler.TransferSupport info)
	{
		// logger.info("can import");
		InventoryPane.DropLocation dl = (InventoryPane.DropLocation) info.getDropLocation();
		int in = dl.getIndex();
		setTargetInventoryPane((InventoryPane) info.getComponent());
		if (in == -1)
		{
			return true;
		}
		if (getTargetInventoryPane().getModel().getElementAt(in).isContainer())
		{
			utilityItem = (Utility) getTargetInventoryPane().getModel().getElementAt(in);
			return true;
		}
		else
		{
			return false;
		}
	}

	protected Transferable createTransferable(JComponent c)
	{
		logger.info("create transferable");
		setSourceInventoryPane((InventoryPane) c);
		if (getSourceInventoryPane().getSelectedValue() != null)
		{
			setDragImage(getSourceInventoryPane().getSelectedValue().getItemImage());
		}
		return new ItemTransferable(getSourceInventoryPane().getSelectedValue());
	}

	@Override
	public void exportAsDrag(JComponent comp, InputEvent e, int action)
	{
		// logger.info("export as drag");
		super.exportAsDrag(comp, e, action);
	}

	/**
	 * export done is when the drop is finished and dealing with the source control
	 */
	protected void exportDone(JComponent c, Transferable data, int action)
	{
		logger.info("begin: export done");
		setSourceInventoryPane((InventoryPane) c);
		Inventory sourceInv = ((Inventory) getSourceInventoryPane().getModel());

		ItemTransferable itemtransferable = (ItemTransferable) data;
		AbstractItem item = itemtransferable.getItem();

		ArrayList<AbstractItem> found = new ArrayList<AbstractItem>();
		int count = 0;
		// make sure that only one item is added to the removal list.
		for (AbstractItem i : sourceInv.getInventory())
		{
			//logger.info("item: {}", i);
			if (i.equals(item))
			{
				if (count == 0)
				{
					count = 1;
					found.add(item);
				}
			}
		}
		// might be overkill but to be sure its thread safe lets do it that way
		logger.debug("before source inventory: {}", sourceInv.getInventory());
		sourceInv.remove(found.get(0));
		getSourceInventoryPane().repaint();
		logger.debug("after source inventory: {}", sourceInv.getInventory());
		logger.info("end: export done");
	}

	@Override
	public Image getDragImage()
	{
		return super.getDragImage();
	}

	@Override
	public Point getDragImageOffset()
	{
		return super.getDragImageOffset();
	}

	public Logger getLogger()
	{
		return logger;
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

	public int getSourceActions(JComponent comp)
	{
		logger.info("get source actions");
		
		return TransferHandler.MOVE;
	}


	/**
	 * this method deals with the target component.
	 */
	public boolean importData(TransferHandler.TransferSupport info)
	{
		logger.info("begin: import data");
		setTargetInventoryPane((InventoryPane) info.getComponent());
		InventoryPane.DropLocation dl = (InventoryPane.DropLocation) info.getDropLocation();
		int in = dl.getIndex();
		if (!info.isDrop())
		{
			return false;
		}

		AbstractItem item =	null;
		try
		{
			item = (AbstractItem) info.getTransferable().getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Inventory targetInv = ((Inventory) getTargetInventoryPane().getModel());
		// index -1 means this is a new item on this panel
		if (in == -1)
		{
			targetInv = ((Inventory) getTargetInventoryPane().getModel());			
		}
		// this cannot happen otherwise, else it would not be allowed
		else
		{
			if (utilityItem == null)
			{
				logger.error("something is really broken here");
			}			
			targetInv = utilityItem.getInventory();
		}		
		ArrayList<AbstractItem> found = new ArrayList<AbstractItem>();
		int count = 0;
		// make sure that only one item is added to the removal list.
		for (AbstractItem i : targetInv.getInventory())
		{
			if (i.equals(item))
			{
				if (count == 0)
				{
					count = 1;
					found.add(item);
				}
			}
		}
		// might be overkill but to be sure its thread safe lets do it that way
		logger.info("before target inventory: {}", targetInv.getInventory());
		targetInv.add(item);		
		logger.info("after target inventory: {}", targetInv.getInventory());
		getTargetInventoryPane().repaint();
		logger.info("done: import data");
		return true;
	}

	@Override
	public void setDragImage(Image img)
	{
		super.setDragImage(img);
	}

	@Override
	public void setDragImageOffset(Point p)
	{
		super.setDragImageOffset(p);
	}

	public InventoryPane getSourceInventoryPane()
	{
		return sourceInventoryPane;
	}

	public void setSourceInventoryPane(InventoryPane sourceInventoryPane)
	{
		this.sourceInventoryPane = sourceInventoryPane;
	}

	public InventoryPane getTargetInventoryPane()
	{
		return targetInventoryPane;
	}

	public void setTargetInventoryPane(InventoryPane targetInventoryPane)
	{
		this.targetInventoryPane = targetInventoryPane;
	}

}
