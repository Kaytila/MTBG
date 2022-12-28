package net.ck.game.ui.components;

import net.ck.game.backend.game.Game;
import net.ck.game.backend.entities.AbstractAttribute;
import net.ck.util.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StatsPane extends JList<AbstractAttribute>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane sp;
	
	public StatsPane()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setVisible(true);
		this.setFont(GameUtils.getFont());
		
		this.setAutoscrolls(true);
		this.setBorder(blackline);
		//this.setToolTipText(getLogger().getName());
		this.requestFocus();
		this.setVisibleRowCount(-1);						
		this.setModel(Game.getCurrent().getCurrentPlayer().getAttributes());
	}

	public JScrollPane initializeScrollPane()
	{
		sp = new JScrollPane(this);
		sp.setBounds(0, 0, 250, 200);
		sp.setVisible(true);		
		return sp;
	}
	

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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

	public Logger getLogger()
	{
		return logger;
	}
}
