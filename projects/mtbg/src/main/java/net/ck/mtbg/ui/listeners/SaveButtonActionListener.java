package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.controllers.MapEditorController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Log4j2
@Getter
@Setter
public class SaveButtonActionListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        File start = new File(GameConfiguration.mapEditorLocation);
        JFileChooser fileChooser = new JFileChooser(start);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML only", "xml");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(MapEditorController.getCurrent().getMapEditorFrame());
        logger.debug("return value: {}", returnValue);

        File saveFile = fileChooser.getSelectedFile();
        MapEditorApplication.getCurrent().saveFile(saveFile);

    }
}
