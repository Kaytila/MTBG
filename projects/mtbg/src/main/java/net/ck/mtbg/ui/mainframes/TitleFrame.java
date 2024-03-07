package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.state.NoiseManager;
import net.ck.mtbg.ui.listeners.TitleController;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class TitleFrame extends JFrame
{
    TitleController titleController;

    public TitleFrame() throws HeadlessException
    {
        this.titleController = new TitleController(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setLayout(null);
        this.setVisible(true);
        this.addWindowListener(titleController);

        NoiseManager.getMusicSystemNoThread().playSong();
    }
}
