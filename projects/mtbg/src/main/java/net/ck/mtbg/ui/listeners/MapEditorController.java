package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.FurnitureItemPane;
import net.ck.mtbg.ui.components.MapTilePane;
import net.ck.mtbg.ui.components.NPCPane;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Getter
@Setter
@Log4j2
public class MapEditorController implements WindowListener, ActionListener
{
    private final MapEditorFrame mapEditorFrame;
    private MapTilePane mapTilePane;
    private NPCPane npcPane;
    private FurnitureItemPane furnitureItemPane;

    private boolean initiallyOpened = true;
    //TODO add selection item to check from what pane the selection is happening
    private Selection selectedItem;

    public MapEditorController(MapEditorFrame mapEditorFrame)
    {
        this.mapEditorFrame = mapEditorFrame;
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
}
