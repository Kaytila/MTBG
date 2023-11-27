package net.ck.mtbg.ui.components;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class SpellBookDataModelDataListener implements ListDataListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	@Override
	public void intervalAdded(ListDataEvent e)
	{
		logger.info("intervaladded");
	}

	@Override
	public void intervalRemoved(ListDataEvent e)
	{
		logger.info("intervalRemoved");
	}

	@Override
	public void contentsChanged(ListDataEvent e)
	{
		logger.info("contentsChanged");
	}
}
