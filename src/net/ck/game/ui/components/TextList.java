package net.ck.game.ui.components;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import net.ck.util.GameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TextList extends JTextArea
{

	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private JScrollPane sp;
	private final int numberOfLines = 15;


	public TextList()
	{
		super();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setVisible(true);
		this.setFont(GameUtils.getFont());
		this.setFocusable(false);
		this.setAutoscrolls(true);
		this.setBorder(blackline);
		//this.setToolTipText(getLogger().getName());
	}


	public JScrollPane initializeScrollPane()
	{
		sp = new JScrollPane(this);
		sp.setBounds(GameConfiguration.UIwidth - 200, 200, 150, (GameUtils.getLineHeight() * numberOfLines));
		sp.setVisible(true);
		return sp;
	}
}
