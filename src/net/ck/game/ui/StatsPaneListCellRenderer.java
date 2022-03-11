package net.ck.game.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.AbstractAttribute;

public class StatsPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractAttribute>
{

	private static final long serialVersionUID = 1L;
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
