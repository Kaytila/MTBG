package net.ck.mtbg.ui.components;

import net.ck.mtbg.ui.listeners.SpellBookListener;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Spellbook extends JTable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private Point selectionPoint;

	public Spellbook(Object[][] data, String[] columnNames)
	{
		super(data, columnNames);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBounds(0, 20, 200, 100);
		setFillsViewportHeight(true);
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(true);

		SpellBookListener spellBookListener = new SpellBookListener();
		this.addMouseListener(spellBookListener);
		this.addMouseMotionListener(spellBookListener);

	}

	public Point getSelectionPoint()
	{
		return selectionPoint;
	}

	public void setSelectionPoint(Point selectionPoint)
	{
		this.selectionPoint = selectionPoint;
	}
}
