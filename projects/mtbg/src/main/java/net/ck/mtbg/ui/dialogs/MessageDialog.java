package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.map.Message;
import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class MessageDialog extends AbstractDialog
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public MessageDialog(Frame owner, String title, boolean modal, Message message1)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        this.setUndecorated(true);

        TextField textField = new TextField();
        textField.setBounds(0, 0, 300, 200);
        textField.setText(message1.getDescription());
        root.add(textField);

        cancelButton = new CancelButton();
        okButton = new OKButton();
        okButton.setBounds(300 - 160, 300 - 70, 70, 30);
        cancelButton.setBounds(300 - 90, 300 - 70, 70, 30);
        this.add(cancelButton);
        this.add(okButton);
        this.setVisible(true);
    }


}
