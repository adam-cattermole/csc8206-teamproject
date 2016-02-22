
public class Section extends Block
{	
	public Section(Block up, Block down)
	{
		super(up, down);
	}

	public boolean addUp(Block newBlock)
	{
		if(getUp() == null)
		{
			setUp(newBlock);
			return true;
		}
		
		return false;		
	}
	
	public boolean addDown(Block newBlock)
	{
		if(getDown() == null)
		{
			setDown(newBlock);
			return true;
		}
		
		return false;
	}
	
	
}
