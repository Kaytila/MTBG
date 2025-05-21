package net.ck.mtbg.ui.controllers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.buttons.mapeditor.EditMapButton;
import net.ck.mtbg.ui.buttons.mapeditor.LoadButton;
import net.ck.mtbg.ui.buttons.mapeditor.SaveButton;
import net.ck.mtbg.ui.components.mapeditor.FurnitureItemPane;
import net.ck.mtbg.ui.components.mapeditor.MapEditorCanvas;
import net.ck.mtbg.ui.components.mapeditor.MapTilePane;
import net.ck.mtbg.ui.components.mapeditor.NPCPane;
import net.ck.mtbg.ui.listeners.mapeditor.MapEditorListsSelection;
import net.ck.mtbg.ui.mainframes.mapeditor.MapEditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

@Getter
@Setter
@Log4j2
public class MapEditorController implements WindowListener, ActionListener
{
    private static final MapEditorController mapEditorController = new MapEditorController();
    private MapEditorFrame mapEditorFrame;
    private MapTilePane mapTilePane;
    private MapEditorCanvas mapEditorCanvas;
    private FurnitureItemPane furnitureItemPane;
    private JScrollPane scrollPane;
    private NPCPane npcPane;
    private LoadButton loadButton;
    private SaveButton saveButton;
    private EditMapButton editMapButton;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JSplitPane splitPane;

    private boolean initiallyOpened = true;
    //TODO add selection item to check from what pane the selection is happening
    private MapEditorListsSelection selectedItem;

    private int scrollPositionX = 0;
    private int scrollPositionY = 0;

    public MapEditorController()
    {

    }

    public static MapEditorController getCurrent()
    {
        return mapEditorController;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        File file = new File(GameConfiguration.mapFileRootPath + File.separator + "outpost.xml");
        boolean ret = MapEditorApplication.getCurrent().loadFile(file);
        MapEditorController.getCurrent().updateUIAfterLoadingMap();
        mapEditorFrame.getContentPane().repaint();
    }

    @Override
    public void windowClosing(WindowEvent e)
    {

    }

    @Override
    public void windowClosed(WindowEvent e)
    {

    }

    @Override
    public void windowIconified(WindowEvent e)
    {
        mapEditorFrame.getContentPane().repaint();
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
        mapEditorFrame.getContentPane().repaint();
    }

    @Override
    public void windowActivated(WindowEvent e)
    {

    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {

    }

    public void updateUIAfterLoadingMap()
    {
        Dimension preferredSize = new Dimension((MapEditorApplication.getCurrent().getMap().getSize().x * GameConfiguration.tileSize) + GameConfiguration.editorScrollbarSize, (MapEditorApplication.getCurrent().getMap().getSize().y * GameConfiguration.tileSize) + GameConfiguration.editorScrollbarSize);
        logger.debug("preferred size: {}", preferredSize);
        scrollPositionY = 0;
        scrollPositionX = 0;
        mapEditorCanvas.setPreferredSize(preferredSize);
        //mapEditorCanvas.setMinimumSize(preferredSize);
        leftPanel.setMinimumSize(preferredSize);
        leftPanel.setPreferredSize(preferredSize);
        //leftPanel.setPreferredSize(new Dimension(MapEditorApplication.getCurrent().getMap().getSize().x * GameConfiguration.tileSize, MapEditorApplication.getCurrent().getMap().getSize().y * GameConfiguration.tileSize));
        splitPane.resetToPreferredSizes();
    }

    public void calculateMaxMapSize()
    {
        int maxWidth = leftPanel.getPreferredSize().width;
        int maxHeight = leftPanel.getPreferredSize().height;

        int maxTilesWidth = (maxWidth - GameConfiguration.editorScrollbarSize) / GameConfiguration.tileSize;
        int maxTilesHeight = (maxHeight - GameConfiguration.editorScrollbarSize) / GameConfiguration.tileSize;

        logger.debug("left Panel preferred size: {}", leftPanel.getPreferredSize());
        logger.debug("maxTilesWidth: {}", maxTilesWidth);
        logger.debug("maxTilesHeight: {}", maxTilesHeight);

        if (MapEditorApplication.getCurrent().getMap().getSize().x > maxTilesWidth)
        {
            logger.debug("Canvas needs scrolling left-right because of {}", maxTilesWidth);
        }

        if (MapEditorApplication.getCurrent().getMap().getSize().y > maxTilesHeight)
        {
            logger.debug("Canvas needs scrolling up-down because of {}", maxTilesHeight);
        }
    }

    public void incrementScrollPositionY()
    {
        scrollPositionY++;
    }

    public void decrementScrollPositionY()
    {
        scrollPositionY--;
    }

    public void decrementScrollPositionX()
    {
        scrollPositionX++;
    }

    public void incrementScrollPositionX()
    {
        scrollPositionX--;
    }
}
