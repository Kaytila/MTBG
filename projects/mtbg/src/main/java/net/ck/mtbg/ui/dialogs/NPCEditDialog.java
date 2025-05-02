package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.NPCProperty;
import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.LabeledEntryField;
import net.ck.mtbg.ui.components.LabeledEntryFieldFactory;
import net.ck.mtbg.ui.controllers.MapEditorController;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class NPCEditDialog extends AbstractDialog
{

    public NPCEditDialog(JFrame owner, String title, boolean modal)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);

        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        this.add(content);


        for (NPCProperty property : MapEditorController.getCurrent().getMapEditorCanvas().getSelectedTile().getLifeForm().getProperties())
        {
            logger.debug("property: {}", property);
            LabeledEntryField field = LabeledEntryFieldFactory.createDefault(property);
            if (field != null)
            {
                content.add(field);
            }
        }


        cancelButton = new CancelButton();
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getActionCommand().equalsIgnoreCase("Cancel"))
                {
                    logger.info("Cancel");
                    NPCEditDialog.this.dispose();
                    UIStateMachine.setDialogOpened(false);
                }
            }
        });
        okButton = new OKButton();
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getActionCommand().equalsIgnoreCase("OK"))
                {
                    logger.info("OK - figure this out later");
                    //TODO add actual logic here what to do with the Editor contents

                    NPCEditDialog.this.dispose();
                    UIStateMachine.setDialogOpened(false);
                }
            }
        });
        content.add(cancelButton);
        content.add(okButton);

        content.add(
                // push content upwards,
                // general not really useful but for demo reasons enough
                new Box.Filler(
                        new Dimension(0, 0),
                        new Dimension(0, Short.MAX_VALUE),
                        new Dimension(0, Short.MAX_VALUE)
                )
        );

        this.add(new JScrollPane(content));
        this.setVisible(true);
    }

}
