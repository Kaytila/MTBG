package net.ck.mtbg.ui.components.mapeditor;

import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.ui.listeners.mapeditor.NPCPaneListener;
import net.ck.mtbg.ui.renderers.mapeditor.NPCPaneListCellRenderer;

import javax.swing.*;

public class NPCPane extends JList<NPC>
{

    public NPCPane(DefaultListModel<NPC> npcList)
    {
        super(npcList);
        setCellRenderer(new NPCPaneListCellRenderer());
        NPCPaneListener npcPaneListener = new NPCPaneListener(this);

        this.addListSelectionListener(npcPaneListener);
    }
}
