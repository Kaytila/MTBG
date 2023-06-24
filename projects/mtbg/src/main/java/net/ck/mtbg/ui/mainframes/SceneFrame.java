package net.ck.mtbg.ui.mainframes;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.EnhancedCutSceneWithText;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SceneFrame extends JFrame
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public SceneFrame() throws HeadlessException
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		EnhancedCutSceneWithText cutScene = new EnhancedCutSceneWithText(images, texts);
		//this.getContentPane().add(cutScene);
		this.add(cutScene);
		cutScene.setVisible(true);
		this.setVisible(true);
		this.setResizable(false);
		this.toFront();
		this.setLayout(null);
	}
}
