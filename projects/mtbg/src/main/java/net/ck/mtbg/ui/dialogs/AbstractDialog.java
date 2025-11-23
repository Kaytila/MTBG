package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.buttons.general.CancelButton;
import net.ck.mtbg.ui.buttons.general.OKButton;
import net.ck.mtbg.util.communication.keyboard.framework.WindowClosingAction;

import javax.swing.*;
import java.awt.event.KeyEvent;

@Log4j2
@Getter
@Setter
public class AbstractDialog extends JDialog
{

    public static final String dispatchWindowClosingActionMapKey = "WINDOW_CLOSING";
    protected static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public CancelButton cancelButton;
    public OKButton okButton;
    protected JRootPane root;


    public AbstractDialog()
    {

    }


    public AbstractDialog(JFrame owner, String title, boolean modal)
    {
        super(owner, title, true);
        setTitle(title);
        this.setBounds(0, 0, 300, 300);

        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    public void addButtons()
    {
        cancelButton = new CancelButton();
        okButton = new OKButton();
        okButton.setBounds(getWidth() - 160, getHeight() - 70, 70, 30);
        cancelButton.setBounds(getWidth() - 90, getHeight() - 70, 70, 30);
        this.add(cancelButton);
        this.add(okButton);
    }

    /**
     * ideally the buttons are right aligned at the lower right corner of the dialog.
     * but this does not work for some reason
     *
     * @return a JPanel right aligned with two buttons.
     */
    public JPanel addButtonswithLayout()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        cancelButton = new CancelButton();
        okButton = new OKButton();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

}
