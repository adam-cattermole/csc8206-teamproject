package route;
import backend.Block;
import java.util.List;
import java.util.ArrayList;

public class Route
{
	private static int ROUTE_NO = 1;
	private final String routeID;
	private final List<Block> sequence;
	private final Block start;
	private final Block end;
	
	/*public Route(Block[] path)
	{
		if(path.length < 2)
		{
			throw new IllegalArgumentException("Insufficient blocks for a route");
		}
		
		routeID = "R" + ROUTE_NO++;
		start = path[0];
		end = path[path.length - 1];
		
		sequence = new ArrayList<Block>();
		for(Block b : path){sequence.add(b);}
	}*/
	
	public Route(List<Block> path)
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
	
	public List<Block> getRoute()
	{
		return sequence;
	}
	
	public Block getStart()
	{
		return start;
	}
	
	public Block getEnd()
	{
		return end;
	}
	
	public static RouteBuilder getRouteBuilder()
	{
		return new RouteBuilder();
	}
	
	public static boolean validateRoute()
	{
		/*
		 * Unsure on the rules needed to validate a route
		 * Until then, all routes are assumed to be valid
		 */
		return true;
	}
	
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
	static class RouteBuilder
	{
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
	}
	
}
