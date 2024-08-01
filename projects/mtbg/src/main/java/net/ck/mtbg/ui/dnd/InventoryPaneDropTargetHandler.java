package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.ui.components.InventoryPane;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

@Getter
@Setter
@Log4j2
@ToString
public class InventoryPaneDropTargetHandler implements DropTargetListener
{


    public InventoryPaneDropTargetHandler(InventoryPane inventoryPane)
    {
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
            Transferable t = dtde.getTransferable();
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
