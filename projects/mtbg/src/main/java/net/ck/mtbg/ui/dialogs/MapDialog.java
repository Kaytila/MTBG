package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.map.AutoMap;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.ui.components.game.AutoMapCanvas;
import net.ck.mtbg.ui.listeners.game.AutoMapCanvasMouseListener;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Log4j2
@Getter
@Setter
public class MapDialog extends AbstractDialog
{
    public MapDialog(JFrame owner, String title, boolean modal, Map map)
    {
        setTitle(title);
        this.setBounds(0, 0, 500, 500);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        this.setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 500, 500);
        panel.setLayout(null);
        this.setContentPane(panel);

        AutoMap autoMap = null;
        boolean found = false;
        for (AutoMap m : Game.getCurrent().getAutomaps())
        {
            if (m.getName().equals(map.getName()))
            {
                logger.debug("automap already exists");
                found = true;
                autoMap = m;
                break;
            }
        }

        if (!(found))
        {
            autoMap = new AutoMap(map);
        }

        AutoMapCanvas autoMapCanvas = new AutoMapCanvas(autoMap);
        autoMapCanvas.addMouseListener(new AutoMapCanvasMouseListener(autoMapCanvas));
        autoMapCanvas.setVisible(true);
        this.add(autoMapCanvas);
        addButtons();
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e)
            {

            }

            public void windowClosing(WindowEvent e)
            {
                System.out.println("jdialog window closing event received");

            }
        });
    }
}
