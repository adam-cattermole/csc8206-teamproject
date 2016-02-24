
public class Section extends Block
{
	protected Signal signalUp;
	protected Signal signalDown;
	
	public Section()
	{
		super(null, null, Identifiers.getSectionID());
	}
	
	public Section(int id)
	{
		super(null, null, id);
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