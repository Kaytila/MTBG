package net.ck.mtbg.backend.state;

import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.MessageTypes;
import net.ck.mtbg.ui.dialogs.AbstractDialog;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentalStoryTeller
{
	private static final Logger logger = LogManager.getLogger(EnvironmentalStoryTeller.class);


	public static void tellStoryLeave(MapTile tile)
	{
		if (tile.getMessage() != null)
		{
			if (tile.getMessage().getMessageType().equals(MessageTypes.LEAVE))
			{
				if (UIStateMachine.isUiOpen())
				{
					AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
				}
				logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
			}
		}
	}


	public static void tellStoryEnter(MapTile tile)
	{
		if (tile.getMessage() != null)
		{
			if (tile.getMessage().getMessageType().equals(MessageTypes.ENTER))
			{
				if (UIStateMachine.isUiOpen())
				{
					AbstractDialog.createDialog(WindowBuilder.getFrame(), "Message", false, tile.getMessage());
				}
				logger.debug("maptile: {} : enter message: {}", tile, tile.getMessage().getDescription());
			}
		}
	}

}



