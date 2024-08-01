package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.AbstractItem;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

@Getter
@Setter
@Log4j2
@ToString
public class ItemTransferable implements Transferable
{

    public static final DataFlavor ITEM_DATA_FLAVOR = new DataFlavor(AbstractItem.class, "Item");
    private AbstractItem item;

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