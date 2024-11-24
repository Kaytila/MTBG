package net.ck.mtbg.util.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.buttons.*;
import net.ck.mtbg.ui.components.InputField;
import net.ck.mtbg.ui.components.JWeatherCanvas;
import net.ck.mtbg.ui.components.MapCanvas;
import net.ck.mtbg.ui.components.TextList;
import net.ck.mtbg.ui.dialogs.InventoryDialog;
import net.ck.mtbg.ui.dialogs.StatsDialog;
import net.ck.mtbg.ui.dnd.JGridCanvasDragGestureHandler;
import net.ck.mtbg.ui.dnd.JGridCanvasDropTargetHandler;
import net.ck.mtbg.ui.listeners.Controller;
import net.ck.mtbg.ui.listeners.MyFocusListener;
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

@Getter
@Setter
@Log4j2
public class WindowBuilder
{

    @Getter
    @Setter
    /**
     * stats Dialog - there will be one dialog only with exchanging JPanels
     */
    private static StatsDialog statsDialog;

    @Getter
    @Setter
    /**
     * mainframe
     */
    private static JFrame frame;

    @Getter
    @Setter
    /**
     * left part, GRID Canvas
     */
    private static MapCanvas gridCanvas;

    @Getter
    @Setter
    /**
     * inventoryDialog
     */
    private static InventoryDialog inventoryDialog;

    @Getter
    @Setter
    /**
     * text area is the textlist where all the actions are stored
     */
    private static TextList textArea;

    @Getter
    @Setter
    /**
     * shows the last command
     */
    private static InputField textField;

    @Getter
    @Setter
    /**
     * undo button for retracting turns
     */
    private static JButton undoButton;

    @Getter
    @Setter
    /**
     * weather canvas
     */
    private static JWeatherCanvas weatherCanvas;

    @Getter
    @Setter
    /**
     * button for stopping music - will need to move into options menu once music works properly
     */
    private static StopMusicButton stopMusicButton;

    @Getter
    @Setter
    /**
     * button for starting music - will need to move into options menu once music works properly
     */
    private static StartMusicButton startMusicButton;

    @Getter
    @Setter
    /**
     * button for increasing volume
     */
    private static IncreaseVolumeButton increaseVolumeButton;

    @Getter
    @Setter
    /**
     * button for decreasing volume
     */
    private static DecreaseVolumeButton decreaseVolumeButton;

    @Getter
    @Setter
    private static SaveButton saveButton;

    @Getter
    @Setter
    private static LoadButton loadButton;


    @Getter
    @Setter
    /**
     * mainWindow is not the mainWindow, this is actually the controller
     */
    private static Controller controller;

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
    public static void buildGameWindow(Controller mW)
    {
        logger.info("start: build window");
        controller = mW;
        frame = new GameFrame();
        MyFocusListener myFocusListener = new MyFocusListener();
        undoButton = new UndoButton(new Point(GameConfiguration.UIwidth - 300, GameConfiguration.UIheight - 100));
        undoButton.addFocusListener(myFocusListener);
        frame.add(undoButton);

        stopMusicButton = new StopMusicButton(new Point(GameConfiguration.UIwidth - 400, GameConfiguration.UIheight - 100));
        stopMusicButton.addActionListener(controller);
        frame.add(stopMusicButton);

        startMusicButton = new StartMusicButton(new Point(GameConfiguration.UIwidth - 500, GameConfiguration.UIheight - 100));
        startMusicButton.addActionListener(controller);
        frame.add(startMusicButton);

        increaseVolumeButton = new IncreaseVolumeButton(new Point(GameConfiguration.UIwidth - 600, GameConfiguration.UIheight - 100));
        increaseVolumeButton.addActionListener(controller);
        frame.add(increaseVolumeButton);

        decreaseVolumeButton = new DecreaseVolumeButton(new Point(GameConfiguration.UIwidth - 700, GameConfiguration.UIheight - 100));
        decreaseVolumeButton.addActionListener(controller);
        frame.add(decreaseVolumeButton);

        /*
        save load buttons
         */

        loadButton = new LoadButton(new Point(GameConfiguration.UIwidth - 600, GameConfiguration.UIheight - 170));
        loadButton.addActionListener(controller);
        frame.add(loadButton);

        saveButton = new SaveButton(new Point(GameConfiguration.UIwidth - 700, GameConfiguration.UIheight - 170));
        saveButton.addActionListener(controller);
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
        frame.addWindowListener(controller);

        gridCanvas.addMouseListener(controller);
        gridCanvas.addMouseMotionListener(controller);
        undoButton.addActionListener(controller);

        DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDragGestureHandler(gridCanvas));

        DropTarget dt = new DropTarget(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDropTargetHandler(gridCanvas), true);
        gridCanvas.setDropTarget(dt);

        WindowBuilder.setController(controller);
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
        JLabel title = new JLabel();
        title.setText("TITLE");
        title.setBounds(30, 30, 100, 50);
        JButton characterEditorButton = new TitleScreenButton(100, 30, "Character Editor");
        JButton newGameButton = new TitleScreenButton(100, 90, "New Game");
        JButton loadGameButton = new TitleScreenButton(100, 150, "Load Game");
        JButton creditsButton = new TitleScreenButton(100, 210, "Credits");
        JButton optionsButton = new TitleScreenButton(100, 270, "Options");
        JButton mapEditorButton = new TitleScreenButton(100, 330, "Map Editor");
        //TODO
        titleFrame.add(title);
        titleFrame.add(characterEditorButton);
        titleFrame.add(newGameButton);
        titleFrame.add(loadGameButton);
        titleFrame.add(creditsButton);
        titleFrame.add(optionsButton);
        titleFrame.add(mapEditorButton);
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


}
