package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.attributes.AbstractAttribute;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.CodeUtils;
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
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private JScrollPane sp;
	
	public StatsPane()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setVisible(true);
		this.setFont(GameConfiguration.font);
		
		this.setAutoscrolls(true);
		this.setBorder(blackline);
		//this.setToolTipText(getLogger().getName());
		this.requestFocus();
		this.setVisibleRowCount(-1);						
		this.setModel(Game.getCurrent().getCurrentPlayer().getAttributes());
		logger.info("Building StatsPane done");
	}

	public JScrollPane initializeScrollPane()
	{
		sp = new JScrollPane(this);
		sp.setBounds(0, 0, 250, 200);
		sp.setVisible(true);		
		return sp;
	}
}
