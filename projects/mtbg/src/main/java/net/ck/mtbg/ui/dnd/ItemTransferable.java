package net.ck.mtbg.ui.dnd;

import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ItemTransferable implements Transferable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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