package net.ck.game.ui.components;

import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InventoryImagePanel extends JComponent
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public void paintComponent(Graphics g)
    {
       // g.setColor(Color.black);
        //g.drawOval(0,0, 50, 50);
        g.drawImage(ImageUtils.getInventoryImage(), 1, 1, null);
    }

}
