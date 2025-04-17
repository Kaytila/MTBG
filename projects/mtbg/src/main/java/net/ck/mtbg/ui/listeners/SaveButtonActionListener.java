package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditor;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;
import net.ck.mtbg.util.utils.MapUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

@Log4j2
@Getter
@Setter
public class SaveButtonActionListener implements ActionListener
{

    MapEditorFrame mapEditorFrame;
    MapEditorCanvas mapEditorCanvas;

    public SaveButtonActionListener(MapEditorFrame frame, MapEditorCanvas canvas)
    {
        this.mapEditorFrame = frame;
        this.mapEditorCanvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        File start = new File("C:\\Users\\Claus\\eclipse-workspace\\MyTurnBasedGame\\projects\\mtbg\\assets\\maps");
        JFileChooser fileChooser = new JFileChooser(start);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML only", "xml");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(mapEditorFrame);
        logger.debug("return value: {}", returnValue);
        File saveFile = fileChooser.getSelectedFile();
        MapEditor.getCurrent().getMap().setName(saveFile.getName());
        logger.debug("saveFile: {}", saveFile);

        try
        {
            MapUtils.writeMapToXML(MapEditor.getCurrent().getMap());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }

                /*try
                {
                    if (saveFile.createNewFile())
                    {
                        System.out.println("File created: " + saveFile.getName());
                    }
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
                */
    }
}
