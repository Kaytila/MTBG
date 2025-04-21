package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCFactory;
import net.ck.mtbg.graphics.TileTypes;
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
    MapTile selectedTile;

    public MapEditorCanvasListener(MapEditorCanvas canvas)
    {
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
            Point uiCoordinate = MapUtils.getUICoordinateUnderCursor(e.getPoint());
            if (e.getClickCount() == 1)
            {
                if (MapEditorController.getCurrent().getMapTilePane().getSelectedIndex() != -1)
                {
                    logger.info("relative: {}", e.getPoint());
                    //TODO here: switch based on selection in controller.

                    if (Selection.getSelectedItem() == "ProtoMapTile")
                    {
                        ProtoMapTile protoMapTile = MapEditorController.getCurrent().getMapTilePane().getModel().getElementAt(MapEditorController.getCurrent().getMapTilePane().getSelectedIndex());
                        if (protoMapTile != null)
                        {
                            MapTile mapTile = new MapTile();
                            mapTile.setMapPosition(new Point(uiCoordinate.x, uiCoordinate.y));
                            mapTile.setX(uiCoordinate.x);
                            mapTile.setY(uiCoordinate.y);
                            mapTile.setType(protoMapTile.getType());

                            MapEditorApplication.getCurrent().getMap().mapTiles[mapTile.getMapPosition().x][mapTile.getMapPosition().y] = mapTile;
                            logger.debug("maptile: {}", mapTile);
                        }
                    }
                    else if (Selection.getSelectedItem() == "NPC")
                    {
                        NPC npc = NPCFactory.createNPC(MapEditorController.getCurrent().getNpcPane().getModel().getElementAt(MapEditorController.getCurrent().getNpcPane().getSelectedIndex()).getId());
                        if (npc != null)
                        {
                            logger.debug("npc: {}", npc);
                            MapTile mapTile = MapUtils.getMapTileByCoordinates(MapEditorApplication.getCurrent().getMap(), uiCoordinate.x, uiCoordinate.y);
                            npc.setMapPosition(uiCoordinate.x, uiCoordinate.y);
                            mapTile.setLifeForm(npc);
                            MapEditorApplication.getCurrent().getMap().mapTiles[mapTile.getMapPosition().x][mapTile.getMapPosition().y] = mapTile;
                            logger.debug("maptile: {}", mapTile);
                        }
                    }
                    else if (Selection.getSelectedItem() == "FurnitureItem")
                    {
                        FurnitureItem furnitureItem = MapEditorController.getCurrent().getFurnitureItemPane().getModel().getElementAt(MapEditorController.getCurrent().getFurnitureItemPane().getSelectedIndex());
                        if (furnitureItem != null)
                        {
                            logger.debug("furniture item: {}", furnitureItem);
                            MapTile mapTile = MapUtils.getMapTileByCoordinates(MapEditorApplication.getCurrent().getMap(), uiCoordinate.x, uiCoordinate.y);
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
                MapTile mapTile = MapUtils.getMapTileByCoordinates(MapEditorApplication.getCurrent().getMap(), uiCoordinate.x, uiCoordinate.y);
                logger.debug("double click in canvas on tile: {}", mapTile);
                getCanvas().setSelectedTile(mapTile);
                getCanvas().repaint();
            }
        }

        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (getCanvas().getSelectedTile() != null)
            {
                //TODO add context menu here
                logger.debug("open context menu");
                JPopupMenu popupMenu = new JPopupMenu();
                ActionListener actionListener = new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        logger.debug("source: {}", e.getSource());
                        JMenuItem source = (JMenuItem) (e.getSource());
                        if (source.getText().equals("remove npc"))
                        {
                            logger.debug("removing npc");
                            getCanvas().getSelectedTile().setLifeForm(null);

                        }
                        else if (source.getText().equals("remove furniture"))
                        {
                            logger.debug("removing furniture");
                            getCanvas().getSelectedTile().setFurniture(null);
                        }
                        // now it must be a tile type
                        else
                        {
                            logger.debug("source: {}", source.getText());
                            getCanvas().getSelectedTile().setType(TileTypes.valueOf(source.getText()));
                        }
                        getCanvas().repaint();
                    }
                };
                JMenu menuItem1 = new JMenu("change type");

                for (TileTypes type : TileTypes.values())
                {
                    JMenuItem menuItem = new JMenuItem(type.name());
                    menuItem.addActionListener(actionListener);
                    menuItem1.add(menuItem);

                }

                menuItem1.addActionListener(actionListener);
                JMenuItem menuItem2 = new JMenuItem("remove npc");
                menuItem2.addActionListener(actionListener);
                JMenuItem menuItem3 = new JMenuItem("remove furniture");
                menuItem3.addActionListener(actionListener);
                popupMenu.add(menuItem1);
                popupMenu.add(menuItem2);
                popupMenu.add(menuItem3);

                if (e.isPopupTrigger())
                {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            else
            {
                logger.debug("sorry, no context menu without selected tile");
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
