package net.ck.mtbg.ui.components;

import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.ui.listeners.mapeditor.FurnitureItemPaneListener;
import net.ck.mtbg.ui.renderers.FurnitureItemPaneListCellRenderer;

import javax.swing.*;

public class FurnitureItemPane extends JList<FurnitureItem>
{
    public FurnitureItemPane(DefaultListModel<FurnitureItem> furnitureItemList)
    {
        super(furnitureItemList);
        FurnitureItemPaneListCellRenderer furnitureItemPaneRenderer = new FurnitureItemPaneListCellRenderer();
        this.setCellRenderer(furnitureItemPaneRenderer);

        FurnitureItemPaneListener furnitureItemPaneListener = new FurnitureItemPaneListener(this);
        this.addListSelectionListener(furnitureItemPaneListener);
    }
}
