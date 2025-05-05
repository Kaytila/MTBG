package net.ck.mtbg.ui.mainframes.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is the main frame of the application
 *
 * @author Claus
 */
@Getter
@Setter
@Log4j2
public class GameFrame extends JFrame
{
	public GameFrame() throws HeadlessException
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
		this.setUndecorated(true);
		this.setResizable(false);
		this.toFront();
		this.setLayout(null);
	}
}