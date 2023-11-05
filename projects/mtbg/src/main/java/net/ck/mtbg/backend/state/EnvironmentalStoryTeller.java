package net.ck.mtbg.backend.state;

import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.MessageTypes;
import net.ck.mtbg.ui.dialogs.AbstractDialog;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class EnvironmentalStoryTeller
{
	private static final Logger logger = LogManager.getLogger(EnvironmentalStoryTeller.class);
	private static ArrayList<MapTile> noRepeatMapTilesLeave = new ArrayList<>();
	private static ArrayList<MapTile> noRepeatMapTilesEnter = new ArrayList<>();

	public static void tellStoryLeave(MapTile tile)
	{
		if (tile.getMessage() != null)
		{
			if (tile.getMessage().getMessageType().equals(MessageTypes.LEAVE))
			{
				if (tile.getMessage().isRepeat())
				{
					if (UIStateMachine.isUiOpen())
					{
						AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
					}
					logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
				}
				else
				{
					if (noRepeatMapTilesLeave.contains(tile))
					{
						logger.debug("maptile: {}: dont repeat message", tile);
					}
					else
					{
						if (UIStateMachine.isUiOpen())
						{
							AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
						}
						logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
						noRepeatMapTilesLeave.add(tile);
					}
				}
			}
		}
	}


	public static void tellStoryEnter(MapTile tile)
	{
		if (tile.getMessage() != null)
		{
			if (tile.getMessage().getMessageType().equals(MessageTypes.ENTER))
			{
				if (tile.getMessage().isRepeat())
				{
					if (UIStateMachine.isUiOpen())
					{
						AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
					}
					logger.debug("maptile: {} : enter message: {}", tile, tile.getMessage().getDescription());
				}
				else
				{
					if (noRepeatMapTilesEnter.contains(tile))
					{
						logger.debug("maptile: {}: dont repeat message", tile);
					}
					else
					{
						if (UIStateMachine.isUiOpen())
						{
							AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
						}
						logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
						noRepeatMapTilesEnter.add(tile);
					}
				}
			}
		}
	}
}



