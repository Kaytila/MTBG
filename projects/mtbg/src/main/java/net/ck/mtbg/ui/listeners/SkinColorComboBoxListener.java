package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.models.CharacterPortraitModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@Log4j2
@Getter
@Setter
public class SkinColorComboBoxListener implements ItemListener, ActionListener
{
    CharacterPortraitModel characterPortraitModel;

    public SkinColorComboBoxListener(CharacterPortraitModel characterPortraitModel)
    {
        this.characterPortraitModel = characterPortraitModel;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        logger.debug("item: {}", e.getItem());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComboBox cb = (JComboBox) e.getSource();
        Color eyeColor = (Color) cb.getSelectedItem();
        logger.debug("selected item: {}", eyeColor);
        characterPortraitModel.setSkinColor(eyeColor);
        characterPortraitModel.updateUI();
    }
}