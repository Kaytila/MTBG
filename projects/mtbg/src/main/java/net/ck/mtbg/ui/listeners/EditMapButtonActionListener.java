package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.dialogs.DialogFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class EditMapButtonActionListener implements ActionListener
{

    public EditMapButtonActionListener()
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        logger.info("open map edit dialog");
        DialogFactory.createDialog(MapEditorController.getCurrent().getMapEditorFrame(), "Edit Map", false, null, null, null);
    }
}
