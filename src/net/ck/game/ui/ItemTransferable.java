package net.ck.game.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.items.AbstractItem;

public class ItemTransferable implements Transferable
{

	public static final DataFlavor ITEM_DATA_FLAVOR = new DataFlavor(AbstractItem.class, "Item");
	private AbstractItem item;
	public AbstractItem getItem()
	{
		return item;
	}

	public void setItem(AbstractItem item)
	{
		this.item = item;
	}

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

	public ItemTransferable(AbstractItem item)
	{		
		this.item = item;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]
		{ITEM_DATA_FLAVOR};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return ITEM_DATA_FLAVOR.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		Object value = null;
		if (ITEM_DATA_FLAVOR.equals(flavor))
		{
			value = item;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
		return value;
	}
}