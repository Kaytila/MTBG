package net.ck.util.communication.graphics;

import net.ck.game.backend.entities.LifeForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class AnimatedRepresentationChanged extends ChangedEvent {

	private final Logger logger = LogManager.getLogger(getRealClass());
	private LifeForm player;


	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}
	
	public AnimatedRepresentationChanged(LifeForm p)
	{
		this.player = p;
	}

	public LifeForm getPlayer()
	{
		return this.player;
	}

	public void setPlayer(LifeForm player)
	{
		this.player = player;
	}

	public Logger getLogger()
	{
		return logger;
	}
	
}
