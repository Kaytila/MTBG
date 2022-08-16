package net.ck.game.old;

import net.ck.game.backend.Game;
import net.ck.game.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;

public class WindowBuilder
{

	private static final Logger logger = (Logger) LogManager.getLogger(WindowBuilder.class);
	private static MainFrame frame;
	private static UndoButton undoButton;
	private static JGridCanvas gridCanvas;
	private static TextList textArea;
	private static InputField textField;
	private static JWeatherCanvas weatherCanvas;
	private static Controller controller;
	private static MainWindow mainWindow;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public WindowBuilder()
	{

	}
	
	public static void buildController()
	{
		controller = new Controller();
	}
	
	
	public static void buildApplicationListener()
	{
		mainWindow = new MainWindow();
	}
	
	
	public static void buildWindow()
	{
		logger.info("start: build window");
		frame = new MainFrame();
		undoButton = new UndoButton(new Point(700 - 200, 620));

		frame.add(undoButton);

		gridCanvas = new JGridCanvas();
		frame.add(gridCanvas);

		textArea = new TextList();
		frame.add(textArea.initializeScrollPane());

		textField = new InputField();
		frame.add(textField);

		weatherCanvas = new JWeatherCanvas();
		frame.add(weatherCanvas);

		logger.info("setting listeners");
		frame.addWindowListener(mainWindow);

		gridCanvas.addMouseListener(mainWindow);
		gridCanvas.addMouseMotionListener(mainWindow);
		undoButton.addActionListener(mainWindow);

		frame.setVisible(true);
		logger.info("finish: build window");
		logger.info("setting up event bus");
		EventBus.getDefault().register(mainWindow);
		Game.getCurrent().setController(mainWindow);
	}

	
}
