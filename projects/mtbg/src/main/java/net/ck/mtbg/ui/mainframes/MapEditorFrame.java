package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.backend.state.NPCManager;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.items.FurnitureItem;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
    private DefaultListModel<FurnitureItem> furnitureItemList;
    private DefaultListModel<NPC> npcList;

    private MapTilePane mapTilePane;
    private MapEditorCanvas canvas;
    private FurnitureItemPane furnitureItemPane;
    private JScrollBar horizontalScrollbar;
    private JScrollBar verticalScrollbar;
    private JScrollPane scrollPane;
    private NPCPane npcPane;

    private LoadButton loadButton;
    private SaveButton saveButton;
    private File file;

    private Dimension preferredButtonSize = new Dimension(70, 30);

    public MapEditorFrame() throws HeadlessException
    {
        protoMapTileList = new DefaultListModel<>();
        for (TileTypes type : TileTypes.values())
        {
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


        this.setTitle("MTBG - Map Editor");
        this.mapEditorController = new MapEditorController(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        this.setPreferredSize(new Dimension(800, 800));
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);


        this.addWindowListener(mapEditorController);


        GridBagLayout gridBagLayout = new GridBagLayout();
        getContentPane().setLayout(gridBagLayout);

        this.pack();
        this.setVisible(true);

        createUI();
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
                mapEditorFrame.pack();
                mapEditorFrame.setVisible(true);
            }
        });
    }

    public void createUI()
    {
        GridBagConstraints c;


        //list of map entries
        //fill from the list
        mapTilePane = new MapTilePane(protoMapTileList);
        mapTilePane.setPreferredSize(new Dimension(200, 200));
        mapTilePane.setForeground(Color.YELLOW);
        mapTilePane.setBackground(Color.BLUE);
        mapTilePane.setVisible(true);
        mapTilePane.setCellRenderer(new MapTilePaneListRenderer());
        mapTilePane.setDragEnabled(true);
        mapTilePane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mapTilePane.setVisibleRowCount(5);
        mapTilePane.setAutoscrolls(true);

        //canvas for showing the map will need to think about a new version that does not move
        //with player interaction but with scrollbars going to be interesting
        //ok standard canvas is too specialized
        //break up into prototypeMapCanvas
        //or sublcass with basic behaviour
        canvas = new MapEditorCanvas(mapEditorController);
        //canvas.setPreferredSize(new Dimension(GameConfiguration.tileSize * 12, GameConfiguration.tileSize * 12));


        //list of furniture items?
        furnitureItemPane = new FurnitureItemPane(furnitureItemList);
        furnitureItemPane.setForeground(Color.BLUE);
        furnitureItemPane.setBackground(Color.YELLOW);
        furnitureItemPane.setVisible(true);

        //NPCs?
        npcPane = new NPCPane(npcList);
        npcPane.setForeground(Color.YELLOW);
        npcPane.setBackground(Color.CYAN);
        npcPane.setVisible(true);

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
                if (file != null)
                {
                    logger.debug("file: {}", file);
                    Map map = RunXMLParser.parseMap(file.getPath());

                    canvas.setMap(map);
                    canvas.setMinimumSize(new Dimension(map.getSize().x * GameConfiguration.tileSize, map.getSize().y * GameConfiguration.tileSize));
                    MapEditorFrame.this.setPreferredSize(new Dimension(MapEditorFrame.this.getWidth() + (map.getSize().x * GameConfiguration.tileSize), MapEditorFrame.this.getHeight() + (map.getSize().y * GameConfiguration.tileSize)));
                    canvas.repaint();
                }
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

        //make sure buttons have their listeners.
        saveButton = new SaveButton(saveActionListener, preferredButtonSize);
        saveButton.setMinimumSize(preferredButtonSize);
        saveButton.setMaximumSize(preferredButtonSize);

        loadButton = new LoadButton(loadActionListener, preferredButtonSize);
        loadButton.setMinimumSize(preferredButtonSize);
        loadButton.setMaximumSize(preferredButtonSize);


        scrollPane = new JScrollPane(canvas);
        scrollPane.setViewportView(canvas);
        scrollPane.setVisible(true);


        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 5;
        c.gridwidth = 1;
        getContentPane().add(scrollPane, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(mapTilePane, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(npcPane, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(furnitureItemPane, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(saveButton, c);


        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(loadButton, c);

        /**
         * just for debugging options opening will start the break point
         */
        this.addWindowListener(new WindowListener()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {

            }

            @Override
            public void windowOpened(WindowEvent e)
            {
                logger.debug("test");
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
            }

        });


        mapEditorController.setMapTilePane(mapTilePane);
        mapEditorController.setNpcPane(npcPane);
        mapEditorController.setFurnitureItemPane(furnitureItemPane);
    }
}
