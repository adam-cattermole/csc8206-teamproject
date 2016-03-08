package route;

import java.util.ArrayList;
import java.util.List;

import backend.Block;
import backend.Signal;
import route.RouteBuilder.Route;
import utilities.Change;
import utilities.ChangeListener;
import utilities.ChangeType;
import utilities.Observable;

public class JourneyBuilder
{
	private static int Journey_ID = 0;
	private List<Route> path;
	
	public JourneyBuilder()
	{
		path = new ArrayList<Route>();
	}
	
	public int addToJourney(Route r)
	{
		path.add(r);
		return path.size();
	}
	
	public int addToJourney(List<Route> routes)
	{
		path.addAll(routes);
		return path.size();
	}
	
	public List<Route> getCurrentPath()
	{
		return path;
	}
	
	public int removeTrailingRoute()
	{
		path.remove(path.size() - 1);
		return path.size();
	}
	
	public Journey build()
	{
		if(validPath(path))
		{
			return new Journey(path);
		}
		else
		{
			throw new IllegalArgumentException("Invalid Journey");
		}
	}
	
	private boolean validPath(List<Route> path)
	{
		if (path.size() < 1) {
			return false;
		}
		
		for(int i = 0; i < path.size() - 1; i++)
		{
			Route a = path.get(i);
			Route b = path.get(i + 1);
			
			if(a.getDestination() != b.getSource())
			{
				return false;
			}
			
			if(a.getDirection() != b.getDirection())
			{
				return false;
			}
		}
		
		
		return true;
	}
		
	public class Journey implements Observable<Journey>
	{
		private List<Route> sequence;
		private final String ID;
		
		private final ChangeListener<Route> routeChangeListener;
		private final List<ChangeListener<Journey>> listeners = new ArrayList<ChangeListener<Journey>>();

		/*public Journey()
		{
			this(new ArrayList<Route>());
		}*/

		public Journey(List<Route> routes)
		{
			sequence = routes;
			ID = "J" + Journey_ID++;
			
			routeChangeListener = (change) -> {
				if (change.wasRemoved()) {
					listeners.stream().forEach(listener -> listener.onChange(new Change<Journey>(this, ChangeType.REMOVED)));
				}
			};
			
			sequence.stream().forEach(route -> {
				route.addChangeListener(routeChangeListener);
			});
		}

		public List<Route> getRoutes()
		{
			return sequence;
		}

		public String getID()
		{
			return ID;
		}

		public Signal getStartSignal()
		{
			return sequence.get(0).getSource();
		}
		
		public Signal getDestinationSignal()
		{
			return sequence.get(sequence.size() - 1).getDestination();
		}

		@Override
		public void addChangeListener(ChangeListener<Journey> listener) {
			listeners.add(listener);
		}

		@Override
		public void removeChangeListener(ChangeListener<Journey> listener) {
			listeners.remove(listener);
		}
	}
}
