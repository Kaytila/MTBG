package net.ck.mtbg.ui.mainframes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.CharacterCanvas;
import net.ck.mtbg.ui.components.CharacterTinyCanvas;
import net.ck.mtbg.ui.listeners.CharacterEditorController;
import net.ck.mtbg.ui.listeners.EyeColorComboBoxListener;
import net.ck.mtbg.ui.listeners.GenderComboBoxListener;
import net.ck.mtbg.ui.listeners.HairColorComboBoxListener;
import net.ck.mtbg.ui.models.CharacterPortraitColor;
import net.ck.mtbg.ui.models.CharacterPortraitModel;
import net.ck.mtbg.ui.renderers.ComboBoxRenderer;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class CharacterEditorFrame extends JFrame
{
    CharacterPortraitModel characterPortraitModel;
    CharacterEditorController characterEditorController;
    CharacterCanvas characterCanvas;
    CharacterTinyCanvas characterTinyCanvas;
    JComboBox hairColorComboBox;
    JComboBox eyeColorComboBox;
    JComboBox genderComboBox;
    JLabel hairColorLabel;
    JLabel eyeColorLabel;
    JLabel genderLabel;
    JLabel characterCanvasLabel;
    JLabel characterTinyCanvasLabel;

    public CharacterEditorFrame() throws HeadlessException
    {
        this.characterEditorController = new CharacterEditorController(this);
        //this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.setVisible(true);
        this.setLayout(null);
        this.addWindowListener(characterEditorController);

        characterPortraitModel = new CharacterPortraitModel();

        ComboBoxRenderer renderer = new ComboBoxRenderer();


        genderLabel = new JLabel("Gender");
        genderLabel.setBounds(400, 20, 100, 20);
        this.add(genderLabel);

        genderComboBox = new JComboBox<>();
        genderComboBox.setBounds(400, 50, 100, 20);
        genderComboBox.addItem("male");
        genderComboBox.addItem("female");
        GenderComboBoxListener genderComboBoxListener = new GenderComboBoxListener(characterPortraitModel);
        genderComboBox.addActionListener(genderComboBoxListener);
        genderComboBox.addItemListener(genderComboBoxListener);
        this.add(genderComboBox);

        hairColorLabel = new JLabel("Hair Color");
        hairColorLabel.setBounds(400, 150, 100, 20);
        this.add(hairColorLabel);

        hairColorComboBox = new JComboBox<>();
        hairColorComboBox.setBounds(400, 200, 100, 20);
        hairColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLACK));
        hairColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.YELLOW));
        hairColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.DARK_GRAY));
        hairColorComboBox.setSelectedIndex(0);
        HairColorComboBoxListener hairColorComboBoxListener = new HairColorComboBoxListener(characterPortraitModel);
        hairColorComboBox.addActionListener(hairColorComboBoxListener);
        hairColorComboBox.addItemListener(hairColorComboBoxListener);
        hairColorComboBox.setRenderer(renderer);
        this.add(hairColorComboBox);

        eyeColorLabel = new JLabel("Eye Color");
        eyeColorLabel.setBounds(400, 300, 100, 20);
        this.add(eyeColorLabel);

        eyeColorComboBox = new JComboBox<>();
        eyeColorComboBox.setBounds(400, 350, 100, 20);
        eyeColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLACK));
        eyeColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.GREEN));
        eyeColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLUE));
        eyeColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.RED));
        eyeColorComboBox.addItem(CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.DARK_GRAY));
        eyeColorComboBox.setSelectedIndex(0);
        EyeColorComboBoxListener eyeColorComboBoxListener = new EyeColorComboBoxListener(characterPortraitModel);
        eyeColorComboBox.addItemListener(eyeColorComboBoxListener);
        eyeColorComboBox.addActionListener(eyeColorComboBoxListener);
        eyeColorComboBox.setRenderer(renderer);
        this.add(eyeColorComboBox);


        characterCanvasLabel = new JLabel("Character Portrait");
        characterCanvasLabel.setBounds(10, 0, 150, 20);
        this.add(characterCanvasLabel);

        characterCanvas = new CharacterCanvas(characterPortraitModel);
        characterCanvas.setBounds(10, 50, 100, 100);
        this.add(characterCanvas);

        characterTinyCanvasLabel = new JLabel("Character View");
        characterTinyCanvasLabel.setBounds(10, 230, 150, 20);
        this.add(characterTinyCanvasLabel);

        characterTinyCanvas = new CharacterTinyCanvas(characterPortraitModel);
        characterTinyCanvas.setBounds(10, 280, GameConfiguration.tileSize * 5, GameConfiguration.tileSize * 5);
        this.add(characterTinyCanvas);

        characterPortraitModel.setCharacterCanvas(characterCanvas);
        characterPortraitModel.setCharacterTinyCanvas(characterTinyCanvas);
    }
}
