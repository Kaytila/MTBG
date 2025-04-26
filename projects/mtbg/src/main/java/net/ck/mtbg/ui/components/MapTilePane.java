package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.ProtoMapTile;
import net.ck.mtbg.ui.listeners.mapeditor.MapTilePaneListener;
import net.ck.mtbg.ui.renderers.MapTilePaneListRenderer;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class MapTilePane extends JList<ProtoMapTile>
{

    public MapTilePane(ListModel<ProtoMapTile> dataModel)
    {
        setModel(dataModel);
        setAutoscrolls(true);
        MapTilePaneListener mapTilePaneListener = new MapTilePaneListener();

        this.addListSelectionListener(mapTilePaneListener);
        this.setVisibleRowCount(5);
        this.setLayoutOrientation(JList.VERTICAL);
        this.setForeground(Color.YELLOW);
        this.setBackground(Color.BLUE);
        this.setVisible(true);
        this.setCellRenderer(new MapTilePaneListRenderer());
        this.setDragEnabled(true);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.invalidate();
    }
}
