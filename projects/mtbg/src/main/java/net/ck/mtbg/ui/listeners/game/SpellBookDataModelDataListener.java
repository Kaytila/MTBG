package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

@Log4j2
@Getter
@Setter
public class SpellBookDataModelDataListener implements ListDataListener
{
    @Override
    public void intervalAdded(ListDataEvent e)
    {
        logger.info("interval added");
    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {
        logger.info("interval Removed");
    }

    @Override
    public void contentsChanged(ListDataEvent e)
    {
        logger.info("contents Changed");
    }
}
