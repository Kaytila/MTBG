package net.ck.game.ui.renderers;

import net.ck.game.backend.entities.AbstractAttribute;
import net.ck.game.ui.components.StatsPane;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class StatsPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractAttribute>
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public StatsPaneListCellRenderer(StatsPane statsPane)
	{
	
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractAttribute> list, AbstractAttribute value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		String text = value.getClass().getSimpleName() + ": " + String.valueOf(value.getValue());
		ImageIcon icon = new ImageIcon(value.getImage());
		setIcon(icon);
		setText(text);
		setFont(list.getFont());
		
		return this;
	}
}
