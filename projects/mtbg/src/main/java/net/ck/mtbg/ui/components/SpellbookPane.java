package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.ui.dialogs.AbstractDialog;
import net.ck.mtbg.ui.listeners.game.SpellBookListener;
import net.ck.mtbg.ui.models.SpellBookListDataModel;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class SpellbookPane extends JList<AbstractSpell>
{
    private AbstractDialog parentDialog;
    private Frame owner;
    private SpellBookListener spellBookListener;

    public SpellbookPane(Frame owner, AbstractDialog dialog, AbstractKeyboardAction action)
    {
        super();
        this.setOwner(owner);
        this.setParentDialog(dialog);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setVisible(true);
        this.setFont(GameConfiguration.font);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLayoutOrientation(VERTICAL_WRAP);
        this.requestFocus();
        this.setVisibleRowCount(-1);
        setBounds(20, 40, 260, 100);
        spellBookListener = new SpellBookListener(this, action);
        this.addMouseListener(spellBookListener);
        this.addMouseMotionListener(spellBookListener);
        this.setModel(new SpellBookListDataModel());
    }
}

