package chenjunfu2.tpacommand.tpa;

public class PlayerTpaData
{
	Long time;
	TpaDirection direction;
	
	public PlayerTpaData(Long time, TpaDirection direction)
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
	
	public TpaDirection getDirection()
	{
		return direction;
	}
	
	public void setDirection(TpaDirection direction)
	{
		this.direction = direction;
	}
}