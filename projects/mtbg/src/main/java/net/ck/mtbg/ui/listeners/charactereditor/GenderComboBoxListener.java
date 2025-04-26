package net.ck.mtbg.ui.listeners.charactereditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.models.CharacterPortraitModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@Log4j2
@Getter
@Setter
public class GenderComboBoxListener implements ItemListener, ActionListener
{
    CharacterPortraitModel characterPortraitModel;

    public GenderComboBoxListener(CharacterPortraitModel characterPortraitModel)
    {
        this.characterPortraitModel = characterPortraitModel;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        //logger.debug("item: {}", e.getItem());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComboBox cb = (JComboBox) e.getSource();
        String eyeColor = (String) cb.getSelectedItem();
        characterPortraitModel.setGender(eyeColor);
        characterPortraitModel.updateUI();
    }
}