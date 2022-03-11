package net.ck.game.demos;

public class LoggerUtils
{

	@SuppressWarnings("rawtypes")
	public static String getRealClassName(Class c)
	{
		Class<?> enclosingClass = c.getClass().getEnclosingClass();

		if (enclosingClass != null)
		{
			return (enclosingClass.getName());
		} else
		{
			return (c.getClass().getName());
		}
	}
}
