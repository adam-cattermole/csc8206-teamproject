package backend;

public class Signal
{
	enum Setting
	{
		CLEAR, STOP
	}
	
	private final int id;
	private Direction direction = Direction.UP;
	private Setting setting = Setting.STOP;
	
	public Signal()
	{
		id = Identifiers.getSignalID();
	}
	
	public Signal(int id)
	{
		this.id = id;
	}
	
	public int getID()
	{
		return id; 
	}
	
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void setSetting(Setting setting)
	{
		this.setting = setting;
	}
	
	public Setting getSetting()
	{
		return setting;
	}
}
