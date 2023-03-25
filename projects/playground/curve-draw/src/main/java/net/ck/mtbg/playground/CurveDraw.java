package net.ck.mtbg.playground;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class CurveDraw extends JFrame
{
	 public static void main(String[] args) {
         CurveDraw frame = new CurveDraw();
         frame.setVisible(true);
 }
 public CurveDraw() {
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(400,400);
 }
 public void paint(Graphics g) {
         QuadCurve2D.Double curve = new QuadCurve2D.Double(0,16,8,0,16,16);
         ((Graphics2D)g).draw(curve);
         QuadCurve2D.Double curve2 = new QuadCurve2D.Double(16,16,24,32,32,16);
         ((Graphics2D)g).draw(curve2);
 }
	
}
