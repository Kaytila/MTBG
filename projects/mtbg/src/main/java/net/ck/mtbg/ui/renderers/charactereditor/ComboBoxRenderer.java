package net.ck.mtbg.ui.renderers.charactereditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.models.charactereditor.CharacterPortraitColor;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class ComboBoxRenderer extends JLabel implements ListCellRenderer
{
    public ComboBoxRenderer()
    {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
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

        String text = ((CharacterPortraitColor) value).getDescription();
        setText(text);
        BufferedImage img = ImageUtils.createImage(((CharacterPortraitColor) value).getColor(), GameConfiguration.characterEditorTinyTileSize);
        ImageIcon icon = new ImageIcon(img);
        setIcon(icon);
        return this;
    }
}
