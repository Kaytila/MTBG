package net.ck.mtbg.ui.renderers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.attributes.AbstractAttribute;
import net.ck.mtbg.ui.components.StatsPane;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class StatsPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractAttribute>
{


	public StatsPaneListCellRenderer(StatsPane statsPane) {

	}

	@Override
	public Component getListCellRendererComponent(JList<? extends AbstractAttribute> list, AbstractAttribute value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		String text = value.getClass().getSimpleName() + ": " + value.getValue();
		ImageIcon icon = new ImageIcon(value.getImage());
		setIcon(icon);
		setText(text);
		setFont(list.getFont());

		return this;
	}
}
