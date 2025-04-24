package net.ck.mtbg.util.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.buttons.*;
import net.ck.mtbg.ui.components.*;
import net.ck.mtbg.ui.dialogs.InventoryDialog;
import net.ck.mtbg.ui.dialogs.StatsDialog;
import net.ck.mtbg.ui.dnd.JGridCanvasDragGestureHandler;
import net.ck.mtbg.ui.dnd.JGridCanvasDropTargetHandler;
import net.ck.mtbg.ui.listeners.*;
import net.ck.mtbg.ui.mainframes.CharacterEditorFrame;
import net.ck.mtbg.ui.mainframes.GameFrame;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;
import net.ck.mtbg.ui.mainframes.TitleFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.Component.CENTER_ALIGNMENT;

@Getter
@Setter
@Log4j2
public class WindowBuilder
{
    /**
     * stats Dialog - there will be one dialog only with exchanging JPanels
     */
    @Getter
    @Setter
    private static StatsDialog statsDialog;


    /**
     * mainframe
     */
    @Getter
    @Setter
    private static JFrame frame;


    /**
     * left part, GRID Canvas
     */
    @Getter
    @Setter
    private static MapCanvas gridCanvas;


    /**
     * inventoryDialog
     */
    @Getter
    @Setter
    private static InventoryDialog inventoryDialog;


    /**
     * text area is the textlist where all the actions are stored
     */
    @Getter
    @Setter
    private static TextList textArea;


    /**
     * shows the last command
     */
    @Getter
    @Setter
    private static InputField textField;


    /**
     * undo button for retracting turns
     */
    @Getter
    @Setter
    private static JButton undoButton;


    /**
     * weather canvas
     */
    @Getter
    @Setter
    private static JWeatherCanvas weatherCanvas;


    /**
     * button for stopping music - will need to move into options menu once music works properly
     */
    @Getter
    @Setter
    private static StopMusicButton stopMusicButton;


    /**
     * button for starting music - will need to move into options menu once music works properly
     */
    @Getter
    @Setter
    private static StartMusicButton startMusicButton;


    /**
     * button for increasing volume
     */
    @Getter
    @Setter
    private static IncreaseVolumeButton increaseVolumeButton;


    /**
     * button for decreasing volume
     */
    @Getter
    @Setter
    private static DecreaseVolumeButton decreaseVolumeButton;

    @Getter
    @Setter
    private static SaveButton saveButton;

    @Getter
    @Setter
    private static LoadButton loadButton;


    /**
     * mainWindow is not the mainWindow, this is actually the controller
     */
    @Getter
    @Setter
    private static GameController gameController;

    @Getter
    @Setter
    private static TitleFrame titleFrame;

    @Getter
    @Setter
    private static CharacterEditorFrame characterEditorFrame;

    @Getter
    @Setter
    private static MapEditorFrame mapEditorFrame;

    /**
     * in smalltalk fashion, using buildGameWindow: :D
     * for creating the actual ui
     * <p>
     * as a rule, we have a build method for each UI.
     * Will need to check how often the whole UI is built in the constructor
     * of the frame/dialog.
     */
    public static void buildGameWindow(GameController mW)
    {
        logger.info("start: build window");
        gameController = mW;
        frame = new GameFrame();
        MyFocusListener myFocusListener = new MyFocusListener();
        undoButton = new UndoButton(new Point(GameConfiguration.UIwidth - 300, GameConfiguration.UIheight - 100));
        undoButton.addFocusListener(myFocusListener);
        frame.add(undoButton);

        stopMusicButton = new StopMusicButton(new Point(GameConfiguration.UIwidth - 400, GameConfiguration.UIheight - 100));
        stopMusicButton.addActionListener(gameController);
        frame.add(stopMusicButton);

        startMusicButton = new StartMusicButton(new Point(GameConfiguration.UIwidth - 500, GameConfiguration.UIheight - 100));
        startMusicButton.addActionListener(gameController);
        frame.add(startMusicButton);

        increaseVolumeButton = new IncreaseVolumeButton(new Point(GameConfiguration.UIwidth - 600, GameConfiguration.UIheight - 100));
        increaseVolumeButton.addActionListener(gameController);
        frame.add(increaseVolumeButton);

        decreaseVolumeButton = new DecreaseVolumeButton(new Point(GameConfiguration.UIwidth - 700, GameConfiguration.UIheight - 100));
        decreaseVolumeButton.addActionListener(gameController);
        frame.add(decreaseVolumeButton);

        /*
        save load buttons
         */

        loadButton = new LoadButton(new Point(GameConfiguration.UIwidth - 600, GameConfiguration.UIheight - 170));
        loadButton.addActionListener(gameController);
        frame.add(loadButton);

        saveButton = new SaveButton(new Point(GameConfiguration.UIwidth - 700, GameConfiguration.UIheight - 170));
        saveButton.addActionListener(gameController);
        frame.add(saveButton);


        gridCanvas = new MapCanvas();
        gridCanvas.addFocusListener(myFocusListener);
        frame.add(gridCanvas);

        textArea = new TextList();
        textArea.addFocusListener(myFocusListener);
        frame.add(textArea.initializeScrollPane());

        textField = new InputField();
        textField.addFocusListener(myFocusListener);
        frame.add(textField);

        weatherCanvas = new JWeatherCanvas();
        weatherCanvas.addFocusListener(myFocusListener);
        frame.add(weatherCanvas);

        logger.info("setting listeners");
        frame.addWindowListener(gameController);

        gridCanvas.addMouseListener(gameController);
        gridCanvas.addMouseMotionListener(gameController);
        undoButton.addActionListener(gameController);

        DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDragGestureHandler(gridCanvas));

        DropTarget dt = new DropTarget(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDropTargetHandler(gridCanvas), true);
        gridCanvas.setDropTarget(dt);

        WindowBuilder.setGameController(gameController);
        frame.setVisible(true);

        logger.info("finish: build window: UI is open");
    }

    /**
     * as a rule, we have a build method for each UI.
     * Will need to check how often the whole UI is built in the constructor
     * of the frame/dialog.
     */
    public static void buildCharacterEditor()
    {
        characterEditorFrame = new CharacterEditorFrame();
    }

    /**
     * as a rule, we have a build method for each UI.
     * Will need to check how often the whole UI is built in the constructor
     * of the frame/dialog.
     */
    public static void buildTitleScreen()
    {
        titleFrame = new TitleFrame();
        JPanel content = (JPanel) titleFrame.getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel();
        title.setText(GameConfiguration.titleString);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBounds(30, 30, 100, 50);
        TitleScreenButton characterEditorButton = new TitleScreenButton(100, 30, "Character Editor");
        TitleScreenButton newGameButton = new TitleScreenButton(100, 90, "New Game");
        TitleScreenButton loadGameButton = new TitleScreenButton(100, 150, "Load Game");
        TitleScreenButton creditsButton = new TitleScreenButton(100, 210, "Credits");
        TitleScreenButton optionsButton = new TitleScreenButton(100, 270, "Options");
        TitleScreenButton mapEditorButton = new TitleScreenButton(100, 330, "Map Editor");
        //TODO
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(title);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(characterEditorButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(newGameButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(loadGameButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(creditsButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(optionsButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.add(mapEditorButton);
        content.add(Box.createVerticalStrut(GameConfiguration.verticalTitleSpacer));
        content.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * as a rule, we have a build method for each UI.
     * Will need to check how often the whole UI is built in the constructor
     * of the frame/dialog.
     */
    public static void buildMapEditor()
    {
        mapEditorFrame = new MapEditorFrame();
    }

    public static void createMapEditorUI(MapEditorFrame mapEditorFrame)
    {

        JPanel leftPanel = new JPanel();
        //leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.setBackground(Color.YELLOW);
        //setting this for minimum size on the left panel will lead to this being the minimum size of the left panel??
        //why not of the children?
        //leftPanel.setMinimumSize(new Dimension(GameConfiguration.tileSize * 12, GameConfiguration.tileSize * 12));
        //leftPanel.setMaximumSize(new Dimension(MapEditorApplication.getCurrent().getMap().getSize().x * GameConfiguration.tileSize, MapEditorApplication.getCurrent().getMap().getSize().y * GameConfiguration.tileSize));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.CYAN);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerLocation(GameConfiguration.tileSize * 12);
        splitPane.setPreferredSize(new Dimension(700, 700));
        mapEditorFrame.add(splitPane);

        logger.debug("building the right side of the map editor");
        //list of map entries
        //fill from the list
        MapTilePane mapTilePane = new MapTilePane(MapEditorApplication.getCurrent().getProtoMapTileList());

        JScrollPane scrollPane = new JScrollPane(mapTilePane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        rightPanel.add(scrollPane);

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
        logger.debug("building the left side of the map editor");
        MapEditorCanvas mapEditorCanvas = new MapEditorCanvas();
        mapEditorCanvas.setPreferredSize(new Dimension(GameConfiguration.tileSize * MapEditorApplication.getCurrent().getMap().getSize().x, GameConfiguration.tileSize * MapEditorApplication.getCurrent().getMap().getSize().y));

        JScrollPane scrollPaneCanvas = new JScrollPane(mapEditorCanvas);
        scrollPaneCanvas.setViewportView(mapEditorCanvas);
        scrollPaneCanvas.setVisible(true);
        scrollPaneCanvas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneCanvas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneCanvas.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPaneCanvas.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        leftPanel.add(scrollPaneCanvas);

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

        MapEditorController.getCurrent().setMapTilePane(mapTilePane);
        MapEditorController.getCurrent().setNpcPane(npcPane);
        MapEditorController.getCurrent().setFurnitureItemPane(furnitureItemPane);
        MapEditorController.getCurrent().setMapEditorFrame(mapEditorFrame);
        MapEditorController.getCurrent().setLoadButton(loadButton);
        MapEditorController.getCurrent().setSaveButton(saveButton);
        MapEditorController.getCurrent().setEditMapButton(editMapButton);
        MapEditorController.getCurrent().setMapEditorCanvas(mapEditorCanvas);
        MapEditorController.getCurrent().setLeftPanel(leftPanel);
        MapEditorController.getCurrent().setRightPanel(rightPanel);
        MapEditorController.getCurrent().setSplitPane(splitPane);

    }
}
