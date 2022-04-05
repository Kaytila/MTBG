package net.ck.util.communication.graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.entities.AbstractEntity;

import java.util.Objects;


public class AnimatedRepresentationChanged extends ChangedEvent {

	private final Logger logger = LogManager.getLogger(getRealClass());
	private AbstractEntity player;


	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}
	
	public AnimatedRepresentationChanged(AbstractEntity p)
	{
		this.player = p;
	}

	public AbstractEntity getPlayer()
	{
		return this.player;
	}

	public void setPlayer(AbstractEntity player)
	{
		this.player = player;
	}

	public Logger getLogger()
	{
		return logger;
	}
	
}
