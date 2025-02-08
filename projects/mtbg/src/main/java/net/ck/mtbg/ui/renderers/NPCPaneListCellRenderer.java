package net.ck.mtbg.ui.renderers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@Log4j2
@Getter
@Setter
public class NPCPaneListCellRenderer extends JLabel implements javax.swing.ListCellRenderer<NPC>
{
    @Override
    public Component getListCellRendererComponent(JList<? extends NPC> list, NPC value, int index, boolean isSelected, boolean cellHasFocus)
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
        ImageIcon icon = new ImageIcon(ImageUtils.loadImage("lifeforms" + File.separator + String.valueOf(value.getType()), "image0"));
        String text = value.getType().name();
        setIcon(icon);
        setText(text);
        //setFont(list.getFont());
        return this;
    }
}
