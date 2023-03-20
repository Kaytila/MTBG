package net.ck.mtbg.playground;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

/*
 * SplashDemo.java
 * 
 * example by oracle how to do a Splash screen
 * by now incorporated in RunGame
 *
 */
public class SplashDemo extends Frame implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	static void renderSplashFrame(Graphics2D g, int frame)
	{
		final String[] comps =
		{ "foo", "bar", "baz" };
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(120, 140, 200, 40);
		g.setPaintMode();
		g.setColor(Color.BLACK);
		g.drawString("Loading " + comps[(frame / 5) % 3] + "...", 120, 150);
	}

	public SplashDemo()
	{
		super("SplashScreen demo");
		setSize(300, 200);
		setLayout(new BorderLayout());
		Menu m1 = new Menu("File");
		MenuItem mi1 = new MenuItem("Exit");
		m1.add(mi1);
		mi1.addActionListener(this);
		this.addWindowListener(closeWindow);

		MenuBar mb = new MenuBar();
		setMenuBar(mb);
		mb.add(m1);
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null)
		{
			logger.info("SplashScreen.getSplashScreen() returned null");
			return;
		}
		Graphics2D g = splash.createGraphics();
		if (g == null)
		{
			System.out.println("g is null");
			return;
		}
		for (int i = 0; i < 100; i++)
		{
			renderSplashFrame(g, i);
			splash.update();
			try
			{
				Thread.sleep(90);
			} catch (InterruptedException e)
			{
			}
		}
		splash.close();
		setVisible(true);
		toFront();
	}

	public void actionPerformed(ActionEvent ae)
	{
		System.exit(0);
	}

	private static WindowListener closeWindow = new WindowAdapter()
	{
		public void windowClosing(WindowEvent e)
		{
			e.getWindow().dispose();
		}
	};

	public static void main(String[] args) {
        SplashDemo test = new SplashDemo();
    }
}