package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.components.FurnitureItemPane;
import net.ck.mtbg.ui.components.MapCanvas;
import net.ck.mtbg.ui.components.MapTilePane;
import net.ck.mtbg.ui.components.NPCPane;
import net.ck.mtbg.ui.listeners.MapEditorController;

import javax.swing.*;
import java.awt.*;


/**
 * so how to do it
 * we have a map
 * how to fill it
 * click
 * drag and drop
 * <p>
 * how to fill the list of map type prototypes
 * JLabel with image of tile type
 * Tab list? Scroll list?
 * lets try tab and then jlist per type
 * <p>
 * same for furniture and npcs
 */
@Getter
@Setter
@Log4j2
public class MapEditorFrame extends JFrame
{
    private final MapEditorController mapEditorController;
    private DefaultListModel<ProtoMapTile> protoMapTileList;

    public MapEditorFrame() throws HeadlessException
    {
        protoMapTileList = new DefaultListModel<>();
        for (TileTypes type : TileTypes.values())
        {
            ProtoMapTile protoMapTile = new ProtoMapTile(type);
            protoMapTileList.addElement(protoMapTile);
        }

        this.mapEditorController = new MapEditorController(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setLayout(null);
        this.setVisible(true);
        this.addWindowListener(mapEditorController);

        //canvas for showing the map will need to think about a new version that does not move
        //with player interaction but with scrollbars going to be interesting
        //ok standard canvas is too specialized
        //break up into prototypeMapCanvas
        //or sublcass with basic behaviour
        MapCanvas canvas = new MapCanvas();
        canvas.setBounds(0, 0, 500, 500);
        //list of map entries
        //fill from the list
        MapTilePane mapTilePane = new MapTilePane(protoMapTileList);
        mapTilePane.setBounds(505, 0, 200, 200);
        mapTilePane.setForeground(Color.BLUE);
        mapTilePane.setBackground(Color.BLUE);
        mapTilePane.setVisible(true);


        //list of furniture items?
        FurnitureItemPane furnitureItemPane = new FurnitureItemPane();
        furnitureItemPane.setBounds(505, 205, 200, 200);
        furnitureItemPane.setForeground(Color.YELLOW);
        furnitureItemPane.setBackground(Color.YELLOW);
        furnitureItemPane.setVisible(true);
        //NPCs?
        NPCPane npcPane = new NPCPane();
        npcPane.setBounds(505, 405, 200, 200);
        npcPane.setForeground(Color.CYAN);
        npcPane.setBackground(Color.CYAN);
        npcPane.setVisible(true);

        this.add(npcPane);
        this.add(mapTilePane);
        this.add(canvas);
    }

    public MapEditorFrame(MapEditorController mapEditorController)
    {
        this.mapEditorController = mapEditorController;
    }
}
