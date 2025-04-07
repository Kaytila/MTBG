package net.ck.mtbg.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

@Log4j2
@Getter
@Setter
public class LabeledEntryFieldDemo
{
    public static void main(String[] args)
    {
        runDemo();
    }

    private static void runDemo()
    {
        final var initFrameDefaults = (Consumer<JFrame>) frame ->
        {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLocationRelativeTo(null); // centered
        };

        final var initFrameContent = (Consumer<JFrame>) frame ->
        {
            final var content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            for (int i = 1; i <= 5; ++i)
            {
                content.add(new LabeledEntryField(
                        "label ".repeat(i).trim(),
                        "value ".repeat(i).trim()
                ));
            }

            content.add(
                    // push content upwards,
                    // general not really useful but for demo reasons enough
                    new Box.Filler(
                            new Dimension(0, 0),
                            new Dimension(0, Short.MAX_VALUE),
                            new Dimension(0, Short.MAX_VALUE)
                    )
            );

            // will have always a vertical scrollbar because of the Short.MAX filler
            frame.add(new JScrollPane(content));
        };

        SwingUtilities.invokeLater(() ->
        {
            final var frame = new JFrame("LabeledEntryField");
            initFrameDefaults.accept(frame);
            initFrameContent.accept(frame);
            frame.setVisible(true);
        });
    }

    public static final class LabeledEntryField
            extends JComponent
    {
        private final JLabel labelHolder = new JLabel();
        private final JTextField valueHolder = new JTextField();

        {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(labelHolder);
            add(valueHolder);
        }

        public LabeledEntryField()
        {

        }

        public LabeledEntryField(
                final String label,
                final String value)
        {
            setLabel(label);
            setValue(value);
        }

        private static String _toLabelString(
                final String label)
        {
            if (label == null)
            {
                return null;
            }
            final var cleaned = label.trim();
            if (cleaned.endsWith(":"))
            {
                return cleaned;
            }
            return cleaned + ":";
        }

        public String getLabel()
        {
            return labelHolder.getText();
        }

        public void setLabel(
                final String label)
        {
            labelHolder.setText(_toLabelString(label));
        }

        public String getValue()
        {
            return valueHolder.getText();
        }

        public void setValue(
                final String value)
        {
            valueHolder.setText(value);
        }
    }
}

