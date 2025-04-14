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
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.buttons.EditMapButton;
import net.ck.mtbg.ui.buttons.LoadButton;
import net.ck.mtbg.ui.buttons.SaveButton;
import net.ck.mtbg.ui.components.FurnitureItemPane;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.ui.components.MapTilePane;
import net.ck.mtbg.ui.components.NPCPane;
import net.ck.mtbg.ui.listeners.EditMapButtonActionListener;
import net.ck.mtbg.ui.listeners.LoadButtonActionListener;
import net.ck.mtbg.ui.listeners.MapEditorController;
import net.ck.mtbg.ui.listeners.SaveButtonActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


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
    //private JScrollBar horizontalScrollbar;
    //private JScrollBar verticalScrollbar;
    private JScrollPane scrollPane;
    private NPCPane npcPane;

    private LoadButton loadButton;
    private SaveButton saveButton;

    private JPanel leftPanel;
    private JPanel rightPanel;


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
        this.setPreferredSize(new Dimension(1500, 1000));
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);


        this.addWindowListener(mapEditorController);
        //this.setLayout(null);
        this.pack();
        this.setVisible(true);

        createUI();
    }


    public void createUI()
    {

        canvas = new MapEditorCanvas(mapEditorController);
        canvas.setPreferredSize(new Dimension(GameConfiguration.tileSize * canvas.getMap().getSize().x, GameConfiguration.tileSize * canvas.getMap().getSize().y));
        JSplitPane splitPane;
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.YELLOW);
        leftPanel.setMinimumSize(new Dimension(GameConfiguration.tileSize * 12, GameConfiguration.tileSize * 12));
        leftPanel.setMaximumSize(new Dimension(canvas.getMap().getSize().x * GameConfiguration.tileSize, canvas.getMap().getSize().y * GameConfiguration.tileSize));
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.CYAN);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        //splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(GameConfiguration.tileSize * 12);
        splitPane.setPreferredSize(new Dimension(700, 700));
        this.add(splitPane);
        //list of map entries
        //fill from the list
        mapTilePane = new MapTilePane(protoMapTileList);
        mapTilePane.setPreferredSize(new Dimension(200, 200));
        JScrollPane scrollPane = new JScrollPane(mapTilePane);
        rightPanel.add(scrollPane);

        //canvas for showing the map will need to think about a new version that does not move
        //with player interaction but with scrollbars going to be interesting
        //ok standard canvas is too specialized
        //break up into prototypeMapCanvas
        //or sublcass with basic behaviour


        //list of furniture items?
        furnitureItemPane = new FurnitureItemPane(furnitureItemList);
        furnitureItemPane.setForeground(Color.BLUE);
        furnitureItemPane.setBackground(Color.YELLOW);
        furnitureItemPane.setVisible(true);
        rightPanel.add(furnitureItemPane);

        //NPCs?
        npcPane = new NPCPane(npcList);
        npcPane.setForeground(Color.YELLOW);
        npcPane.setBackground(Color.CYAN);
        npcPane.setVisible(true);
        rightPanel.add(npcPane);

        SaveButtonActionListener saveButtonActionListener = new SaveButtonActionListener(this, canvas);
        LoadButtonActionListener loadButtonActionListener = new LoadButtonActionListener(this, canvas);
        EditMapButtonActionListener editMapButtonActionListener = new EditMapButtonActionListener(this);

        //make sure buttons have their listeners.
        saveButton = new SaveButton(saveButtonActionListener, preferredButtonSize);
        saveButton.setMinimumSize(preferredButtonSize);
        saveButton.setMaximumSize(preferredButtonSize);
        rightPanel.add(saveButton);

        loadButton = new LoadButton(loadButtonActionListener, preferredButtonSize);
        loadButton.setMinimumSize(preferredButtonSize);
        loadButton.setMaximumSize(preferredButtonSize);
        rightPanel.add(loadButton);

        EditMapButton editMapButton = new EditMapButton(editMapButtonActionListener, preferredButtonSize);
        editMapButton.setMinimumSize(preferredButtonSize);
        editMapButton.setMaximumSize(preferredButtonSize);
        rightPanel.add(editMapButton);

        scrollPane = new JScrollPane(canvas);
        scrollPane.setViewportView(canvas);
        scrollPane.setVisible(true);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPanel.add(scrollPane);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBlockIncrement(1);
        verticalScrollBar.setMinimum(0);
        verticalScrollBar.setMaximum(21);

        verticalScrollBar.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                logger.debug("verticalScrollBar mouse event");
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                logger.debug("verticalScrollBar mouse event");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                logger.debug("verticalScrollBar mouseEntered");
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                logger.debug("verticalScrollBar mouseExited");
            }
        });

        verticalScrollBar.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    logger.debug("verticalScrollBar Adjusting");
                }

                // The user clicked or adjusted the scrollbar
                int value = e.getValue();  // Current position of the scrollbar
                logger.debug("verticalScrollBar position: {}", e.getAdjustmentType());

                if (e.getAdjustmentType() == AdjustmentEvent.UNIT_INCREMENT)
                {
                    System.out.println("Up arrow clicked or scrolled down");
                }
                else if (e.getAdjustmentType() == AdjustmentEvent.UNIT_DECREMENT)
                {
                    System.out.println("Down arrow clicked or scrolled up");
                }

                // If needed, increment the viewport (for example, scroll by a fixed amount)
                if (e.getSource() == verticalScrollBar)
                {
                    if (value + 50 < verticalScrollBar.getMaximum())
                    {
                        verticalScrollBar.setValue(value + 50);  // Increment viewport by 50 units
                    }
                }
            }
        });


        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBlockIncrement(1);
        horizontalScrollBar.setMinimum(0);
        horizontalScrollBar.setMaximum(21);

        horizontalScrollBar.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                logger.debug("horizontalScrollBar mouse event");
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                logger.debug("horizontalScrollBar mouse event");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                logger.debug("horizontalScrollBar mouseEntered");
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                logger.debug("horizontalScrollBar mouseExited");
            }
        });

        horizontalScrollBar.addAdjustmentListener(new AdjustmentListener()
        {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    logger.debug("horizontalScrollBar Adjusting");
                }

                // The user clicked or adjusted the scrollbar
                int value = e.getValue();  // Current position of the scrollbar
                logger.debug("horizontalScrollBar position: {}", e.getAdjustmentType());

                if (e.getAdjustmentType() == AdjustmentEvent.UNIT_INCREMENT)
                {
                    System.out.println("Up arrow clicked or scrolled down");
                }
                else if (e.getAdjustmentType() == AdjustmentEvent.UNIT_DECREMENT)
                {
                    System.out.println("Down arrow clicked or scrolled up");
                }

                // If needed, increment the viewport (for example, scroll by a fixed amount)
                if (e.getSource() == horizontalScrollBar)
                {
                    if (value + 50 < horizontalScrollBar.getMaximum())
                    {
                        horizontalScrollBar.setValue(value + 50);  // Increment viewport by 50 units
                    }
                }
            }
        });

       /* c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(leftPanel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        getContentPane().add(rightPanel, c);*/

        /*c.fill = GridBagConstraints.BOTH;
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
        getContentPane().add(loadButton, c);/*

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

        setLocationRelativeTo(null);
    }
}
