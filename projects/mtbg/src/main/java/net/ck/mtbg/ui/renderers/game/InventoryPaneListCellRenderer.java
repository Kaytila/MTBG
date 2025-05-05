package net.ck.mtbg.ui.renderers.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.AbstractItem;

import javax.swing.*;
import java.awt.*;

/**
 * https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html#renderer
 * https://docs.oracle.com/javase/tutorial/uiswing/components/list.html#mutable
 *
 * @author Claus
 */
@Getter
@Setter
@Log4j2
public class InventoryPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<AbstractItem>
{

    public InventoryPaneListCellRenderer()
    {
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends AbstractItem> list, AbstractItem value, int index, boolean isSelected, boolean cellHasFocus)
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

        // Set the icon and text. If icon was null, say so.
        ImageIcon icon = new ImageIcon(value.getItemImage());
        String text = value.getName();
        setIcon(icon);
        setText(text);
        setFont(list.getFont());
        return this;
    }
}
