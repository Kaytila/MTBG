package net.ck.mtbg.backend.applications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.backend.state.NPCManager;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.controllers.MapEditorController;
import net.ck.mtbg.ui.mainframes.mapeditor.MapEditorFrame;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.xml.RunXMLParser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Log4j2
@Getter
@Setter
public class MapEditorApplication
{

    /**
     * Singleton
     */
    private static final MapEditorApplication mapEditor = new MapEditorApplication();

    /**
     * the currently edited map
     */
    private Map map;

    /**
     * the list of possible maptiles
     */
    private DefaultListModel<ProtoMapTile> protoMapTileList;

    /**
     * the list of furnutire items
     */
    private DefaultListModel<FurnitureItem> furnitureItemList;

    /**
     * list of NPCs
     */
    private DefaultListModel<NPC> npcList;

    /**
     *
     */
    private Point visibleTiles;


    public MapEditorApplication()
    {
        map = new Map();
        map.setSize(new Point(13, 13));
        MapTile[][] mapTiles = new MapTile[12][12];
        map.setMapTiles(mapTiles);

        protoMapTileList = new DefaultListModel<>();
        for (TileTypes type : TileTypes.values())
        {
            logger.debug("tile type: {}", type);
            ProtoMapTile protoMapTile = new ProtoMapTile(type);
            protoMapTileList.addElement(protoMapTile);
        }

        furnitureItemList = new DefaultListModel<>();
        for (FurnitureItem fi : ItemManager.getFurnitureList().values())
        {
            furnitureItemList.addElement(fi);
        }

        npcList = new DefaultListModel<>();
        for (NPC fi : NPCManager.getNpcList().values())
        {
            npcList.addElement(fi);
        }

    }

    /**
     * Singleton access - now I can take away game in a lot of things :D
     */
    public static MapEditorApplication getCurrent()
    {
        return mapEditor;
    }

    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MapEditorFrame mapEditorFrame = new MapEditorFrame();
                MapEditorController.getCurrent().setMapEditorFrame(mapEditorFrame);
                mapEditorFrame.pack();
                mapEditorFrame.setVisible(true);
            }
        });
    }

    public boolean saveFile(File saveFile)
    {
        MapEditorApplication.getCurrent().getMap().setName(saveFile.getName());
        logger.debug("saveFile: {}", saveFile);

        try
        {
            MapUtils.writeMapToXML(MapEditorApplication.getCurrent().getMap());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public boolean loadFile(File file)
    {
        if (file != null)
        {
            logger.debug("file: {}", file);
            Map map = RunXMLParser.parseMap(file.getPath());

            MapEditorApplication.getCurrent().setMap(map);
            return true;
        }
        return false;
    }
}
