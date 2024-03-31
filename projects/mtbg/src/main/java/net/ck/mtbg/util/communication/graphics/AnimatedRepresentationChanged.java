package net.ck.mtbg.util.communication.graphics;

import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AnimatedRepresentationChanged extends ChangedEvent
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private LifeForm player;



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
}
