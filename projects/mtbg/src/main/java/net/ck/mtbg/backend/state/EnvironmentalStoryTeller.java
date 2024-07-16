package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.MessageTypes;
import net.ck.mtbg.ui.dialogs.DialogFactory;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.ui.WindowBuilder;

import java.util.ArrayList;

@Log4j2
@ToString
public class EnvironmentalStoryTeller
{

    @Getter
    @Setter
    private static ArrayList<MapTile> noRepeatMapTilesLeave = new ArrayList<>();

    @Getter
    @Setter
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
                        DialogFactory.createDialog(WindowBuilder.getFrame(), "Message", false, null, tile.getMessage(), null);
                    }
                    if (GameConfiguration.debugEnvironmentalStoryTeller == true)
                    {
                        logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
                    }
                }
                else
                {
                    if (noRepeatMapTilesLeave.contains(tile))
                    {
                        if (GameConfiguration.debugEnvironmentalStoryTeller == true)
                        {
                            logger.debug("maptile: {}: dont repeat message", tile);
                        }
                    }
                    else
                    {
                        if (UIStateMachine.isUiOpen())
                        {
                            DialogFactory.createDialog(WindowBuilder.getFrame(), "Message", false, null, tile.getMessage(), null);
                        }
                        if (GameConfiguration.debugEnvironmentalStoryTeller == true)
                        {
                            logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
                        }
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
                        DialogFactory.createDialog(WindowBuilder.getFrame(), "Message", false, null, tile.getMessage(), null);
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
                            DialogFactory.createDialog(WindowBuilder.getFrame(), "Message", false, null, tile.getMessage(), null);
                        }
                        logger.debug("maptile: {} : leave message: {}", tile, tile.getMessage().getDescription());
                        noRepeatMapTilesEnter.add(tile);
                    }
                }
            }
        }
    }
}



