package net.ck.mtbg.ui.components;

import com.google.common.collect.Iterables;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class InputField extends JTextField
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private ArrayList<String> contents = new ArrayList<>();

	/**
	 * create the input field, stupid settings to find it on the screen
	 */
	public InputField()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.red);
		this.setBounds(GameConfiguration.UIwidth - 200, 580, 150, GameConfiguration.lineHight);
		
		this.setVisible(true);
		this.setFont(GameConfiguration.font);
		this.setFocusable(false);
		this.setBorder(blackline);
		//this.setToolTipText(getLogger().getName());
		logger.info("created input field for talk dialog");
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
	 * <a href="https://stackoverflow.com/questions/687833/how-to-get-the-last-value-of-an-arraylist">https://stackoverflow.com/questions/687833/how-to-get-the-last-value-of-an-arraylist</a>
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

}
