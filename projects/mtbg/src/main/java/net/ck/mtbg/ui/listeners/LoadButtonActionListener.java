package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Log4j2
@Getter
@Setter
public class LoadButtonActionListener implements ActionListener
{


    public LoadButtonActionListener()
    {

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        File start = new File(GameConfiguration.mapEditorLocation);
        JFileChooser fileChooser = new JFileChooser(start);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML only", "xml");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(MapEditorController.getCurrent().getMapEditorFrame());
        logger.debug("return value: {}", returnValue);
        File file = fileChooser.getSelectedFile();
        boolean ret = MapEditorApplication.getCurrent().loadFile(file);

        /**
         * file was successfully loaded - repaint the bloody thing
         */
        if (ret == true)
        {
            MapEditorController.getCurrent().updateUIAfterLoadingMap();
        }
    }
}
