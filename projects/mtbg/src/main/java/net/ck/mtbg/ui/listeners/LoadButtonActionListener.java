package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;
import net.ck.mtbg.util.xml.RunXMLParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Log4j2
@Getter
@Setter
public class LoadButtonActionListener implements ActionListener
{
    MapEditorFrame mapEditorFrame;
    MapEditorCanvas mapEditorCanvas;

    public LoadButtonActionListener(MapEditorFrame frame, MapEditorCanvas canvas)
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

        int returnValue = fileChooser.showOpenDialog(mapEditorFrame);
        logger.debug("return value: {}", returnValue);
        File file = fileChooser.getSelectedFile();
        if (file != null)
        {
            logger.debug("file: {}", file);
            Map map = RunXMLParser.parseMap(file.getPath());

            mapEditorCanvas.setMap(map);
            //mapEditorCanvas.setMinimumSize(new Dimension(map.getSize().x * GameConfiguration.tileSize, map.getSize().y * GameConfiguration.tileSize));
            mapEditorCanvas.setPreferredSize(new Dimension(map.getSize().x * GameConfiguration.tileSize, map.getSize().y * GameConfiguration.tileSize));
            mapEditorCanvas.getParent().setPreferredSize(mapEditorCanvas.getPreferredSize());
            logger.debug("parent: {}", mapEditorCanvas.getParent());

            mapEditorCanvas.getParent().setMinimumSize(mapEditorCanvas.getPreferredSize());
            mapEditorCanvas.getParent().getParent().setMinimumSize(mapEditorCanvas.getPreferredSize());
            mapEditorFrame.setMinimumSize(new Dimension(1500, 1000));
            mapEditorCanvas.repaint();
            mapEditorFrame.repaint();
        }
    }
}
