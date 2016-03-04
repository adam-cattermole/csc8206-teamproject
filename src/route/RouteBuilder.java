package route;

import java.util.ArrayList;
import java.util.List;

import backend.Block;
import backend.Point;
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
	List<Block> path = new ArrayList<Block>();

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
		Route r = new Route(path);
		path = new ArrayList<Block>();
		return r;
	}
	
	public class Route
	{
		private final String routeID;
		private final List<Block> sequence;
		private final Block start;
		private final Block end;
		private final List<String> points = new ArrayList<String>();
		private final List<String> signals = new ArrayList<String>();
		
		private Route(List<Block> path)
		{
			if(path.size() < 2)
			{
				throw new IllegalArgumentException("Insufficient blocks for a route");
			}
			
			routeID = "R" + ROUTE_NO++;
			start = path.get(0);
			end = path.get(path.size() - 1);
			
			sequence = path;//TODO Defensive copying; make programming easier too
			
		}
		
		public String getId() {
			return routeID;
		}
		
		public Block getStart()
		{
			return start;
		}
		
		public Block getEnd()
		{
			return end;
		}
		
		public boolean validateRoute()
		{
			return true;
		}
	}
}
