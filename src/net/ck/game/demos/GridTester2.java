package net.ck.game.demos;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class GridTester2 extends JPanel {
   public static final int[][] MAP = {
      {1, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2},
      {1, 1, 0, 0, 2, 2, 2, 2, 2, 2, 2},
      {1, 1, 1, 0, 2, 2, 2, 2, 2, 2, 2},
      {1, 1, 1, 0, 0, 2, 2, 2, 2, 2, 2},
      {1, 1, 1, 1, 0, 2, 2, 2, 2, 2, 2},
      {1, 1, 1, 0, 0, 0, 2, 2, 2, 2, 2},
      {1, 1, 0, 0, 0, 2, 2, 2, 2, 2, 2},
      {1, 1, 1, 0, 0, 0, 2, 2, 2, 2, 2},
      {1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2},
      {1, 1, 1, 1, 1, 0, 0, 0, 2, 2, 2},
      {1, 1, 1, 1, 1, 1, 0, 0, 0, 2, 2}
   };

   public static final Color[] COLORS = {};
   private JLabel[][] labelGrid = new JLabel[MAP.length][MAP[0].length];

   public GridTester2() {
      setLayout(new GridLayout(MAP.length, MAP[0].length));
      for (int r = 0; r < labelGrid.length; r++) {
         for (int c = 0; c < labelGrid[r].length; c++) {
            labelGrid[r][c] = new JLabel();
            labelGrid[r][c].setIcon(Ground.getGround(MAP[r][c]).getIcon());
            add(labelGrid[r][c]);            
         }
      }
   }

   private static void createAndShowGui() {
      GridTester2 mainPanel = new GridTester2();

      JFrame frame = new JFrame("GridExample");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}

enum Ground2 {
   DIRT(0, new Color(205,133, 63)), GRASS(1, new Color(0, 107, 60)), 
   WATER(2, new Color(29, 172, 214));
   private int value;
   private Color color;
   private Icon icon;

   private Ground2(int value, Color color) {
      this.value = value;
      this.color = color;

      icon = createIcon(color);
   }

   private Icon createIcon(Color color) {
      int width = 24; // how to use const in enum? 
      BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
      Graphics g = img.getGraphics();
      g.setColor(color);
      g.fillRect(0, 0, width, width);
      g.dispose();
      return new ImageIcon(img);
   }

   public int getValue() {
      return value;
   }

   public Color getColor() {
      return color;
   }

   public Icon getIcon() {
      return icon;
   }

   public static Ground getGround(int value) {
      for (Ground ground : Ground.values()) {
         if (ground.getValue() == value) {
            return ground;
         }
      }
      return null;
   }

}