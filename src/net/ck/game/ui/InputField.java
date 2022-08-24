package net.ck.game.ui;

import com.google.common.collect.Iterables;
import net.ck.game.backend.GameConfiguration;
import net.ck.util.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class InputField extends JTextField
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private ArrayList<String> contents = new ArrayList<String>();

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

	/**
	 * create the input field, stupid settings to find it on the screen
	 */
	public InputField()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.red);
		this.setBounds(GameConfiguration.UIwidth - 200, 580, 150, GameUtils.getLineHeight());
		
		this.setVisible(true);
		this.setFont(GameUtils.getFont());
		this.setFocusable(false);
		this.setBorder(blackline);
		//this.setToolTipText(getLogger().getName());
	}

	/**
	 * add the last command to the command list (not sure whether getting it from the Game.currentPlayer() wouldnt be better)
	 */
	public void setText(String t)
	{
		contents.add(t);
		super.setText(t);
	}

	/**
	 * retract the last turn, remove the last entry in the command window.
	 * https://stackoverflow.com/questions/687833/how-to-get-the-last-value-of-an-arraylist
	 */
	public void retractTurn()
	{

		//for (String t : contents)
		//{
			//logger.info("input field contents: {}", t);
		//}
		
		String lastElement;
		try
		{
			lastElement = Iterables.getLast(contents);
		}
		catch (NoSuchElementException e)
		{
			lastElement = "";
		}
		contents.remove(lastElement);
		try
		{
			lastElement = Iterables.getLast(contents);
		}
		catch (NoSuchElementException e)
		{
			lastElement = "";
		}
		super.setText(lastElement);
	}

	public Logger getLogger()
	{
		return logger;
	}

}
