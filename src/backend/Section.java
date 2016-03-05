package backend;

import java.util.Arrays;

public class Section extends Block
{
	protected Signal signalDown = new Signal(); //Signal on the left side of a section
	protected Signal signalUp = new Signal(); //Signal on the right side of a section
	
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
	
	public boolean hasSignalUp()
	{
		return signalUp != null;
	}
	
	public boolean hasSignalDown()
	{
		return signalDown != null;
	}
	
	public Signal getSignalUp()
	{
		/*if (signalUp == null &&
			up != null &&  //if next block is not null
			(up instanceof Section || (up instanceof Point && ((Point)up).getOrientation() == Point.Orientation.DOWN))) //next block must either be a section, or be a >- Point
		{
			signalUp = new Signal();
		}*/
		return signalUp;
	}
	
	public Signal getSignalDown()
	{
		/*if (signalDown == null &&
			down != null &&  //if previous block is not null
			(down instanceof Section || (down instanceof Point && ((Point)down).getOrientation() == Point.Orientation.UP))) //prev block must either be a section, or be a -< Point
		{
			signalDown = new Signal();
		}*/
		return signalDown;
	}
	
	public String getSignals()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("{SignalDown:" + getSignalDown());
		builder.append(", SignalUp:" + getSignalUp()).append("}");
		
		return builder.toString();
	}
	
	public String toString()
	{
		return "Section[" + super.toString() + "]";
	}
	
	public int compareTo(Block b)
	{
		if (getID() > b.getID())
		{
			return +1;
		} else if (getID() < b.getID()) {
			return -1;
		} else {
			if (b instanceof Section)
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
		return (other != null && other instanceof Section && ((Section)other).getID() == getID());
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new Object[]{new Integer(getID()), new Integer(1)}); //1 - Section
	}
	
	/**
	 * Section is valid if:
	 * it has 1 or 2 neighbours
	 */
	public boolean isValid()
	{
		int neighboursSize = getNeighbours().size();
		return (neighboursSize == 1 || neighboursSize == 2);
	}
}