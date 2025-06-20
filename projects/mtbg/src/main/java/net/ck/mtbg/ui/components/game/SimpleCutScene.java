package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.communication.keyboard.framework.ESCAction;
import org.imgscalr.Scalr;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class SimpleCutScene extends JComponent
{
    private BufferedImage image;

    public SimpleCutScene(BufferedImage img)
    {
        image = Scalr.resize(img, GameConfiguration.UIheight);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        this.getActionMap().put("escape", new ESCAction());
        this.setOpaque(false);
        this.setVisible(true);
        this.setFocusable(true);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);
        this.setBackground(Color.green);
    }

    public SimpleCutScene()
    {
    }

    public void paintComponent(Graphics g)
    {
        logger.debug("draw image");
        g.drawImage(image, 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);
    }


}
