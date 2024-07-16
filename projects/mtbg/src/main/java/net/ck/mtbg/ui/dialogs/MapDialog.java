package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.components.AutoMapCanvas;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class MapDialog extends AbstractDialog
{
    public MapDialog(Frame owner, String title, boolean modal)
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

        AutoMapCanvas autoMapCanvas = new AutoMapCanvas(Game.getCurrent().getCurrentMap());
        autoMapCanvas.setVisible(true);
        this.add(autoMapCanvas);
        addButtons();
        this.setVisible(true);
    }
}
