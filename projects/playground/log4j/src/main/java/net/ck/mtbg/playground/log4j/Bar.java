package net.ck.mtbg.playground.log4j;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bar
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public boolean doIt()
	{
		logger.entry();
		logger.error("Did it again!");
		return logger.exit(false);
	}
}
