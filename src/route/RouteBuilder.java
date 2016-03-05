package route;

import java.util.ArrayList;
import java.util.List;

import backend.Block;
import backend.Section;
import backend.Signal;

/**
 *
 * @author chris_000
 *
 * This class produces objects that can be used to form the
 * final parameter for the Route constructor over time
 *
 * Once the path has been completed, there is a build method
 * which then creates the Route itself, returning it.
 *
 * The RouteBuilder object resets after this build call
 */
public class RouteBuilder
{
	private static int ROUTE_NO = 1;
	private List<Block> path = new ArrayList<Block>();

	public int addToRoute(Block b)
	{
		path.add(b);
		return path.size();
	}

	public int addToRoute(List<Block> blocks)
	{
		path.addAll(blocks);
		return path.size();
	}

	public List<Block> getCurrentPath()
	{
		return new ArrayList<Block>(path);
	}

	public Route build()
	{
		return new Route(path);
	}

	public class Route
	{
		private final String id;

		private final List<Block> path;
		private final Section startBlock;
		private final Section endBlock;

		private final Signal source;
		private final Signal destination;

		private final List<String> points = new ArrayList<String>();
		private final List<String> signals = new ArrayList<String>();
		private final List<String> conflicts = new ArrayList<String>();

		private Route(List<Block> path)
		{
			if(path.size() < 2)
			{
				throw new IllegalArgumentException("Insufficient blocks for a route");
			}
			
			Block start = path.get(0);
			Block end = path.get(path.size()-1);
			
			if (!(start instanceof Section && end instanceof Section)) {
				throw new IllegalArgumentException("First and last block of a route must be a section");
			}

			id = "R" + ROUTE_NO++;
			this.path = path;//TODO Defensive copying; make programming easier too
			
			startBlock = (Section) start;
			endBlock = (Section) end;
			
			//detect the direction based on next block
			if (startBlock.getUp().equals(path.get(1))) {
				//we are going in up direction
				source = startBlock.getSignalUp();
				destination = endBlock.getSignalUp();
			} else {
				//we are going in down direction
				source = startBlock.getSignalDown();
				destination = endBlock.getSignalDown();
			}
		}

		public String getId() {
			return id;
		}
		
		public List<Block> getPath()
		{
			return path;
		}

		public Block getStartBlock()
		{
			return startBlock;
		}

		public Block getEndBlock()
		{
			return endBlock;
		}
		
		public Signal getSource()
		{
			return source;
		}
		
		public Signal getDestination()
		{
			return destination;
		}
		
		public List<String> getPoints()
		{
			return points;
		}
		
		public List<String> getSignals()
		{
			return signals;
		}
		
		public List<String> getConflicts()
		{
			return conflicts;
		}
		
		public void calculateSettings()
		{
			//calculate interlocking settings here
		}

		public boolean validateRoute()
		{
			//validate route based on conflicts
			return true;
		}
	}
}
