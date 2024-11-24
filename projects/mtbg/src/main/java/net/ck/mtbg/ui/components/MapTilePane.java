package net.ck.mtbg.ui.components;

import net.ck.mtbg.map.ProtoMapTile;

import javax.swing.*;

public class MapTilePane extends JList<ProtoMapTile>
{

    public MapTilePane(ListModel<ProtoMapTile> dataModel)
    {
        super(dataModel);
    }
}
