package net.ck.mtbg.ui.mainframes.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Log4j2
@Getter
@Setter


public class TestFrame extends JFrame
{
    private static final int TILE_SIZE = 50; // Assume each tile is 50x50 pixels
    private static final int MAP_WIDTH = 500; // Total map width
    private static final int MAP_HEIGHT = 500; // Total map height
    private int xOffset = 0; // Offset to keep track of map scroll position
    private int yOffset = 0;

    private JPanel mapPanel;

    public TestFrame()
    {
        setTitle("Game Map Scroll Example");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mapPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawMap(g);
            }
        };

        // Set the preferred size of the map to be the size of the whole game map
        mapPanel.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));

        // Create a scroll pane for the map
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Add Mouse Listener for clicks
        mapPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                Point point = e.getPoint();
                if (point.x < getWidth() / 2)
                {
                    // Scroll left (by one tile)
                    xOffset = Math.max(xOffset - TILE_SIZE, 0);
                }
                else
                {
                    // Scroll right (by one tile)
                    xOffset = Math.min(xOffset + TILE_SIZE, MAP_WIDTH - getWidth());
                }

                if (point.y < getHeight() / 2)
                {
                    // Scroll up (by one tile)
                    yOffset = Math.max(yOffset - TILE_SIZE, 0);
                }
                else
                {
                    // Scroll down (by one tile)
                    yOffset = Math.min(yOffset + TILE_SIZE, MAP_HEIGHT - getHeight());
                }

                // Repaint the map after scrolling
                mapPanel.repaint();
            }
        });

        setLocationRelativeTo(null); // Center the window
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            TestFrame frame = new TestFrame();
            frame.setVisible(true);
        });
    }

    // Method to draw the map, using tiles
    private void drawMap(Graphics g)
    {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < MAP_WIDTH; x += TILE_SIZE)
        {
            for (int y = 0; y < MAP_HEIGHT; y += TILE_SIZE)
            {
                g.fillRect(x - xOffset, y - yOffset, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}