package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.ESCAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class SimpleCutScene extends JComponent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void paintComponent(Graphics g)
    {
        logger.debug("draw image");
        g.drawImage(image, 0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight, null);
    }


}
