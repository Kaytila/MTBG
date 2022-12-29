package net.ck.game.demos;

import net.ck.util.CodeUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFieldDemo extends JFrame implements DocumentListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	class CancelAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ev)
		{
			hilit.removeAllHighlights();
			entry.setText("");
			entry.setBackground(entryBg);
		}
	}

	final static String CANCEL_ACTION = "cancel-search";
	final static Color ERROR_COLOR = Color.PINK;
	final static Color HILIT_COLOR = Color.LIGHT_GRAY;
	public static void main(String args[])
	{
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				new TextFieldDemo().setVisible(true);
			}
		});
	}
	private JTextField entry;

	final Color entryBg;
	final Highlighter hilit;
	private JLabel jLabel1;

	private JScrollPane jScrollPane1;
	final Highlighter.HighlightPainter painter;

	private JLabel status;

	private JTextArea textArea;

	public TextFieldDemo()
	{
		initComponents();

		File initialFile = new File("content.txt");
		InputStream in = null;
		try
		{
			in = FileUtils.openInputStream(initialFile);
		}
		catch (IOException e1)
		{			
			e1.printStackTrace();
		}

		try
		{
			textArea.read(new InputStreamReader(in), null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		hilit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
		textArea.setHighlighter(hilit);

		entryBg = entry.getBackground();
		entry.getDocument().addDocumentListener(this);

		InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = entry.getActionMap();
		im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
		am.put(CANCEL_ACTION, new CancelAction());
	}

	public void changedUpdate(DocumentEvent ev)
	{
	}

	// DocumentListener methods

	/**
	 * This method is called from within the constructor to initialize the form.
	 */

	private void initComponents()
	{
		entry = new JTextField();
		textArea = new JTextArea();
		status = new JLabel();
		jLabel1 = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("TextFieldDemo");

		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		jScrollPane1 = new JScrollPane(textArea);

		jLabel1.setText("Enter text to search:");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		// Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

		// Add a container gap to the sequential group h1
		h1.addContainerGap();

		// Add a scroll pane and a label to the parallel group h2
		h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

		// Create a sequential group h3
		SequentialGroup h3 = layout.createSequentialGroup();
		h3.addComponent(jLabel1);
		h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		h3.addComponent(entry, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);

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
		v2.addComponent(jLabel1);
		v2.addComponent(entry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		// Add the group v2 tp the group v1
		v1.addGroup(v2);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(status);
		v1.addContainerGap();

		// Add the group v1 to the group vGroup
		vGroup.addGroup(v1);
		// Create the vertical group
		layout.setVerticalGroup(vGroup);
		pack();
	}

	public void insertUpdate(DocumentEvent ev)
	{
		search();
	}

	void message(String msg)
	{
		status.setText(msg);
	}

	public void removeUpdate(DocumentEvent ev)
	{
		search();
	}

	public void search()
	{
		hilit.removeAllHighlights();

		String s = entry.getText();
		if (s.length() <= 0)
		{
			message("Nothing to search");
			return;
		}

		String content = textArea.getText();
		int index = content.indexOf(s, 0);
		if (index >= 0)
		{ // match found
			try
			{
				int end = index + s.length();
				hilit.addHighlight(index, end, painter);
				textArea.setCaretPosition(end);
				entry.setBackground(entryBg);
				message("'" + s + "' found. Press ESC to end search");
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			entry.setBackground(ERROR_COLOR);
			message("'" + s + "' not found. Press ESC to start a new search");
		}
	}
}
