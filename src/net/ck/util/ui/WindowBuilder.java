package net.ck.util.ui;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.ui.MainWindow;
import net.ck.game.ui.buttons.*;
import net.ck.game.ui.components.InputField;
import net.ck.game.ui.components.JGridCanvas;
import net.ck.game.ui.components.JWeatherCanvas;
import net.ck.game.ui.components.TextList;
import net.ck.game.ui.dialogs.InventoryDialog;
import net.ck.game.ui.dialogs.StatsDialog;
import net.ck.game.ui.dnd.JGridCanvasDragGestureHandler;
import net.ck.game.ui.dnd.JGridCanvasDropTargetHandler;
import net.ck.game.ui.listeners.MyFocusListener;
import net.ck.game.ui.mainframes.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;

public class WindowBuilder {
    private static final Logger logger = LogManager.getLogger(WindowBuilder.class);

    /**
     * stats Dialog - there will be one dialog only with exchanging JPanels
     */
    private StatsDialog statsDialog;

    /**
     * mainframe
     */
    private JFrame frame;

    /**
     * left part, GRID Canvas
     */
    private JGridCanvas gridCanvas;

    public StatsDialog getStatsDialog() {
        return statsDialog;
    }

    public void setStatsDialog(StatsDialog statsDialog) {
        this.statsDialog = statsDialog;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JGridCanvas getGridCanvas() {
        return gridCanvas;
    }

    public void setGridCanvas(JGridCanvas gridCanvas) {
        this.gridCanvas = gridCanvas;
    }

    public InventoryDialog getInventoryDialog() {
        return inventoryDialog;
    }

    public void setInventoryDialog(InventoryDialog inventoryDialog) {
        this.inventoryDialog = inventoryDialog;
    }

    public TextList getTextArea() {
        return textArea;
    }

    public void setTextArea(TextList textArea) {
        this.textArea = textArea;
    }

    public InputField getTextField() {
        return textField;
    }

    public void setTextField(InputField textField) {
        this.textField = textField;
    }

    public JButton getUndoButton() {
        return undoButton;
    }

    public void setUndoButton(JButton undoButton) {
        this.undoButton = undoButton;
    }

    public JWeatherCanvas getWeatherCanvas() {
        return weatherCanvas;
    }

    public void setWeatherCanvas(JWeatherCanvas weatherCanvas) {
        this.weatherCanvas = weatherCanvas;
    }

    public StopMusicButton getStopMusicButton() {
        return stopMusicButton;
    }

    public void setStopMusicButton(StopMusicButton stopMusicButton) {
        this.stopMusicButton = stopMusicButton;
    }

    public StartMusicButton getStartMusicButton() {
        return startMusicButton;
    }

    public void setStartMusicButton(StartMusicButton startMusicButton) {
        this.startMusicButton = startMusicButton;
    }

    public IncreaseVolumeButton getIncreaseVolumeButton() {
        return increaseVolumeButton;
    }

    public void setIncreaseVolumeButton(IncreaseVolumeButton increaseVolumeButton) {
        this.increaseVolumeButton = increaseVolumeButton;
    }

    public DecreaseVolumeButton getDecreaseVolumeButton() {
        return decreaseVolumeButton;
    }

    public void setDecreaseVolumeButton(DecreaseVolumeButton decreaseVolumeButton) {
        this.decreaseVolumeButton = decreaseVolumeButton;
    }

    /**
     * inventoryDialog
     */
    private InventoryDialog inventoryDialog;

    /**
     * text area is the textlist where all the actions are stored
     */
    private TextList textArea;

    /**
     * shows the last command
     */
    private InputField textField;

    /**
     * undo button for retracting turns
     */
    private JButton undoButton;

    /**
     * weather canvas
     */
    private JWeatherCanvas weatherCanvas;

    /**
     * button for stopping music - will need to move into options menu once music works properly
     */
    private StopMusicButton stopMusicButton;

    /**
     * button for starting music - will need to move into options menu once music works properly
     */
    private StartMusicButton startMusicButton;

    /**
     * button for increasing volume
     */
    private IncreaseVolumeButton increaseVolumeButton;

    /**
     * button for decreasing volume
     */
    private DecreaseVolumeButton decreaseVolumeButton;


    private MainWindow mainWindow;

    public WindowBuilder(MainWindow mW) {
        mainWindow = mW;
    }

    /**
     * in smalltalk fashion, using buildWindow: :D
     * for creating the actual ui
     */
    public void buildWindow() {
        logger.info("start: build window");
        frame = new MainFrame();
        MyFocusListener myFocusListener = new MyFocusListener();
        undoButton = new UndoButton(new Point(GameConfiguration.UIwidth - 300, GameConfiguration.UIheight - 100));
        undoButton.addFocusListener(myFocusListener);
        frame.add(undoButton);

        stopMusicButton = new StopMusicButton(new Point(GameConfiguration.UIwidth - 400, GameConfiguration.UIheight - 100));
        stopMusicButton.addActionListener(mainWindow);
        frame.add(stopMusicButton);

        startMusicButton = new StartMusicButton(new Point(GameConfiguration.UIwidth - 500, GameConfiguration.UIheight - 100));
        startMusicButton.addActionListener(mainWindow);
        frame.add(startMusicButton);

        increaseVolumeButton = new IncreaseVolumeButton(new Point(GameConfiguration.UIwidth - 600, GameConfiguration.UIheight - 100));
        increaseVolumeButton.addActionListener(mainWindow);
        frame.add(increaseVolumeButton);

        decreaseVolumeButton = new DecreaseVolumeButton(new Point(GameConfiguration.UIwidth - 700, GameConfiguration.UIheight - 100));
        decreaseVolumeButton.addActionListener(mainWindow);
        frame.add(decreaseVolumeButton);

        gridCanvas = new JGridCanvas();
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
        frame.addWindowListener(mainWindow);

        gridCanvas.addMouseListener(mainWindow);
        gridCanvas.addMouseMotionListener(mainWindow);
        undoButton.addActionListener(mainWindow);

        DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDragGestureHandler(gridCanvas));
        DropTarget dt = new DropTarget(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDropTargetHandler(gridCanvas), true);
        gridCanvas.setDropTarget(dt);


        EventBus.getDefault().register(mainWindow);
        Game.getCurrent().setController(mainWindow);
        this.getFrame().setVisible(true);
        Game.getCurrent().setUiOpen(true);
        logger.info("finish: build window: UI is open");
    }

}
