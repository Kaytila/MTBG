package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class InventoryImagePanel extends JComponent
{
    public void paintComponent(Graphics g)
    {
        g.drawImage(ImageUtils.getInventoryImage(), 1, 1, null);
    }

}
