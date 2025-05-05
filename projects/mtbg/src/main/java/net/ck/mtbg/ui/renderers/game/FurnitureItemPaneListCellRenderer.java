package net.ck.mtbg.ui.renderers.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.FurnitureItem;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class FurnitureItemPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<FurnitureItem>
{

    @Override
    public Component getListCellRendererComponent(JList<? extends FurnitureItem> list, FurnitureItem value, int index, boolean isSelected, boolean cellHasFocus)
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
