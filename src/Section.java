
public class Section extends Block
{
	public Section()
	{
		super(null, null, Identifiers.getSectionID());
	}
	
	public Section(Block up, Block down)
	{
		super(up, down, Identifiers.getSectionID());
	}
	
	public String toString()
	{
		return "Section[" + super.toString() + "]";
	}
}