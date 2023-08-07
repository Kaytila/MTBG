package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.backend.entities.entities.AbstractEntity;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.ui.listeners.InputFieldListener;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;

public class TalkDialog extends AbstractDialog
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private JTextArea textArea;
    private JTextField textField;
    private LifeForm   npc;
	
	public TalkDialog()
	{
	}

    public TalkDialog(Frame owner, String title, boolean modal, AbstractEntity target, LifeForm n)
    {
        setTitle(title);
        setNpc(n);
		this.setBounds(0, 200, 700, 700);
		this.setLayout(null);
        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
		root.setOpaque(false);

		JPanel panel = new JPanel();
		panel.setBounds(0, 200, 700, 700);
		panel.setLayout(null);
		this.setContentPane(panel);
		this.setUndecorated(true);
		this.setOpacity(0.3f);

		textField = new JTextField();
		textField.setBackground(Color.green);
		textField.setVisible(true);
		textField.requestFocus();
		textField.setFocusable(true);
		textField.setCaretPosition(0);
		
		this.add(textField);
		
		
		textArea = new JTextArea();
		textArea.setBackground(Color.blue);

		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setEnabled(false);
		JScrollPane sp = new JScrollPane(textArea);
		sp.setVisible(true);
		this.add(sp);

		textField.addActionListener(new InputFieldListener(this, textField, textArea, n));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		this.getContentPane().setLayout(layout);

		// Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

		// Add a container gap to the sequential group h1
		h1.addContainerGap();

		// Add a scroll pane and a label to the parallel group h2
		h2.addComponent(sp, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		

		// Create a sequential group h3
		SequentialGroup h3 = layout.createSequentialGroup();		
		h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		h3.addComponent(textField, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);

		// Add the group h3 to the group h2
		h2.addGroup(h3);
		// Add the group h2 to the group h1
		h1.addGroup(h2);

		h1.addContainerGap();

		// Add the group h1 to the hGroup
		hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
		// Create the horizontal group
		layout.setHorizontalGroup(hGroup);

		// Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		// Create a sequential group v1
		SequentialGroup v1 = layout.createSequentialGroup();
		// Add a container gap to the sequential group v1
		v1.addContainerGap();
		// Create a parallel group v2
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
	
		v2.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		// Add the group v2 tp the group v1
		v1.addGroup(v2);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(sp, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);		
		v1.addContainerGap();

		// Add the group v1 to the group vGroup
		vGroup.addGroup(v1);
		// Create the vertical group
		layout.setVerticalGroup(vGroup);
		pack();
		this.setVisible(true);
	}

	public Logger getLogger()
	{
		return logger;
	}

    public LifeForm getNpc()
    {
        return npc;
    }

    public void setNpc(LifeForm npc)
    {
        this.npc = npc;
    }
}
