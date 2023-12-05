package net.ck.mtbg.ui.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;

import javax.swing.table.AbstractTableModel;

@Getter
@Setter
@Log4j2
public class SpellBookDataModel extends AbstractTableModel
{
	AbstractSpell[] data = {new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell(), new AbstractSpell()};
	private String[] columnNames = {"left", "right"};

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex];
	}


	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col) {

	}
}
