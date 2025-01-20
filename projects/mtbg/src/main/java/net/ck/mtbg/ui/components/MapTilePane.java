package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.listeners.MapTilePaneListener;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public class MapTilePane extends JList<ProtoMapTile>
{
    ListModel<ProtoMapTile> dataModel;

    public MapTilePane(ListModel<ProtoMapTile> dataModel)
    {
        super(dataModel);
        this.dataModel = dataModel;
        MapTilePaneListener mapTilePaneListener = new MapTilePaneListener(this);
        this.addListSelectionListener(mapTilePaneListener);
    }
}
