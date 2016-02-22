
public class Point extends Block
{
	enum PointOrientation
	{
		UP, DOWN;
	}
	
	private Block sideline;
	private PointOrientation orientation;
	
	public Point(Section up, Section down, Block sideline, PointOrientation orientation)
	{
		super(up, down);
		this.sideline = sideline;
		this.orientation = orientation;
	}
	
	public void setSideline(Block sideline, PointOrientation orientation)
	{
		this.sideline = sideline;
		this.orientation = orientation;
	}
	
	public Block getSideline()
	{
		return sideline;
	}

	public PointOrientation getOrientation()
	{
		return orientation;
	}
	
}
