package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.MapUtils;

import java.awt.*;

@Log4j2
@Getter
@Setter
@ToString
/**
 * the idea of the class is to extract mapPosition and UIPosition from AbstractEntity or MapTile
 * and just have this encapsulated and also the UI position calculation. It is always correct and there
 * then in the end.
 * Lets see whether I try to do that in the end.
 */
public class MapPosition
{
    private Point mapPosition;
    private Point uiPosition;

    /**
     * overloaded setter that will automatically translate the mapPosition to the UIPosition
     *
     * @param mapPosition the new map position
     */
    public void setMapPosition(Point mapPosition)
    {
        this.mapPosition = mapPosition;
        setUiPosition(MapUtils.calculateUIPositionFromMapOffset(mapPosition));
    }
}
