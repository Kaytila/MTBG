package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class TextList extends JTextArea
{
    private final int numberOfLines = 15;
    private JScrollPane sp;


    public TextList()
    {
        super();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setVisible(true);
        this.setFont(GameConfiguration.font);
        this.setFocusable(false);
        this.setAutoscrolls(true);
        this.setBorder(blackline);
        //this.setToolTipText(getLogger().getName());
    }


    public JScrollPane initializeScrollPane()
    {
        sp = new JScrollPane(this);
        sp.setBounds(GameConfiguration.UIwidth - 200, 200, 150, (GameConfiguration.lineHight * numberOfLines));
        sp.setVisible(true);
        return sp;
    }
}
