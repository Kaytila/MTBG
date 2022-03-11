package net.ck.util;

import java.awt.Point;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * https://www.codeproject.com/Articles/15604/Ray-casting-in-a-2D-tile-based-environment
 * https://gamedev.stackexchange.com/questions/21897/how-to-quickly-calculate-the-sight-area-in-a-2d-tiled-game
 * 
 * @author Claus
 *
 */
public class LineOfSight
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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

	public Logger getLogger()
	{
		return logger;
	}
	
	
	// Swap the values of A and B
	//private void swap<T>(ref T a, ref T b) {
	 //   T c = a;
	  //  a = b;
	  //  b = c;
	//}

	// Returns the list of points from p0 to p1 
	public static ArrayList<Point> BresenhamLine(Point p0, Point p1) 
	{
	    return BresenhamLine(p0.x, p0.y, p1.x, p1.y);
	}

	// Returns the list of points from (x0, y0) to (x1, y1)
	private static ArrayList<Point> BresenhamLine(int x0, int y0, int x1, int y1) 
	{
	    // Optimization: it would be preferable to calculate in
	    // advance the size of "result" and to use a fixed-size array
	    // instead of a list.
		ArrayList<Point> result = new ArrayList<Point>();

	    boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
	    if (steep) 
	    {
	       // swap(ref x0, ref y0);
	        //swap(ref x1, ref y1);
	    }
	    
	    if (x0 > x1) {
	        //swap(ref x0, ref x1);
	        //swap(ref y0, ref y1);
	    }

	    int deltax = x1 - x0;
	    int deltay = Math.abs(y1 - y0);
	    int error = 0;
	    int ystep;
	    int y = y0;
	    if (y0 < y1) ystep = 1; else ystep = -1;
	    for (int x = x0; x <= x1; x++) {
	        if (steep) result.add(new Point(y, x));
	        else result.add(new Point(x, y));
	        error += deltay;
	        if (2 * error >= deltax) {
	            y += ystep;
	            error -= deltax;
	        }
	    }

	    return result;
	}
	
}
