package net.ck.game.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class IncreaseVolumeButton extends JButton
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public IncreaseVolumeButton(Point p)
    {
        //logger.info("creating button");
        this.setText("Louder");
        setBounds(p.x, p.y, 70, 30);
        this.setVisible(true);
        this.setFocusable(false);
        //this.setMnemonic(KeyEvent.VK_U);
        this.setActionCommand("Louder");
        this.setEnabled(true);
        this.setDoubleBuffered(true);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
