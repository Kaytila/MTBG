package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.EnhancedCutSceneWithDynamicText;
import net.ck.mtbg.ui.components.EnhancedCutSceneWithText;
import net.ck.mtbg.util.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
@Log4j2
public class SceneFrame extends JFrame
{
    public SceneFrame() throws HeadlessException
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            this.setIconImage(ImageIO.read(new File(GameConfiguration.miscImages + "sun.jpg")));
        }
        catch (IOException e)
        {
            logger.error("issue loading icon: {}", e.toString());
        }

        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(true);
        //JPanel mainPanel = new JPanel();
        //mainPanel.setPreferredSize(new Dimension(GameConfiguration.UIwidth, GameConfiguration.UIheight));
        //this.setContentPane(mainPanel);


        //SimpleCutSceneWithText cutScene = new SimpleCutSceneWithText(ImageUtils.loadImage("cutscenes", "dischord"), "TEST TEST TEST");

        ArrayList<BufferedImage> images = new ArrayList<>(2);
        images.add(ImageUtils.loadImage("cutscenes", "dischord"));
        images.add(ImageUtils.loadImage("cutscenes", "concerts"));
        ArrayList<String> texts = new ArrayList<>(2);
        texts.add("Hello, this is the text for the first image");
        texts.add("And I am the text for the second image");
        EnhancedCutSceneWithText cutScene = new EnhancedCutSceneWithDynamicText(images, texts);

        this.add(cutScene);
        cutScene.setVisible(true);
        this.setVisible(true);
        this.setResizable(false);
        this.toFront();
        this.setLayout(null);
    }
}
