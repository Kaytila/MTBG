package net.ck.util;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.util.communication.graphics.CursorChangeEvent;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CursorUtils
{

	private static final Logger logger = (Logger) LogManager.getLogger(CursorUtils.class);
	private static final String additionalImagesPath = "graphics" + File.separator + "misc";
	private static final int border = Game.getCurrent().getTileSize() / 2;
	private static BufferedImage cursorImageNorth;
	private static BufferedImage cursorImageEast;
	private static BufferedImage cursorImageSouth;
	private static BufferedImage cursorImageWest;
	private static Cursor cursor;
	private static Cursor northCursor;
	private static Cursor eastCursor;
	private static Cursor southCursor;
	private static Cursor westCursor;
	private static Cursor targetCursor;

	private static Point lastMousePosition;

	public static void initializeCursors()
	{
		logger.info("start: initializing cursors");

		Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);

		setWestCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageUtils.makeImageTransparent(getAdditionalimagespath() + File.separator + "CURSORS" + File.separator + "270.png"),
			new Point(bestSize.width / 2, bestSize.height / 2), "westCursor"));
		setEastCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageUtils.makeImageTransparent(getAdditionalimagespath() + File.separator + "CURSORS" + File.separator + "90.png"),
			new Point(bestSize.width / 2, bestSize.height / 2), "eastCursor"));
		setNorthCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageUtils.makeImageTransparent(getAdditionalimagespath() + File.separator + "CURSORS" + File.separator + "0.png"),
			new Point(bestSize.width / 2, bestSize.height / 2), "northCursor"));
		setSouthCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageUtils.makeImageTransparent(getAdditionalimagespath() + File.separator + "CURSORS" + File.separator + "180.png"),
			new Point(bestSize.width / 2, bestSize.height / 2), "southCursor"));
		setTargetCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		logger.info("finish: initializing cursors");
	}

	public static BufferedImage getCursorImageNorth()
	{
		return cursorImageNorth;
	}

	public static void setCursorImageNorth(BufferedImage cursorImageNorth)
	{
		CursorUtils.cursorImageNorth = cursorImageNorth;
	}

	public static BufferedImage getCursorImageEast()
	{
		return cursorImageEast;
	}

	public static void setCursorImageEast(BufferedImage cursorImageEast)
	{
		CursorUtils.cursorImageEast = cursorImageEast;
	}

	public static BufferedImage getCursorImageSouth()
	{
		return cursorImageSouth;
	}

	public static void setCursorImageSouth(BufferedImage cursorImageSouth)
	{
		CursorUtils.cursorImageSouth = cursorImageSouth;
	}

	public static BufferedImage getCursorImageWest()
	{
		return cursorImageWest;
	}

	public static void setCursorImageWest(BufferedImage cursorImageWest)
	{
		CursorUtils.cursorImageWest = cursorImageWest;
	}

	public static String getAdditionalimagespath()
	{
		return additionalImagesPath;
	}

	/**
	 * we want to limit the crosshairs to one tile if its melee
	 * how to do this:
	 * 1. calcuate center position (copy from move mouse to center)
	 * 2. depending on range of the weapon (1 for melee of course)
	 * do not allow mouse movement outside of it
	 * if its outside of the position check last position then go back?
	 * @param i
	 */
	public static void limitMouseMovementToRange(int i)
	{
		int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
		int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
		Point relativePoint = Game.getCurrent().getController().getGridCanvas().getLocationOnScreen();
		int playerCenterX = Px + relativePoint.x;
		int playerCenterY = Py + relativePoint.y;

		Range<Integer> rangeX = Range.between(playerCenterX - ((Game.getCurrent().getTileSize() * i) + (Game.getCurrent().getTileSize() / 2)),playerCenterX + (Game.getCurrent().getTileSize() * i) + (Game.getCurrent().getTileSize() / 2));
		Range<Integer> rangeY = Range.between(playerCenterY - ((Game.getCurrent().getTileSize() * i) + (Game.getCurrent().getTileSize() / 2)),playerCenterY + (Game.getCurrent().getTileSize() * i) + (Game.getCurrent().getTileSize() / 2));

		Point mousePosition = MouseInfo.getPointerInfo().getLocation();

		if (lastMousePosition == null)
		{
			lastMousePosition = MouseInfo.getPointerInfo().getLocation();
		}

		if (rangeX.contains(mousePosition.x) && (rangeY.contains(mousePosition.y)))
		{
			lastMousePosition = MouseInfo.getPointerInfo().getLocation();
		}
		else
		{
			//get back to last position
			moveMouse(lastMousePosition);
		}
	}


	public static void moveMouse(Point p)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		// Search the devices for the one that draws the specified point.
		for (GraphicsDevice device : gs)
		{
			GraphicsConfiguration[] configurations = device.getConfigurations();
			for (GraphicsConfiguration config : configurations)
			{
				Rectangle bounds = config.getBounds();
				if (bounds.contains(p))
				{
					// Set point to screen coordinates.
					Point b = bounds.getLocation();
					Point s = new Point(p.x - b.x, p.y - b.y);

					try
					{
						Robot r = new Robot(device);
						r.mouseMove(s.x, s.y);
					}
					catch (AWTException e)
					{
						e.printStackTrace();
					}

					return;
				}
			}
		}
		// Couldn't move to the point, it may be off screen.
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

	/**
	 * calculates where the mouse cursor is compared to the player icon (tile centered)
	 *   ALWAYS USE MouseInfo.getPointerInfo().getLocation() instead of the mouse event locations. These might be easier to do but are relative already.
	 *   sometimes, I have no event, therefore I need to construct the mouse position from the absolute and calculate.
	 * @param currentPlayer
	 * @param point
	 * 
	 *            https://stackoverflow.com/questions/40592495/how-to-create-custom-cursor-images-in-java I can redo this in pixels now instead of tiles which are incorrect anyhow :D
	 * 
	 */
	public static void calculateCursorFromGridPosition(AbstractEntity currentPlayer, Point point)
	{
		if (Game.getCurrent().getController().isSelectTile())
		{
			setCursor(getTargetCursor());
		}
		else
		{
			// this is the position on the grid, tile x and y + half tile size to each
			int Px = (currentPlayer.getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;
			int Py = (currentPlayer.getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;

			int Mx = point.x - Game.getCurrent().getController().getGridCanvas().getLocationOnScreen().x;
			int My = point.y - Game.getCurrent().getController().getGridCanvas().getLocationOnScreen().y;

			//logger.info("player x:{}, player y:{}, mouse x:{}, mouse y: {}", Px, Py, Mx, My);

			Range<Integer> rangeY = Range.between(Py - border, Py + border);
			Range<Integer> rangeX = Range.between(Px - border, Px + border);
			
			if (rangeY.contains(My) && ((Px - border) > Mx))
			{
				// logger.info("Mouse is on the left corridor");
				setCursor(getWestCursor());
				return;
			}

			else if (rangeY.contains(My) && ((Px + border) < Mx))
			{
				// logger.info("Mouse is on the right corridor");
				setCursor(getEastCursor());
				return;
			}

			else if (rangeX.contains(Mx) && ((Py - border) > My))
			{
				// logger.info("Mouse is on the top");
				setCursor(getNorthCursor());
				return;
			}

			else if (rangeX.contains(Mx) && ((Py + border) < My))
			{
				// logger.info("mouse is to the bottom");
				setCursor(getSouthCursor());
				return;
			}
			else
			{
				setCursor(Cursor.getDefaultCursor());
			}
		}
		return;
	}

	@Deprecated
	public static void createArrowCursorImages(Color color)
	{
		BufferedImage cI = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		for (int px1 = 0; px1 < (cI.getWidth()); px1++)
		{
			for (int px2 = 0; px2 < (cI.getHeight()); px2++)
			{
				cI.setRGB(px1, px2, Color.white.getRGB());
			}
		}

		int middle = 15;
		int startrow = 3;
		int endrow = 8;
		boolean start = false;
		int arrow = 0;
		boolean headdone = false;
		int shaft = 0;
		int shaftlength = 16;
		for (int px1 = 0; px1 < (cI.getHeight()); px1++)
		{
			if (start)
			{
				// logger.info("in start: {}", px1);
				for (int y = 0; y < endrow - startrow; y++)
				{
					cI.setRGB(middle + arrow, px1, color.getRGB());
					cI.setRGB(middle - arrow, px1, color.getRGB());
				}
				arrow++;
			}

			if (px1 == startrow)
			{
				// logger.info("starting: {}", px1);
				start = true;
			}

			// draw the line
			if (px1 == endrow + 1)
			{
				int b = middle + arrow;
				for (int a = middle - arrow + 1; a < b; a++)
				{
					cI.setRGB(a, px1, color.getRGB());
					cI.setRGB(a, px1, color.getRGB());
					headdone = true;
				}
			}

			if (px1 > endrow)
			{
				start = false;
			}

			// we have finished the arrow head, now for the shaft
			if (start == false && headdone == true)
			{
				if (shaft < shaftlength)
				{
					cI.setRGB(middle + 2, px1, color.getRGB());
					cI.setRGB(middle - 2, px1, color.getRGB());
				}
				if (shaft == shaftlength)
				{
					for (int y = 0; y < 3; y++)
					{
						cI.setRGB(middle + y, px1, color.getRGB());
						cI.setRGB(middle - y, px1, color.getRGB());
					}
				}
				shaft++;
			}

		}

		ImageIcon icon = new ImageIcon(cI);
		BufferedImage realImage = ImageUtils.makeImageTransparent(icon);

		String filePath = additionalImagesPath + File.separator + "CURSORS" + File.separator + "0" + ".png";
		File newFile = new File(filePath);
		newFile.getParentFile().mkdirs();
		try
		{
			// logger.info("writing image: {}, filePath: {}", img.toString(), filePath);
			ImageIO.write(realImage, "png", newFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		setCursorImageNorth(realImage);
	}

	/**
	 * https://stackoverflow.com/questions/8639567/java-rotating-images
	 * 
	 *
	 * @return roatedImagebydegree
	 */
	@Deprecated
	public static BufferedImage rotateCursorImage(BufferedImage img, int degree)
	{
		// The required drawing location
		BufferedImage cI = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		int drawLocationX = 0;
		int drawLocationY = 0;

		// Rotation information
		double rotationRequired = Math.toRadians(degree);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		Graphics2D g2d = img.createGraphics();
		// Drawing the rotated image at the required drawing locations
		g2d.drawImage(op.filter(img, cI), drawLocationX, drawLocationY, null);
		g2d.dispose();
		String filePath = additionalImagesPath + File.separator + "CURSORS" + File.separator + degree + ".png";
		File newFile = new File(filePath);
		newFile.getParentFile().mkdirs();
		try
		{
			ImageIO.write(cI, "png", newFile);
		}
		catch (IOException e)
		{
			logger.error("problem here: {}", e.toString());
			e.printStackTrace();
		}
		return cI;
	}

	public static Cursor getCursor()
	{
		return cursor;
	}

	public static void setCursor(Cursor cursor)
	{
		// logger.info("setting cursor to {}", cursor);
		CursorUtils.cursor = cursor;
		EventBus.getDefault().post(new CursorChangeEvent(cursor));
	}

	public static Cursor getWestCursor()
	{
		return westCursor;
	}

	public static void setWestCursor(Cursor westCursor)
	{
		CursorUtils.westCursor = westCursor;
	}

	public static Cursor getSouthCursor()
	{
		return southCursor;
	}

	public static void setSouthCursor(Cursor southCursor)
	{
		CursorUtils.southCursor = southCursor;
	}

	public static Cursor getEastCursor()
	{
		return eastCursor;
	}

	public static void setEastCursor(Cursor eastCursor)
	{
		CursorUtils.eastCursor = eastCursor;
	}

	public static Cursor getNorthCursor()
	{
		return northCursor;
	}

	public static void setNorthCursor(Cursor northCursor)
	{
		CursorUtils.northCursor = northCursor;
	}

	public static Cursor getTargetCursor()
	{
		return targetCursor;
	}

	public static void setTargetCursor(Cursor targetCursor)
	{
		CursorUtils.targetCursor = targetCursor;
	}

	
	public static Cursor createCustomCursorFromImage (Image img)
	{
		Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
		return Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(bestSize.width / 2, bestSize.height / 2), "item");
	}

	public static void centerCursorOnPlayer()
	{
		int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
		int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
		Point relativePoint = Game.getCurrent().getController().getGridCanvas().getLocationOnScreen();
		CursorUtils.moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
		Game.getCurrent().getController().setMouseOutsideOfGrid(false);
	}


}
