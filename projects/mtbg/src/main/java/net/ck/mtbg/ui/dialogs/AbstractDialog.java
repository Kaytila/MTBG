package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.map.Message;
import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@Log4j2
@Getter
@Setter
public class AbstractDialog extends JDialog
{

    protected static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final String dispatchWindowClosingActionMapKey = "WINDOW_CLOSING";
    public CancelButton cancelButton;
    public OKButton okButton;
    protected JRootPane root;


    public AbstractDialog() {

    }

    public static AbstractDialog createDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action) {
        //redundant but just to be sure
        if (UIStateMachine.isDialogOpened() == true) {
            return null;
        }

        //redundant but just to be sure
        if (UIStateMachine.isSelectTile() == true) {
            return null;
        }


        UIStateMachine.setDialogOpened(true);
        switch (title) {
            case "Z-Stats": {
                //dialog.addButtons();
                return new StatsDialog(owner, title, modal);
            }

            case "Inventory": {
                //dialog.addButtons();
                return new InventoryDialog(owner, title, modal, action);
            }

            case "Equipment": {
                return new EQDialog(owner, title, modal);
            }

            case "Spellbook": {
                return new SpellDialog(owner, title, modal, action);
            }

            default: {
                throw new IllegalArgumentException("not expected value during Dialog Creation: " + title);
            }
        }
    }

    public AbstractDialog(Frame owner, String title, boolean modal) {
        super(owner, title, true);
        setTitle(title);
        this.setBounds(0, 0, 300, 300);

        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    public static AbstractDialog createDialog(Frame owner, String title, boolean modal, Message message1) {
        //redundant but just to be sure
        if (UIStateMachine.isDialogOpened() == true) {
            return null;
        }

        //redundant but just to be sure
        if (UIStateMachine.isSelectTile() == true) {
            return null;
        }
        UIStateMachine.setDialogOpened(true);
        return new MessageDialog(owner, title, modal, message1);
    }


    public void addButtons() {
        cancelButton = new CancelButton();
        okButton = new OKButton();
        okButton.setBounds(getWidth() - 160, getHeight() - 70, 70, 30);
        cancelButton.setBounds(getWidth() - 90, getHeight() - 70, 70, 30);
    }

    public static TalkDialog createDialog(JFrame frame, String string, boolean b, AbstractKeyboardAction currentAction, LifeForm n) {
        //redundant but just to be sure
        if (UIStateMachine.isDialogOpened() == true) {
            return null;
        }

        //redundant but just to be sure
        if (UIStateMachine.isSelectTile() == true) {
            return null;
        }
        UIStateMachine.setDialogOpened(true);
        return new TalkDialog(frame, string, b, null, n);
    }
}
