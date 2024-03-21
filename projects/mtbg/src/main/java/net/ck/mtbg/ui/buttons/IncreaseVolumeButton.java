package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class IncreaseVolumeButton extends JButton
{
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
}
