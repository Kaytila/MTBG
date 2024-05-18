package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class MapPosition
{

    private Point mapPosition;
    private Point uiPosition;

}
