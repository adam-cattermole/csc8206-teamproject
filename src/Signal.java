
public class Signal
{
	private final int id;
	private Direction direction = Direction.UP;
	
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
}
