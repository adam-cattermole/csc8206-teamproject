package backend;
import java.util.Set;

public class Point extends Block
{
	enum Orientation
	{
		UP, // -<
		DOWN // >-
	}
	
	enum Setting
	{
		PLUS, //selects the mainline
		MINUS //selects the sideline
	}
	
	private Block sideline;
	private Orientation orientation;
	private Setting setting = Setting.PLUS;
	
	public Point()
	{
		super(null, null, Identifiers.getPointID());
	}
	
	public Point(int id)
	{
		super(null, null, id);
	}
	
	public Point(Orientation orientation)
	{
		super(null, null, Identifiers.getPointID());
		this.orientation = orientation;
	}
	
	public Point(Section up, Section down, Block sideline, Orientation orientation)
	{
		super(up, down, Identifiers.getPointID());
		this.sideline = sideline;
		this.orientation = orientation;
	}
	
	public void setSideline(Block sideline)
	{
		this.sideline = sideline;
		if (orientation == Orientation.UP)
		{
			sideline.setDown(this, false);
		} else {
			sideline.setUp(this, false);
		}
	}
	
	public Block getSideline()
	{
		return sideline;
	}

	public Orientation getOrientation()
	{
		return orientation;
	}
	
	public Point setOrientation(Orientation orientation)
	{
		this.orientation = orientation;
		return this;
	}
	
	public Point setSetting(Setting setting)
	{
		this.setting = setting;
		return this;
	}
	
	public Setting getSetting()
	{
		return setting;
	}
	
	public void deleteBlock(Block block)
	{
		if (sideline == block)
		{
			sideline = null;
		}
		
		super.deleteBlock(block);
	}
	
	public Set<Block> getNeighbours()
	{
		Set<Block> n = super.getNeighbours();
		
		if (sideline != null)
		{
			n.add(sideline);
		}
		
		return n;
	}
	
	public String toString()
	{
		return "Point[" + super.toString() + "]";
	}
	
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
	    result = (int) (prime * result + ((sideline == null) ? 0 : sideline.getID()));
	    result = (int) (prime * result + 2);
	    return result;
	}
}