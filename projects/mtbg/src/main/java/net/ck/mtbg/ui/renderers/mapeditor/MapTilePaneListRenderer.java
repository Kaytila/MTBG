package net.ck.mtbg.ui.renderers.mapeditor;

import net.ck.mtbg.map.ProtoMapTile;

import javax.swing.*;
import java.awt.*;

public class MapTilePaneListRenderer extends JLabel implements ListCellRenderer<ProtoMapTile>
{
    @Override
    public Component getListCellRendererComponent(JList<? extends ProtoMapTile> list, ProtoMapTile value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        // Set the icon and text. If icon was null, say so.
        String text = value.getName();
        setIcon(value.getIcon());
        setText(text);
        setFont(list.getFont());
        return this;
    }
}
