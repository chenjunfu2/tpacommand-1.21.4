package chenjunfu2.tpacommand.util;

import chenjunfu2.tpacommand.TpDirection;

public class PlayerTpaData
{
	Long time;
	TpDirection direction;
	
	public PlayerTpaData(Long time, TpDirection direction)
	{
		this.time = time;
		this.direction = direction;
	}
	
	public Long getTime()
	{
		return time;
	}
	
	public void setTime(Long time)
	{
		this.time = time;
	}
	
	public TpDirection getDirection()
	{
		return direction;
	}
	
	public void setDirection(TpDirection direction)
	{
		this.direction = direction;
	}
}