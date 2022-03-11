package net.ck.game.demos;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.geom.QuadCurve2D;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CurveDraw extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}

	
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
