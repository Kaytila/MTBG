package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.controllers.TitleScreenController;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class TitleFrame extends JFrame
{
    public TitleFrame() throws HeadlessException
    {
        this.setTitle(GameConfiguration.titleString);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setVisible(true);
        this.addWindowListener(TitleScreenController.getCurrent());
    }
}
