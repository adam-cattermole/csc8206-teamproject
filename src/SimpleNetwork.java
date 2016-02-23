import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SimpleNetwork
{
	Set<Block> blocks = new HashSet<Block>();
	
	public SimpleNetwork()
	{
		
	}
	
	public SimpleNetwork addBlock(Block block)
	{
		if (!blocks.contains(block))
		{
			blocks.add(block);
		}
		
		return this;
	}
	
	public SimpleNetwork removeBlock(Block block)
	{
		Set<Block> neighbours = block.getNeighbours();
		
		for (Block neighbour : neighbours)
		{
			neighbour.deleteBlock(block);
		}
		
		//finally remove the block from blocks set
		blocks.remove(block);
		
		return this;
	}
	
	/* 
	 * Need to add validation of each section and point:
	 * A track section may have 1 or 2 neighbouring blocks (a section at the end of a line will only have one neighbour); a point has 3 neighbouring blocks
	 */
	public boolean isValid()
	{
		Set<Block> traversed = traverse();
		
		if (traversed.containsAll(blocks))
		{
			return true;
		}
		
		return false;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		Iterator<Block> iterator = blocks.iterator();
		while (iterator.hasNext())
		{
			Block block = iterator.next();
			
			if (block instanceof Section)
			{
				sb.append("Section[" + block.getID() + "] neighbours: " + block.getNeighbours() + "\n");
			} else {
				sb.append("Point[" + block.getID() + "] neighbours: " + block.getNeighbours() + "\n");
			}
		}
		
		return sb.toString();
	}
	
	/*private Set<Block> traverseFrom(Block root)
	{
		return traverse(root, new HashSet<Block>());
	}*/
	
	private Set<Block> traverse()
	{
		Iterator<Block> iterator = blocks.iterator();
		Set<Block> traversed = new HashSet<Block>();
		
		if (iterator.hasNext())
		{
			Block root = iterator.next();
			return traverse(root, traversed);
		}
		
		return traversed;
	}
	
	private Set<Block> traverse(Block root, Set<Block> blocksDone)
	{
		//terminate
		if (blocksDone.contains(root))
		{
			return blocksDone;
		}
		
		blocksDone.add(root);
		
		for (Block neighbour : root.getNeighbours())
		{
			blocksDone = traverse(neighbour, blocksDone);
		}
		
		return blocksDone;
	}
}
