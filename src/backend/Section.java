package backend;
public class Section extends Block
{
	protected Signal signalUp; //Signal on the right side of a section
	protected Signal signalDown; //Signal on the left side of a section
	
	public Section()
	{
		super(null, null, Identifiers.getSectionID());
	}
	
	public Section(int id)
	{
		super(null, null, id);
	}
	
	public void setUp(Block up, boolean reverse)
	{
		super.setUp(up, reverse);
	}
	
	public void setDown(Block down, boolean reverse)
	{
		super.setDown(down, reverse);
	}
	
	public Section setSignalUp(Signal signalUp)
	{
		this.signalUp = signalUp;
		return this;
	}
	
	public Section setSignalDown(Signal signalDown)
	{
		this.signalDown = signalDown;
		return this;
	}
	
	public Signal getSignalUp()
	{
		return signalUp;
	}
	
	public Signal getSignalDown()
	{
		return signalDown;
	}
	
	public String toString()
	{
		return "Section[" + super.toString() + "]";
	}
}