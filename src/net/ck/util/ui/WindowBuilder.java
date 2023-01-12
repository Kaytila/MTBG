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
    private static StatsDialog statsDialog;

    /**
     * mainframe
     */
    private static JFrame frame;

    /**
     * left part, GRID Canvas
     */
    private static JGridCanvas gridCanvas;

    /**
     * inventoryDialog
     */
    private static InventoryDialog inventoryDialog;

    /**
     * text area is the textlist where all the actions are stored
     */
    private static TextList textArea;

    /**
     * shows the last command
     */
    private static InputField textField;

    /**
     * undo button for retracting turns
     */
    private static JButton undoButton;

    /**
     * weather canvas
     */
    private static JWeatherCanvas weatherCanvas;

    /**
     * button for stopping music - will need to move into options menu once music works properly
     */
    private static StopMusicButton stopMusicButton;

    /**
     * button for starting music - will need to move into options menu once music works properly
     */
    private static StartMusicButton startMusicButton;

    /**
     * button for increasing volume
     */
    private static IncreaseVolumeButton increaseVolumeButton;

    /**
     * button for decreasing volume
     */
    private static DecreaseVolumeButton decreaseVolumeButton;


    private static MainWindow mainWindow;

    public static StatsDialog getStatsDialog() {
        return statsDialog;
    }

    public static void setStatsDialog(StatsDialog statsDialog) {
        WindowBuilder.statsDialog = statsDialog;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JFrame frame) {
        WindowBuilder.frame = frame;
    }

    public static JGridCanvas getGridCanvas() {
        return gridCanvas;
    }

    public static void setGridCanvas(JGridCanvas gridCanvas) {
        WindowBuilder.gridCanvas = gridCanvas;
    }

    public static InventoryDialog getInventoryDialog() {
        return inventoryDialog;
    }

    public static void setInventoryDialog(InventoryDialog inventoryDialog) {
        WindowBuilder.inventoryDialog = inventoryDialog;
    }

    public static TextList getTextArea() {
        return textArea;
    }

    public static void setTextArea(TextList textArea) {
        WindowBuilder.textArea = textArea;
    }

    public static InputField getTextField() {
        return textField;
    }

    public static void setTextField(InputField textField) {
        WindowBuilder.textField = textField;
    }

    public static JButton getUndoButton() {
        return undoButton;
    }

    public static void setUndoButton(JButton undoButton) {
        WindowBuilder.undoButton = undoButton;
    }

    public static JWeatherCanvas getWeatherCanvas() {
        return weatherCanvas;
    }

    public static void setWeatherCanvas(JWeatherCanvas weatherCanvas) {
        WindowBuilder.weatherCanvas = weatherCanvas;
    }

    public static StopMusicButton getStopMusicButton() {
        return stopMusicButton;
    }

    public static void setStopMusicButton(StopMusicButton stopMusicButton) {
        WindowBuilder.stopMusicButton = stopMusicButton;
    }

    public static StartMusicButton getStartMusicButton() {
        return startMusicButton;
    }

    public static void setStartMusicButton(StartMusicButton startMusicButton) {
        WindowBuilder.startMusicButton = startMusicButton;
    }

    public static IncreaseVolumeButton getIncreaseVolumeButton() {
        return increaseVolumeButton;
    }

    public static void setIncreaseVolumeButton(IncreaseVolumeButton increaseVolumeButton) {
        WindowBuilder.increaseVolumeButton = increaseVolumeButton;
    }

    public static DecreaseVolumeButton getDecreaseVolumeButton() {
        return decreaseVolumeButton;
    }

    public static void setDecreaseVolumeButton(DecreaseVolumeButton decreaseVolumeButton) {
        WindowBuilder.decreaseVolumeButton = decreaseVolumeButton;
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    public static void setMainWindow(MainWindow mainWindow) {
        WindowBuilder.mainWindow = mainWindow;
    }

    /**
     * in smalltalk fashion, using buildWindow: :D
     * for creating the actual ui
     */
    public static void buildWindow(MainWindow mW) {
        logger.info("start: build window");
        mainWindow = mW;
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

        Game.getCurrent().setController(mainWindow);
        frame.setVisible(true);
        Game.getCurrent().setUiOpen(true);
        logger.info("finish: build window: UI is open");
    }

}
