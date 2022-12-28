package net.ck.game.ui.buttons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DecreaseVolumeButton extends JButton
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public DecreaseVolumeButton(Point p)
    {
        //logger.info("creating button");
        this.setText("Leiser");
        setBounds(p.x, p.y, 70, 30);
        this.setVisible(true);
        this.setFocusable(false);
        //this.setMnemonic(KeyEvent.VK_U);
        this.setActionCommand("Leiser");
        this.setEnabled(true);
        this.setDoubleBuffered(true);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
