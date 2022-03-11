package net.ck.game.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Food extends AbstractItem
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private int nutritionValue;
	
	
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

	public Logger getLogger()
	{
		return logger;
	}
	public Food()
	{
		// TODO Auto-generated constructor stub
	}

	public int getNutritionValue()
	{
		return nutritionValue;
	}

	public void setNutritionValue(int nutritionValue)
	{
		this.nutritionValue = nutritionValue;
	}
}
