package net.ck.game.demos;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.ck.util.ImageUtils;

public class TestDnD
{

	public static void main(String[] args)
	{
		new TestDnD();
	}

	public TestDnD()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (ClassNotFoundException ex)
				{
				}
				catch (InstantiationException ex)
				{
				}
				catch (IllegalAccessException ex)
				{
				}
				catch (UnsupportedLookAndFeelException ex)
				{
				}

				JFrame frame = new JFrame("Test");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

		});
	}

	public class TestPane extends JPanel
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JList<User> list;

		public TestPane()
		{
			setLayout(new BorderLayout());
			list = new JList<User>();
			DefaultListModel<User> model = new DefaultListModel<User>();
			model.addElement(new User("Shaun"));
			model.addElement(new User("Andy"));
			model.addElement(new User("Luke"));
			model.addElement(new User("Han"));
			model.addElement(new User("Liea"));
			model.addElement(new User("Yoda"));
			list.setModel(model);
			add(new JScrollPane(list), BorderLayout.WEST);

			DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureHandler(list));

			JPanel panel = new JPanel(new GridBagLayout());
			add(panel);

			DropTarget dt = new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetHandler(panel), true);

		}

	}

	public static class User
	{

		private String name;

		public User(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}

	}

	public static class UserTransferable implements Transferable
	{

		public static final DataFlavor USER_DATA_FLAVOR = new DataFlavor(User.class, "User");
		private User user;

		public UserTransferable(User user)
		{
			this.user = user;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]
			{USER_DATA_FLAVOR};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return USER_DATA_FLAVOR.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
		{
			Object value = null;
			if (USER_DATA_FLAVOR.equals(flavor))
			{
				value = user;
			}
			else
			{
				throw new UnsupportedFlavorException(flavor);
			}
			return value;
		}

	}

	protected class DragGestureHandler implements DragGestureListener
	{

		private JList<User> list;

		public DragGestureHandler(JList<User> list)
		{
			this.list = list;
		}

		@Override
		public void dragGestureRecognized(DragGestureEvent dge)
		{
			Object selectedValue = list.getSelectedValue();
			if (selectedValue instanceof User)
			{
				User user = (User) selectedValue;
				Transferable t = new UserTransferable(user);
				DragSource ds = dge.getDragSource();
				String additionalImagesPath = "graphics" + File.separator + "misc";
				Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
				Cursor curs = Toolkit.getDefaultToolkit().createCustomCursor(ImageUtils.makeImageTransparent(additionalImagesPath + File.separator + "CURSORS" + File.separator + "270.png"),
					new Point(bestSize.width / 2, bestSize.height / 2), "westCursor");
				ds.startDrag(dge, curs, t, new DragSourceHandler());
			}

		}

	}

	protected class DragSourceHandler implements DragSourceListener
	{

		public void dragEnter(DragSourceDragEvent dsde)
		{
		}

		public void dragOver(DragSourceDragEvent dsde)
		{
		}

		public void dropActionChanged(DragSourceDragEvent dsde)
		{
		}

		public void dragExit(DragSourceEvent dse)
		{
		}

		public void dragDropEnd(DragSourceDropEvent dsde)
		{

			System.out.println("Drag ended...");

		}

	}

	protected class DropTargetHandler implements DropTargetListener
	{

		private JPanel panel;

		public DropTargetHandler(JPanel panel)
		{
			this.panel = panel;
		}

		public void dragEnter(DropTargetDragEvent dtde)
		{
			if (dtde.getTransferable().isDataFlavorSupported(UserTransferable.USER_DATA_FLAVOR))
			{
				System.out.println("Accept...");
				dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
			}
			else
			{
				System.out.println("Drag...");
				dtde.rejectDrag();
			}
		}

		public void dragOver(DropTargetDragEvent dtde)
		{
		}

		public void dropActionChanged(DropTargetDragEvent dtde)
		{
		}

		public void dragExit(DropTargetEvent dte)
		{
		}

		public void drop(DropTargetDropEvent dtde)
		{
			System.out.println("Dropped...");
			if (dtde.getTransferable().isDataFlavorSupported(UserTransferable.USER_DATA_FLAVOR))
			{
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(UserTransferable.USER_DATA_FLAVOR))
				{
					try
					{
						Object transferData = t.getTransferData(UserTransferable.USER_DATA_FLAVOR);
						if (transferData instanceof User)
						{
							User user = (User) transferData;
							dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
							panel.add(new JLabel(user.getName()));
							panel.revalidate();
							panel.repaint();
						}
						else
						{
							dtde.rejectDrop();
						}
					}
					catch (UnsupportedFlavorException ex)
					{
						ex.printStackTrace();
						dtde.rejectDrop();
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
						dtde.rejectDrop();
					}
				}
				else
				{
					dtde.rejectDrop();
				}
			}
		}

	}

}