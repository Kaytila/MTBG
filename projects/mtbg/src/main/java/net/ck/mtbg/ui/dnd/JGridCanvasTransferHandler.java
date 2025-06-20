package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.game.MapCanvas;
import net.ck.mtbg.ui.controllers.GameController;
import net.ck.mtbg.util.communication.keyboard.gameactions.DropAction;
import net.ck.mtbg.util.utils.MapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

@Getter
@Setter
@Log4j2
@ToString
public class JGridCanvasTransferHandler extends TransferHandler
{

    private MapTile sourceMapTile = null;
    private MapTile targetMapTile = null;
    private MapCanvas gridCanvas;

    public JGridCanvasTransferHandler(MapCanvas gridCanvas)
    {
        setGridCanvas(gridCanvas);
    }


    @Override
    public Image getDragImage()
    {
        return super.getDragImage();
    }

    @Override
    public void setDragImage(Image img)
    {
        super.setDragImage(img);
    }

    @Override
    public Point getDragImageOffset()
    {
        return super.getDragImageOffset();
    }

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action)
    {
        super.exportAsDrag(comp, e, action);
    }

    @Override
    public boolean importData(TransferSupport support)
    {
        GameController.getCurrent().setCurrentAction(new DropAction());
        int x = MouseInfo.getPointerInfo().getLocation().x - getGridCanvas().getLocationOnScreen().x;
        int y = MouseInfo.getPointerInfo().getLocation().y - getGridCanvas().getLocationOnScreen().y;
        logger.info("mouse position: {}", new Point(x, y));
        targetMapTile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
        AbstractItem item = null;
        try
        {
            item = (AbstractItem) support.getTransferable().getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        targetMapTile.getInventory().add(item);
        return true;
    }

    @Override
    public boolean canImport(TransferSupport support)
    {
        if (GameController.getCurrent().getCurrentAction() != null)
        {
            return false;
        }
        MapTile tile = MapUtils.calculateMapTileUnderCursor(support.getDropLocation().getDropPoint());
        if (tile.isBlocked())
        {
            logger.info("tile is blocked, return false");
            return false;
        }
        return super.canImport(support);
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
    {
        return super.canImport(comp, transferFlavors);
    }

    @Override
    public int getSourceActions(JComponent c)
    {
        logger.info("get source actions");
        return TransferHandler.MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c)
    {
        int x = MouseInfo.getPointerInfo().getLocation().x - getGridCanvas().getLocationOnScreen().x;
        int y = MouseInfo.getPointerInfo().getLocation().y - getGridCanvas().getLocationOnScreen().y;
        logger.info("mouse position: {}", new Point(x, y));
        sourceMapTile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
        if (sourceMapTile.getInventory().getSize() > 0)
        {
            setDragImage(sourceMapTile.getInventory().get(0).getItemImage());
            return new ItemTransferable(sourceMapTile.getInventory().get(0));
        }
        return null;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action)
    {
        AbstractItem item = null;
        try
        {
            item = (AbstractItem) data.getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        sourceMapTile.getInventory().remove(item);
    }
}
