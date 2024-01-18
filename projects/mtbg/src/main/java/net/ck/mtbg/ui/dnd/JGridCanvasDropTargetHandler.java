package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.JGridCanvas;
import net.ck.mtbg.util.MapUtils;
import net.ck.mtbg.util.communication.keyboard.DropAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import net.ck.mtbg.util.ui.WindowBuilder;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

@Log4j2
@Getter
@Setter
public class JGridCanvasDropTargetHandler implements DropTargetListener
{

    private JGridCanvas gridcanvas;

    public JGridCanvasDropTargetHandler(JGridCanvas gridCanvas)
    {
        gridcanvas = gridCanvas;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {
        // logger.info("dragEnter");

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {
        //logger.info("dragOver: {}", dtde.getLocation());
        MapTile tile = MapUtils.calculateMapTileUnderCursor(dtde.getLocation());
        if (Game.getCurrent().getCurrentPlayer().getMapPosition().equals(tile.getMapPosition()))
        {
            dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        }
        else
        {
            //TODO broken somewhere - why is that again?
            if (tile.isBlocked() && (!(tile.hasInventory())))
            {
                dtde.rejectDrag();
            }
            else
            {
                dtde.acceptDrag(DnDConstants.ACTION_MOVE);
            }
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
        // logger.info("dropActionChanged");

    }

    @Override
    public void dragExit(DropTargetEvent dte)
    {
        // logger.info("dragExit");

    }

    /**
     * https://stackoverflow.com/questions/29187546/java-get-mouse-coordinates-within-window helped with the mouse locations as I am forgetful and dont actually want to remember things like that.
     */
    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        logger.info("drop");
        WindowBuilder.getController().setCurrentAction(new DropAction());
        if (!(WindowBuilder.getController().getCurrentAction().getType().equals(KeyboardActionType.DROP)))
        {
            logger.info("current action: {}", WindowBuilder.getController().getCurrentAction());
            return;
        }
        else
        {
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
                            logger.info("item: {}", item);
                            int x = MouseInfo.getPointerInfo().getLocation().x - gridcanvas.getLocationOnScreen().x;
                            int y = MouseInfo.getPointerInfo().getLocation().y - gridcanvas.getLocationOnScreen().y;
                            logger.info("mouse position: {}", new Point(x, y));
                            MapTile tile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
                            logger.info("map tile: {}", tile);
                            if (tile.isHidden())
                            {
                                logger.info("tile {} is not visible right now", tile);
                                return;
                            }
                            if (Game.getCurrent().getCurrentPlayer().getMapPosition().equals(tile.getMapPosition()))
                            {
                                logger.info("dragging on player");
                                Game.getCurrent().getCurrentPlayer().getInventory().add(item);
                            }
                            else
                            {
                                //TODO
                                if (tile.isBlocked() && (!(tile.hasInventory())))
                                {
                                    logger.info("tile is blocked, no drop");
                                    dtde.rejectDrop();
                                }
                                else
                                {
                                    logger.info("tile is available, drop");
                                    tile.getInventory().add(item);
                                }
                            }
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
}
