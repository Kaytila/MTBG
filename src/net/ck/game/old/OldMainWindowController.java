package net.ck.game.old;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.ui.MainWindow;

public class OldMainWindowController
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private Game game;
	private MainWindow mainWindow;

	public Game getGame()
	{
		return this.game;
	}

	public void setGame(Game game)
	{
		this.game = game;
	}

	public MainWindow getMainWindow()
	{
		return this.mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

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

	public OldMainWindowController(MainWindow window, Game game)
	{
		setMainWindow(window);
		setGame(game);
		
		//getMainWindow().addWindowListener(this);
		//getMainWindow().getUndoButton().addActionListener(this);
	}



	public void windowIconified(WindowEvent e)
	{
		
		//getGame().getThreadController().suspendAnimation();

	}

	public void windowDeiconified(WindowEvent e)
	{
		
		//getGame().getThreadController().reanimateAnimation();
	}

	public void windowActivated(WindowEvent e)
	{
		
		//getGame().getThreadController().reanimateAnimation();
	}

	public void windowDeactivated(WindowEvent e)
	{
		
		//getGame().getThreadController().suspendAnimation();
	}

	

	public void actionPerformed(ActionEvent e)
	{		
			
	}

	public void disableUndoButton()
	{
		getMainWindow().getUndoButton().setEnabled(false);
	}

	public void enableUndoButton()
	{
		getMainWindow().getUndoButton().setEnabled(true);
	}
}
