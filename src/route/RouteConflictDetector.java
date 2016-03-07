package route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backend.Block;
import route.RouteBuilder.Route;

public class RouteConflictDetector
{
	public static List<String> calculateConflicts(List<Route> routes)
	{
		Map<String, Set<String>> routeConflicts = new HashMap<String, Set<String>>();
		
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
		
		for (Route r : routes)
		{
			routeConflicts.put(r.getId(), r.getConflicts());
		}
		
		for(int i = 0; i < routes.size() - 1; i++)
		{
			for(int j = i + 1; j < routes.size(); j++)
			{
				List<Block> a = routes.get(i).getPath();
				List<Block> b = routes.get(j).getPath();
				
				if(!Collections.disjoint(a, b))
				{
					routeConflicts.get(routes.get(i).getId()).add(routes.get(j).getId());
					routeConflicts.get(routes.get(j).getId()).add(routes.get(i).getId());
				}
			}
		}
		
		//set the conflict sets back in each route
		for (Route r : routes) {
			r.setConflicts(routeConflicts.get(r.getId()));
		}
		
		return new ArrayList<String>();
	}
}
