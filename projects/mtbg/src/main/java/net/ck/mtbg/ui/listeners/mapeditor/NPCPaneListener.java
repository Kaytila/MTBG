package net.ck.mtbg.ui.listeners.mapeditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.NPCPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Log4j2
@Getter
@Setter
public class NPCPaneListener implements ListSelectionListener
{
    NPCPane npcPane;

    public NPCPaneListener(NPCPane npcPane)
    {
        this.npcPane = npcPane;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        MapEditorListsSelection.setSelectedItem(getNpcPane().getModel().getElementAt(e.getLastIndex()));
    }
}
