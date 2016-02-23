
public class Signal
{
	private final int ID;
	private Direction direction = Direction.UP;
	
	public Signal()
	{
		ID = Identifiers.getSignalID();
	}
	
	public int getID()
	{
		return ID; 
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
