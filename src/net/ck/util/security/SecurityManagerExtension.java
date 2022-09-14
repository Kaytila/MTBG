package net.ck.util.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileDescriptor;

public class SecurityManagerExtension extends SecurityManager
{

	@Override
	public void checkRead(FileDescriptor fd)
	{
		//logger.info("reading file1: {}", fd.toString());
		super.checkRead(fd);
	}

	@Override
	public void checkRead(String file)
	{
		//logger.info("reading file2: {}", file);
		super.checkRead(file);
	}

	@Override
	public void checkRead(String file, Object context)
	{
		//logger.info("reading file3: {} in context {}", file, context.toString());
		super.checkRead(file, context);
	}

	@Override
	public void checkWrite(FileDescriptor fd)
	{
		// logger.info("writing file fd: {}", fd.toString());
		super.checkWrite(fd);
	}

	@Override
	public void checkWrite(String file)
	{
		// logger.info("writing file string: {}", file);
		super.checkWrite(file);
	}

	@Override
	public void checkDelete(String file)
	{
		// logger.info("deleting file: {}", file);
		super.checkDelete(file);
	}

	@SuppressWarnings("unused")
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public SecurityManagerExtension()
	{		

	}
}
