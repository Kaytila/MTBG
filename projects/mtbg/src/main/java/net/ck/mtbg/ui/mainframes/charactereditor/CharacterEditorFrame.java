package net.ck.mtbg.ui.mainframes.charactereditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.controllers.CharacterEditorController;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class CharacterEditorFrame extends JFrame
{


    public CharacterEditorFrame() throws HeadlessException
    {
        CharacterEditorController.getCurrent().setCharacterEditorFrame(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setVisible(true);
        this.setLayout(null);
        this.addWindowListener(CharacterEditorController.getCurrent());


    }
}
