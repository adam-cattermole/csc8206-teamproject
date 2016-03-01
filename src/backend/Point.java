package backend;
import java.util.Arrays;
import java.util.Set;

public class Point extends Block
{
	public enum Orientation
	{
		UP, // -<
		DOWN // >-
	}
	
	public enum Setting
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
	
	public int compareTo(Block b)
	{
		if (getID() > b.getID())
		{
			return +1;
		} else if (getID() < b.getID()) {
			return -1;
		} else {
			if (b instanceof Point)
			{
				return 0;
			} else {
				return +1;
			}
		}
	}
	
	@Override
	public boolean equals(Object other)
	{
		return (other != null && other instanceof Point && ((Point)other).getID() == getID());
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[]{new Integer(getID()), new Integer(2)}); //1 - Point
	}
	
	/**
	 * Point is valid if:
	 * it has 3 neighbours, those neighbours must be Sections
	 */
	public boolean isValid()
	{
		Set<Block> neighbours = getNeighbours();
		
		//check if we have 3 neighbours
		if (neighbours.size() != 3)
		{
			return false;
		}
		
		//check if all of the neighbours are of type Section
		for (Block b : neighbours)
		{
			if (!(b instanceof Section))
			{
				return false;
			}
		}
		
		return false;
	}
}