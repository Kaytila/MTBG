package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.buttons.LoadButton;
import net.ck.mtbg.ui.buttons.SaveButton;
import net.ck.mtbg.ui.components.FurnitureItemPane;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.ui.components.MapTilePane;
import net.ck.mtbg.ui.components.NPCPane;
import net.ck.mtbg.ui.listeners.MapEditorController;
import net.ck.mtbg.ui.renderers.MapTilePaneListRenderer;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.xml.RunXMLParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


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

    private MapTilePane mapTilePane;
    private MapEditorCanvas canvas;
    private JScrollBar horizontalScrollbar;
    private JScrollBar verticalScrollbar;
    private JScrollPane scrollPane;

    private LoadButton loadButton;
    private SaveButton saveButton;
    private File file;

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


        mapTilePane = new MapTilePane(protoMapTileList);
        mapTilePane.setBounds(505, 0, 200, 200);
        mapTilePane.setForeground(Color.YELLOW);
        mapTilePane.setBackground(Color.BLUE);
        mapTilePane.setVisible(true);
        mapTilePane.setVisibleRowCount(5);
        mapTilePane.setCellRenderer(new MapTilePaneListRenderer());
        mapTilePane.setDragEnabled(true);
        mapTilePane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //mapTilePane.setLayoutOrientation(JList.VERTICAL_WRAP);
        mapTilePane.setVisibleRowCount(10);
        mapTilePane.setAutoscrolls(true);

        //canvas for showing the map will need to think about a new version that does not move
        //with player interaction but with scrollbars going to be interesting
        //ok standard canvas is too specialized
        //break up into prototypeMapCanvas
        //or sublcass with basic behaviour
        canvas = new MapEditorCanvas(mapTilePane);
        canvas.setBounds(0, 0, 500, 500);


        //list of map entries
        //fill from the list


        //list of furniture items?
        FurnitureItemPane furnitureItemPane = new FurnitureItemPane();
        furnitureItemPane.setBounds(505, 205, 200, 200);
        furnitureItemPane.setForeground(Color.BLUE);
        furnitureItemPane.setBackground(Color.YELLOW);
        furnitureItemPane.setVisible(true);
        //NPCs?
        NPCPane npcPane = new NPCPane();
        npcPane.setBounds(505, 405, 200, 200);
        npcPane.setForeground(Color.CYAN);
        npcPane.setBackground(Color.CYAN);
        npcPane.setVisible(true);

        saveButton = new SaveButton(new Point(505, 605));
        loadButton = new LoadButton(new Point(600, 605));
        ActionListener loadActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File start = new File("C:\\Users\\Claus\\eclipse-workspace\\MyTurnBasedGame\\projects\\mtbg\\assets\\maps");
                JFileChooser fileChooser = new JFileChooser(start);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML only", "xml");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(MapEditorFrame.this);
                logger.debug("return value: {}", returnValue);
                file = fileChooser.getSelectedFile();
                logger.debug("file: {}", file);
                Map map = RunXMLParser.parseMap(file.getPath());

                canvas.setMap(map);
                /*verticalScrollbar.setMaximum(map.getSize().x);
                horizontalScrollbar.setMaximum(map.getSize().y);
                verticalScrollbar.repaint();
                horizontalScrollbar.repaint();*/
                canvas.repaint();

            }
        };

        ActionListener saveActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File start = new File("C:\\Users\\Claus\\eclipse-workspace\\MyTurnBasedGame\\projects\\mtbg\\assets\\maps");
                JFileChooser fileChooser = new JFileChooser(start);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML only", "xml");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showSaveDialog(MapEditorFrame.this);
                logger.debug("return value: {}", returnValue);
                File saveFile = fileChooser.getSelectedFile();
                canvas.getMap().setName(saveFile.getName());
                logger.debug("saveFile: {}", saveFile);

                try
                {
                    MapUtils.writeMapToXML(canvas.getMap());
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }

                /*try
                {
                    if (saveFile.createNewFile())
                    {
                        System.out.println("File created: " + saveFile.getName());
                    }
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
                */
            }
        };


        //TODO get file back from load, set file for save.
        saveButton.addActionListener(saveActionListener);
        loadButton.addActionListener(loadActionListener);

        //TODO
        scrollPane = new JScrollPane(canvas, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(canvas);
        scrollPane.setVisible(true);



        /*verticalScrollbar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, canvas.getMap().getSize().x);
        verticalScrollbar.setBounds(485, 0, 15, 480);
        verticalScrollbar.setVisible(true);
        horizontalScrollbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, canvas.getMap().getSize().y);
        horizontalScrollbar.setBounds(0, 485, 480, 15);
        horizontalScrollbar.setVisible(true);
        canvas.add(verticalScrollbar);
        canvas.add(horizontalScrollbar);
        */
        this.add(saveButton);
        this.add(loadButton);
        this.add(npcPane);
        this.add(mapTilePane);
        this.add(furnitureItemPane);
        this.add(canvas);
        this.add(scrollPane);

    }

    public MapEditorFrame(MapEditorController mapEditorController)
    {
        this.mapEditorController = mapEditorController;
    }
}
