package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.game.MapCanvas;
import net.ck.mtbg.ui.controllers.GameController;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;
import net.ck.mtbg.util.communication.keyboard.gameactions.DropAction;
import net.ck.mtbg.util.utils.MapUtils;

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

    private MapCanvas gridcanvas;

    public JGridCanvasDropTargetHandler(MapCanvas gridCanvas)
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
        GameController.getCurrent().setCurrentAction(new DropAction());
        if (!(GameController.getCurrent().getCurrentAction().getType().equals(KeyboardActionType.DROP)))
        {
            logger.info("current action: {}", GameController.getCurrent().getCurrentAction());
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
