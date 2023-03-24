package net.ck.mtbg.playground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Claus from
 *         https://stackoverflow.com/questions/17916057/tile-drawing-on-canvas-based-on-file-with-ints
 */
@SuppressWarnings("serial")
public class GridTester extends JPanel implements KeyListener
{
	private Ground[][] groundMap =
	{
		{Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.WATER, Ground.WATER, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.WATER, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.WATER, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.DIRT,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.DIRT, Ground.WATER,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT, Ground.DIRT, Ground.DIRT,
			Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT,
			Ground.DIRT, Ground.DIRT, Ground.DIRT, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.DIRT,
			Ground.DIRT, Ground.DIRT, Ground.WATER, Ground.WATER, Ground.WATER},
		{Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS, Ground.GRASS,
			Ground.DIRT, Ground.DIRT, Ground.DIRT, Ground.WATER, Ground.WATER},};

	private JLabel[][] labelGrid = new JLabel[groundMap.length][groundMap[0].length];

	public GridTester()
	{
		setLayout(new GridLayout(groundMap.length, groundMap[0].length));
		for (int r = 0; r < labelGrid.length; r++)
		{
			for (int c = 0; c < labelGrid[r].length; c++)
			{
				labelGrid[r][c] = new JLabel();
				labelGrid[r][c].setIcon(groundMap[r][c].getIcon());
				add(labelGrid[r][c]);
			}
		}

		addMouseListener(new MyMouseListener());
	}

	private class MyMouseListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent mEvt)
		{
			Component comp = getComponentAt(mEvt.getPoint());
			for (int row = 0; row < labelGrid.length; row++)
			{
				for (int col = 0; col < labelGrid[row].length; col++)
				{
					if (labelGrid[row][col] == comp)
					{
						Ground ground = groundMap[row][col];
						int mapCode = ground.getValue();
						mapCode++;
						mapCode %= Ground.values().length;
						groundMap[row][col] = Ground.values()[mapCode];
						labelGrid[row][col].setIcon(groundMap[row][col].getIcon());
					}
				}
			}
		}
	}

	private static void createAndShowGui()
	{
		GridTester mainPanel = new GridTester();

		JFrame frame = new JFrame("GridExample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGui();
			}
		});
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{

		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			System.out.println("Right key typed");
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			System.out.println("Left key typed");
		}

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			System.out.println("Right key pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			System.out.println("Left key pressed");
		}

	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			System.out.println("Right key Released");
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			System.out.println("Left key Released");
		}
	}
}

enum Ground {
    DIRT(0, new Color(205, 133, 63)), GRASS(1, new Color(0, 107, 60)), WATER(2,
            new Color(29, 172, 214));

    private final int value;
    private final Color color;
    private final Icon icon;

    Ground(int value, Color color) {
        this.value = value;
        this.color = color;

        icon = createIcon();
    }

    private Icon createIcon()
	{
		int width = 24;
		BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, width, width);
		g.dispose();
		return new ImageIcon(img);
	}

	public int getValue()
	{
		return value;
	}

	public Color getColor()
	{
		return color;
	}

	public Icon getIcon()
	{
		return icon;
	}

	public static Ground getGround(int value)
	{
		for (Ground ground : Ground.values())
		{
			if (ground.getValue() == value)
			{
				return ground;
			}
		}
		return null;
	}



}