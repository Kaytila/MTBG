package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.dialogs.DialogFactory;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class EditMapButtonActionListener implements ActionListener
{
    MapEditorFrame mapEditorFrame;

    public EditMapButtonActionListener(MapEditorFrame mapEditorFrame)
    {
        this.mapEditorFrame = mapEditorFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        logger.info("open map edit dialog");
        DialogFactory.createDialog(getMapEditorFrame(), "Edit Map", false, null, null, null);
    }
}
