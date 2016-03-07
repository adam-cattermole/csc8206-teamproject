package route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backend.Block;
import route.RouteBuilder.Route;

public class RouteConflictDetector
{
	public static List<String> calculateConflicts(List<Route> routes)
	{
		List<String> conflicts = new ArrayList<String>();
		
		
		if(routes.size() == 0)
		{
			return null;
			//IllegalArgument?
		}
		
		if(routes.size() == 1)
		{
			return null;
			//IllegalArgument
		}
		
		
		for(int i = 0; i < routes.size() - 1; i++)
		{
			for(int j = i + 1; j < routes.size(); j++)
			{
				List<Block> a = routes.get(i).getPath();
				List<Block> b = routes.get(j).getPath();
				
				if(!Collections.disjoint(a, b))
				{
					conflicts.add(routes.get(i).getId() + ";" + routes.get(j).getId());
					routes.get(i).addConflict(routes.get(j).getId());
					routes.get(j).addConflict(routes.get(i).getId());
				}
			}
		}
		return conflicts;
	}
}
