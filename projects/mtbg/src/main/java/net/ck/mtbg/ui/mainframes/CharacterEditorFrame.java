package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.listeners.CharacterEditorController;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class CharacterEditorFrame extends JFrame {
    CharacterEditorController characterEditorController;

    public CharacterEditorFrame() throws HeadlessException {
        this.characterEditorController = new CharacterEditorController(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setVisible(true);
        this.addWindowListener(characterEditorController);

    }
}
