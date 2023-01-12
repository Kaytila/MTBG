package net.ck.game.ui.mainframes;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is the main frame of the application
 * 
 * @author Claus
 *
 */
public class MainFrame extends JFrame
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public MainFrame() throws HeadlessException
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

		this.setBounds(0, 0, GameConfiguration.UIwidth, GameConfiguration.UIheight);
		logger.info("bound: {}", this.getBounds());
		this.setLocationRelativeTo(null);
		this.setFocusable(false);
		this.setTitle("My Demo");		
		this.setResizable(false);
		this.toFront();
		this.setLayout(null);
	}
}