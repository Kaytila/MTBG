package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCFactory;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.util.utils.MapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;

@Log4j2
@Getter
@Setter
public class MapEditorCanvasListener implements MouseListener, MouseMotionListener, FocusListener
{
    MapEditorCanvas canvas;
    MapEditorController mapEditorController;

    public MapEditorCanvasListener(MapEditorCanvas canvas, MapEditorController controller)
    {
        this.mapEditorController = controller;
        this.canvas = canvas;
    }

    @Override
    public void focusGained(FocusEvent e)
    {

    }

    @Override
    public void focusLost(FocusEvent e)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (e.getClickCount() == 1)
            {
                if (mapEditorController.getMapTilePane().getSelectedIndex() != -1)
                {
                    logger.info("relative: {}", e.getPoint());
                    Point uiCoordinate = MapUtils.getUICoordinateUnderCursor(e.getPoint());

                    //TODO here: switch based on selection in controller.

                    if (Selection.getSelectedItem() == "ProtoMapTile")
                    {
                        ProtoMapTile protoMapTile = mapEditorController.getMapTilePane().getModel().getElementAt(mapEditorController.getMapTilePane().getSelectedIndex());
                        if (protoMapTile != null)
                        {
                            MapTile mapTile = new MapTile();
                            mapTile.setMapPosition(new Point(uiCoordinate.x, uiCoordinate.y));
                            mapTile.setX(uiCoordinate.x);
                            mapTile.setY(uiCoordinate.y);
                            mapTile.setType(protoMapTile.getType());

                            getCanvas().getMap().mapTiles[mapTile.getMapPosition().x][mapTile.getMapPosition().y] = mapTile;
                            logger.debug("maptile: {}", mapTile);
                        }
                    }
                    else if (Selection.getSelectedItem() == "NPC")
                    {
                        NPC npc = NPCFactory.createNPC(mapEditorController.getNpcPane().getModel().getElementAt(mapEditorController.getNpcPane().getSelectedIndex()).getId());
                        if (npc != null)
                        {
                            logger.debug("npc: {}", npc);
                            MapTile mapTile = MapUtils.getMapTileByCoordinates(getCanvas().getMap(), uiCoordinate.x, uiCoordinate.y);
                            npc.setMapPosition(uiCoordinate.x, uiCoordinate.y);
                            mapTile.setLifeForm(npc);
                            canvas.getMap().mapTiles[mapTile.getMapPosition().x][mapTile.getMapPosition().y] = mapTile;
                            logger.debug("maptile: {}", mapTile);
                        }
                    }
                    else if (Selection.getSelectedItem() == "FurnitureItem")
                    {
                        FurnitureItem furnitureItem = mapEditorController.getFurnitureItemPane().getModel().getElementAt(mapEditorController.getFurnitureItemPane().getSelectedIndex());
                        if (furnitureItem != null)
                        {
                            logger.debug("furniture item: {}", furnitureItem);
                            MapTile mapTile = MapUtils.getMapTileByCoordinates(getCanvas().getMap(), uiCoordinate.x, uiCoordinate.y);
                            mapTile.setFurniture(furnitureItem);
                            //canvas.getMap().mapTiles[mapTile.getMapPosition().x][mapTile.getMapPosition().y] = mapTile;
                            logger.debug("maptile: {}", mapTile);
                        }
                    }
                    logger.info("done adding field");
                    canvas.repaint();
                }
            }
            if (e.getClickCount() == 2)
            {
                logger.info("double click in canvas do nothing");
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (canvas.isDragEnabled())
            {
                final TransferHandler transferHandler = canvas.getTransferHandler();
                if (transferHandler != null)
                {
                    // TODO here could be more "logic" to initiate the drag
                    transferHandler.exportAsDrag(canvas, e, DnDConstants.ACTION_MOVE);
                }
                else
                {
                    logger.info("hmmm");
                }
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
