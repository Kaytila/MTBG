package net.ck.game.items;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Food extends AbstractItem
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private int nutritionValue;
	
	

	public Food()
	{

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
