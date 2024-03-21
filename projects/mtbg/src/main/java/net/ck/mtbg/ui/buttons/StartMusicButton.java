package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class StartMusicButton extends JButton
{
    public StartMusicButton(Point p)
    {
        //logger.info("creating button");
        this.setText("Start Music");
        setBounds(p.x, p.y, 70, 30);
        this.setVisible(true);
        this.setFocusable(false);
        //this.setMnemonic(KeyEvent.VK_U);
        this.setActionCommand("StartMusic");
        this.setEnabled(true);
        this.setDoubleBuffered(true);
        //this.setToolTipText(getLogger().getName());
    }
}
