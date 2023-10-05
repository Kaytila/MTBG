package net.ck.mtbg.ui.models;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.table.AbstractTableModel;

public class SpellBookDataModel extends AbstractTableModel
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	AbstractSpell[] data = {new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell()};
	private String[] columnNames = {"left", "right"};

	@Override
	public int getRowCount()
	{
		return data.length;
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return data[rowIndex];
	}


	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col)
	{

	}
}
