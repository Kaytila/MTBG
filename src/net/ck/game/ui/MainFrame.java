package net.ck.game.ui;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.ui.buttons.UndoButton;
import net.ck.game.ui.components.JGridCanvas;
import net.ck.game.ui.components.JWeatherCanvas;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
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


	private UndoButton undoButton;
	private JGridCanvas gridCanvas;
	private JWeatherCanvas weatherCanvas;


	public UndoButton getUndoButton()
	{
		return undoButton;
	}

	public void setUndoButton(UndoButton undoButton)
	{
		this.undoButton = undoButton;
	}

	public JGridCanvas getGridCanvas()
	{
		return gridCanvas;
	}

	public void setGridCanvas(JGridCanvas gridCanvas)
	{
		this.gridCanvas = gridCanvas;
	}

	public JWeatherCanvas getWeatherCanvas()
	{
		return weatherCanvas;
	}

	public void setWeatherCanvas(JWeatherCanvas weatherCanvas)
	{
		this.weatherCanvas = weatherCanvas;
	}
	public MainFrame() throws HeadlessException
	{

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{
			this.setIconImage(ImageIO.read(new File(ImageUtils.getAdditionalimagespath() + File.separator + File.separator + "sun.jpg")));
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

	/*
	public void buildWindowLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		this.setContentPane(contentPane);
		this.getContentPane().setBackground(Color.CYAN);
		contentPane.setBounds(this.getBounds());
		contentPane.setVisible(true);
		GridBagConstraints c = new GridBagConstraints();

		gridCanvas = new JGridCanvas();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.weightx = 0.5;
		c.weighty = 1;
		contentPane.add(gridCanvas, c);

		weatherCanvas = new JWeatherCanvas(Game.getCurrent().getWeatherSystem().getCurrentWeather());
		// c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		contentPane.add(weatherCanvas, c);

		undoButton = new UndoButton(new Point(350, 300));
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		contentPane.add(undoButton, c);

		logger.info("build window done");

		for (Component com : this.getContentPane().getComponents())
		{
			logger.info("Container: {}, size: {}, bounds: {}", com.getClass().getName(), com.getSize(), com.getBounds());
		}

	}
	*/

	public void buildWindow()
	{
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		this.getContentPane().setBackground(Color.CYAN);
		contentPane.setBounds(this.getBounds());
		contentPane.setVisible(true);		
	}
	
	
	public void addControls()
	{
		gridCanvas = new JGridCanvas();
		this.getContentPane().add(gridCanvas);

		weatherCanvas = new JWeatherCanvas();
		this.getContentPane().add(weatherCanvas);

		undoButton = new UndoButton(new Point(350, 300));
		this.getContentPane().add(undoButton);
		

		for (Component com : this.getContentPane().getComponents())
		{
			logger.info("Container: {}, size: {}, bounds: {}", com.getClass().getName(), com.getSize(), com.getBounds());
		}
		logger.info("build window done");
	}

}
