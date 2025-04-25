package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.controllers.MapEditorController;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Log4j2
@Getter
@Setter
public class MapTilePaneListener implements ListSelectionListener
{
    @Override
    /**
     *  handles the change in item for the maptile pane to make sure the selection object is updated properly all the time.
     *  but it also serves a second purpose:
     *  translation: selection has changed, control please scroll to the selected item
     *  this triggers the scrollbar to be calculated correctly upon creation?
     *  weird?
     */
    public void valueChanged(ListSelectionEvent e)
    {
        //last index contains the value as there is only one active.
        logger.info("value changed: {} {}", e.getFirstIndex(), e.getLastIndex());
        logger.info("selected maptile: {}", MapEditorController.getCurrent().getMapTilePane().getModel().getElementAt(e.getLastIndex()).getName());
        Selection.setSelectedItem(MapEditorController.getCurrent().getMapTilePane().getModel().getElementAt(e.getLastIndex()));
        //TODO
        //ASKSIMON
        MapEditorController.getCurrent().getMapTilePane().ensureIndexIsVisible(e.getLastIndex());
    }
}
