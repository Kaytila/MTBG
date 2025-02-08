package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.FurnitureItemPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Log4j2
@Getter
@Setter
public class FurnitureItemPaneListener implements ListSelectionListener
{
    FurnitureItemPane furnitureItemPane;

    public FurnitureItemPaneListener(FurnitureItemPane furnitureItemPane)
    {
        this.furnitureItemPane = furnitureItemPane;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        Selection.setSelectedItem(getFurnitureItemPane().getModel().getElementAt(e.getLastIndex()));
    }
}
