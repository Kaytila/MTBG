package net.ck.game.old;

import java.awt.image.BufferedImage;

import net.ck.game.backend.Game;
import net.ck.game.graphics.TileTypes;
import net.ck.game.graphics.UnanimatedRepresentation;

/**
 * 
 * @author Claus extends unanimated representation by adding a tiletype to the
 *         image
 */
public class TileType extends UnanimatedRepresentation
{
	private TileTypes type;

	public TileType(BufferedImage img)
	{
		logger.error("tile type class unused");
		Game.getCurrent().stopGame();
	}

	public TileType()
	{
		logger.error("tile type class unused");
		Game.getCurrent().stopGame();
	}

	public TileTypes getType()
	{
		logger.error("tile type class unused");
		Game.getCurrent().stopGame();
		return this.type;
	}

	public void setType(TileTypes type)
	{
		logger.error("tile type class unused");
		Game.getCurrent().stopGame();
		this.type = type;
	}

}
