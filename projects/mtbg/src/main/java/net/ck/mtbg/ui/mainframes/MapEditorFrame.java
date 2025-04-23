package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
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


    public MapEditorFrame() throws HeadlessException
    {
        this.setTitle("MTBG - Map Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        this.setPreferredSize(new Dimension(1500, 1000));
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);


        this.addWindowListener(MapEditorController.getCurrent());
        //this.setLayout(null);
        this.pack();
        this.setVisible(true);

        createUI();
    }


    public void createUI()
    {
        MapEditorCanvas mapEditorCanvas = new MapEditorCanvas();
        mapEditorCanvas.setPreferredSize(new Dimension(GameConfiguration.tileSize * MapEditorApplication.getCurrent().getMap().getSize().x, GameConfiguration.tileSize * MapEditorApplication.getCurrent().getMap().getSize().y));
        JSplitPane splitPane;
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.YELLOW);
        leftPanel.setMinimumSize(new Dimension(GameConfiguration.tileSize * 12, GameConfiguration.tileSize * 12));
        leftPanel.setMaximumSize(new Dimension(MapEditorApplication.getCurrent().getMap().getSize().x * GameConfiguration.tileSize, MapEditorApplication.getCurrent().getMap().getSize().y * GameConfiguration.tileSize));
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.CYAN);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        //splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(GameConfiguration.tileSize * 12);
        splitPane.setPreferredSize(new Dimension(700, 700));
        this.add(splitPane);
        //list of map entries
        //fill from the list
        MapTilePane mapTilePane = new MapTilePane(MapEditorApplication.getCurrent().getProtoMapTileList());
        //mapTilePane.setPreferredSize(new Dimension(200, 200));
        JScrollPane scrollPane = new JScrollPane(mapTilePane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        rightPanel.add(scrollPane);

        //canvas for showing the map will need to think about a new version that does not move
        //with player interaction but with scrollbars going to be interesting
        //ok standard canvas is too specialized
        //break up into prototypeMapCanvas
        //or sublcass with basic behaviour


        //list of furniture items?
        FurnitureItemPane furnitureItemPane = new FurnitureItemPane(MapEditorApplication.getCurrent().getFurnitureItemList());
        furnitureItemPane.setForeground(Color.BLUE);
        furnitureItemPane.setBackground(Color.YELLOW);
        JScrollPane scrollPaneItems = new JScrollPane(furnitureItemPane);
        scrollPaneItems.setPreferredSize(new Dimension(200, 200));
        rightPanel.add(scrollPaneItems);

        //NPCs?
        NPCPane npcPane = new NPCPane(MapEditorApplication.getCurrent().getNpcList());
        npcPane.setForeground(Color.YELLOW);
        npcPane.setBackground(Color.CYAN);
        JScrollPane scrollPaneNPCs = new JScrollPane(npcPane);
        scrollPaneNPCs.setPreferredSize(new Dimension(200, 200));
        rightPanel.add(scrollPaneNPCs);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        SaveButtonActionListener saveButtonActionListener = new SaveButtonActionListener();
        LoadButtonActionListener loadButtonActionListener = new LoadButtonActionListener();
        EditMapButtonActionListener editMapButtonActionListener = new EditMapButtonActionListener();

        //make sure buttons have their listeners.
        SaveButton saveButton = new SaveButton(saveButtonActionListener);
        buttonPanel.add(saveButton);

        LoadButton loadButton = new LoadButton(loadButtonActionListener);
        buttonPanel.add(loadButton);

        EditMapButton editMapButton = new EditMapButton(editMapButtonActionListener);
        buttonPanel.add(editMapButton);

        rightPanel.add(buttonPanel);
        scrollPane = new JScrollPane(mapEditorCanvas);
        scrollPane.setViewportView(mapEditorCanvas);
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


        MapEditorController.getCurrent().setMapTilePane(mapTilePane);
        MapEditorController.getCurrent().setNpcPane(npcPane);
        MapEditorController.getCurrent().setFurnitureItemPane(furnitureItemPane);
        MapEditorController.getCurrent().setMapEditorFrame(this);
        MapEditorController.getCurrent().setLoadButton(loadButton);
        MapEditorController.getCurrent().setSaveButton(saveButton);
        MapEditorController.getCurrent().setEditMapButton(editMapButton);
        MapEditorController.getCurrent().setMapEditorCanvas(mapEditorCanvas);
        MapEditorController.getCurrent().setLeftPanel(leftPanel);
        MapEditorController.getCurrent().setRightPanel(rightPanel);
        MapEditorController.getCurrent().setSplitPane(splitPane);
        setLocationRelativeTo(null);
    }
}
