package net.ck.game.ui;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.util.GameUtils;

public class TextList extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private JScrollPane sp;
	private final int numberOfLines = 15;

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	public TextList()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.black);		
		this.setVisible(true);
		this.setFont(GameUtils.getFont());
		this.setFocusable(false);
		this.setAutoscrolls(true);
		this.setBorder(blackline);
		this.setToolTipText(getLogger().getName());
	}
	
	
	public JScrollPane initializeScrollPane()
	{
		sp = new JScrollPane(this);
		sp.setBounds(700 - 200, 200, 150, (GameUtils.getLineHeight() * numberOfLines));
		sp.setVisible(true);
		return sp;
	}

	public Logger getLogger()
	{
		return logger;
	}
}
