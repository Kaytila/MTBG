package net.ck.game.demos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleFrame extends JFrame
{
	JPanel mainPanel = new JPanel()
	{
		private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
		String imageRootPath = "graphics/players/player";
		ImageIcon originalIcon = new ImageIcon(imageRootPath + "1" + "/image1.png");

		ImageFilter filter = new RGBImageFilter()
		{
			int transparentColor = Color.white.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == transparentColor)
				{
					return 0x00FFFFFF & rgb;
				}
				else
				{
					return rgb;
				}
			}
		};

		ImageProducer filteredImgProd = new FilteredImageSource(
			originalIcon.getImage().getSource(), filter);
		Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);

		public void paintComponent(Graphics g)
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getSize().width, getSize().height);

			// draw the original icon
			g.drawImage(originalIcon.getImage(), 100, 10, this);
			// draw the transparent icon
			g.drawImage(transparentImg, 140, 10, this);
		}
	};

	public SimpleFrame()
	{
		super("Transparency Example");

		JPanel content = (JPanel) getContentPane();
		mainPanel.setBackground(Color.black);
		content.add("Center", mainPanel);
	}

	public static void main(String[] argv)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				SimpleFrame c = new SimpleFrame();
				c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				c.setSize(280, 100);
				c.setVisible(true);
			}
		});
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

}
