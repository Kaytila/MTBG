package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import org.apache.commons.lang3.Range;

import javax.swing.*;

@Getter
@Setter
@Log4j2
public class ProtoMapCanvas extends JComponent
{
    private final Range<Integer> rangeX = Range.of(0, GameConfiguration.numberOfTiles - 1);
    private final Range<Integer> rangeY = Range.of(0, GameConfiguration.numberOfTiles - 1);

    public ProtoMapCanvas()
    {
        //Todo add scrollbars
        //scroll one row or columns of tiles when clicked scrollbar
        //new paint method,
        //new updateProtoMapTile method
    }
}
