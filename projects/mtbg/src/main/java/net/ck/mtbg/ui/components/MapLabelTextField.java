package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.listeners.MapLabelTextFieldListener;

import javax.swing.*;

@Log4j2
@Getter
@Setter
@ToString
public class MapLabelTextField extends JTextField
{
    public MapLabelTextField(AutoMapCanvas autoMapCanvas)
    {
        super();
        setHorizontalAlignment(JTextField.CENTER);
        MapLabelTextFieldListener mapLabelTextFieldListener = new MapLabelTextFieldListener(this, autoMapCanvas);
        this.addFocusListener(mapLabelTextFieldListener);
        this.addKeyListener(mapLabelTextFieldListener);
        this.addMouseListener(mapLabelTextFieldListener);
        this.addMouseMotionListener(mapLabelTextFieldListener);
    }
}
