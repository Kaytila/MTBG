package net.ck.mtbg.ui.components;

import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class InventoryImagePanel extends JComponent
{

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public void paintComponent(Graphics g)
    {
       // g.setColor(Color.black);
        //g.drawOval(0,0, 50, 50);
        g.drawImage(ImageUtils.getInventoryImage(), 1, 1, null);
    }

}
