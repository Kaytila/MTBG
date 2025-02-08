package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.MapTilePane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Log4j2
@Getter
@Setter
public class MapTilePaneListener implements ListSelectionListener
{

    MapTilePane mapTilePane;

    public MapTilePaneListener(MapTilePane pane)
    {
        this.mapTilePane = pane;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        //last index contains the value as there is only one active.
        logger.info("value changed: {} {}", e.getFirstIndex(), e.getLastIndex());
        logger.info("selected maptile: {}", getMapTilePane().getModel().getElementAt(e.getLastIndex()).getName());
        Selection.setSelectedItem(getMapTilePane().getModel().getElementAt(e.getLastIndex()));
    }
}
