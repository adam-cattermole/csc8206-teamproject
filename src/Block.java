import java.util.HashSet;
import java.util.Set;

/**
 * Top level abstract class for Blocks, components of a network
 * Allows for abstraction of components in networks
 * @author chris_curry
 *
 */
public abstract class Block
{
	private final int ID;
	private Block up;
	private Block down;
	
	private Set<Block> neighbours;
	
	private Signal signalUp;
	private Signal signalDown;
	private Direction direction = Direction.UP;
	
	public boolean explored = false;
	
	protected Block(Block up, Block down, int id)
	{
		this.up = up;
		this.down = down;
		this.ID = id;
	}
	
	protected int getID()
	{
		return ID;
	}
	
	protected Block getUp()
	{
		return up;
	}
	
	protected Block getDown()
	{
		return down;
	}
	
	protected void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	
	protected Direction getDirection()
	{
		return direction;
	}
	
	protected Signal getSignalUp()
	{
		return signalUp;
	}
	
	protected Signal getSignalDown()
	{
		return signalDown;
	}
	
	protected void setUp(Block up)
	{
		setUp(up, true);
	}
	
	protected void setDown(Block down)
	{
		setDown(down, true);
	}
	
	protected void setUp(Block up, boolean reverse)
	{
		if (this.up == null)
		{
			this.up = up;
			if (reverse)
			{
				up.setDown(this, false);
			}
		}
	}
	
	protected void setDown(Block down, boolean reverse)
	{
		if (this.down == null)
		{
			this.down = down;
			if (reverse)
			{
				down.setUp(this, false);
			}
		}
	}
	
	protected void deleteBlock(Block block)
	{	
		if (down == block)
		{
			down = null;
		}
		
		if (up == block)
		{
			up = null;
		}
	}
	
	protected Set<Block> getNeighbours()
	{
		if (neighbours == null)
		{
			neighbours = new HashSet<Block>(3);
		}
		
		//fix it
		neighbours.clear();
		
		if (up != null)
		{
			neighbours.add(up);
		}
		
		if (down != null)
		{
			neighbours.add(down);
		}
		
		return neighbours;
	}
	
	public String toString()
	{
		return String.valueOf(ID);
	}
}
