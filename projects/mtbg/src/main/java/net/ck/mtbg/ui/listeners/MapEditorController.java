package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.MapEditorApplication;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.buttons.EditMapButton;
import net.ck.mtbg.ui.buttons.LoadButton;
import net.ck.mtbg.ui.buttons.SaveButton;
import net.ck.mtbg.ui.components.FurnitureItemPane;
import net.ck.mtbg.ui.components.MapEditorCanvas;
import net.ck.mtbg.ui.components.MapTilePane;
import net.ck.mtbg.ui.components.NPCPane;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
    //private JScrollBar horizontalScrollbar;
    //private JScrollBar verticalScrollbar;
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
    private Selection selectedItem;


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
        //TODO
        //why does this not properly update the SplitPane based on the preferred size?
        mapEditorCanvas.setPreferredSize(new Dimension(MapEditorApplication.getCurrent().getMap().getSize().x * GameConfiguration.tileSize, MapEditorApplication.getCurrent().getMap().getSize().y * GameConfiguration.tileSize));
        //mapEditorCanvas.getParent().setPreferredSize(mapEditorCanvas.getPreferredSize());
        //mapEditorCanvas.getParent().setMinimumSize(mapEditorCanvas.getPreferredSize());
        //mapEditorCanvas.getParent().getParent().setMinimumSize(mapEditorCanvas.getPreferredSize());
        //mapEditorFrame.setMinimumSize(new Dimension(1500, 1000));
        //mapEditorCanvas.repaint();
        //mapEditorFrame.repaint();
        //splitPane.repaint();
        splitPane.resetToPreferredSizes();
    }
}
