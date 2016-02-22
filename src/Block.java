/**
 * Top level abstract class for Blocks, components of a network
 * Allows for abstraction of components in networks
 * @author chris_curry
 *
 */
public abstract class Block
{
	private static int blockID = 0;
	private final int ID;
	private Block up;
	private Block down;
	
	public Block(Block up, Block down)
	{
		ID = blockID++;
		this.up = up;
		this.down = down;
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
	
	protected void setUp(Block up)
	{
		this.up = up;
	}
	
	protected void setDown(Block down)
	{
		this.down = down;
	}
}
