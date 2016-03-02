package backend;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleNetwork implements Network
{
	private Set<Block> blocks = new HashSet<Block>();
	
	public SimpleNetwork()
	{
		Identifiers.reset();
		BlockFactory.reset();
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
		//get block neighbours
		Set<Block> neighbours = block.getNeighbours();
		
		for (Block neighbour : neighbours)
		{
			//detach the block from its neighbours
			neighbour.deleteBlock(block);
		}
		
		//release the IDs held by the block
		Identifiers.addToBlockPool(block);
		
		//finally remove the block from blocks set
		blocks.remove(block);
		
		return this;
	}
	
	public Collection<Block> getBlocks()
	{
		return blocks;
	}
	
	public Point makePoint(Point.Orientation orientation)
	{
		Point point = new Point(orientation);
		blocks.add(point);
		return point;
	}
	
	public Section makeSection()
	{
		Section section = new Section();
		blocks.add(section);
		return section;
	}
	
	@JsonIgnore
	public boolean isValid()
	{
		Set<Block> traversed = traverse();
		
		if (traversed.containsAll(blocks))
		{
			return true;
		}
		
		return false;
	}
	
	public SimpleNetwork save(OutputStream stream) throws NetworkSerializationException
	{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			jsonMapper.writerWithDefaultPrettyPrinter().writeValue(stream, this);
			return this;
		} catch (IOException e) {
			throw new NetworkSerializationException(e);
		}
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
				sb.append("Section[" + block.getID() + "] neighbours: " + block.getNeighbours() + ", signals: " + ((Section)block).getSignals() + "\n");
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
