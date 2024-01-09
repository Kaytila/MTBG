package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class Message
{

	private String description;
	private boolean repeat;
	private MessageTypes messageType;

}
