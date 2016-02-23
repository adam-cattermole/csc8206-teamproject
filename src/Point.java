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
	private final Orientation orientation;
	private Setting setting = Setting.PLUS;
	
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
	
	public void setSetting(Setting setting)
	{
		this.setting = setting;
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
}