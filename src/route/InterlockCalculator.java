package route;

import java.util.ArrayList;
import java.util.List;

import backend.Block;
import backend.Point;
import backend.Section;
import route.RouteBuilder.Route;

public class InterlockCalculator
{
	
	public static String[] calculateSettings(Route r)
	{
		List<String> sigSet = new ArrayList<String>();
		List<String> pointSet = new ArrayList<String>();
		
		List<Block> routeBlocks = r.getPath();
		
		Point.Orientation routeDirection;
		Block curr = routeBlocks.get(0);
		Block next = routeBlocks.get(1);
		Block prev;
		
		if(curr.getUp().equals(next))
		{
			routeDirection = Point.Orientation.UP;
			prev = curr.getDown();
		}
		else
		{
			routeDirection = Point.Orientation.DOWN;
			prev = curr.getUp();
		}
	
		if(prev != null)
		{
			if(prev instanceof Section)
			{
				if(routeDirection == Point.Orientation.UP)
				{
					sigSet.add(prev + ".UP, ");
				}
				else
				{
					sigSet.add(prev + ".DOWN, ");
				}
			}
			else //prev is a Point
			{
				Point p = (Point) prev;

				if(p.getSideline().equals(next))
				{
					pointSet.add(p + "+");
				}
				else
				{
					if(p.getOrientation() == routeDirection)
					{
						//Can be directed around
						pointSet.add(p + "-");//TODO Check logic
					}
				}
			}
		}
		
		for(int i = 1; i < routeBlocks.size() - 1; i++)
		{
			prev = routeBlocks.get(i - 1);
			curr = routeBlocks.get(i);
			next = routeBlocks.get(i + 1);
			
			if(curr instanceof Section)
			{
				if(routeDirection == Point.Orientation.UP)
				{
					sigSet.add(curr + ".DOWN, ");
				}
				else
				{
					sigSet.add(curr+ ".UP");
				}
			}
			else
			{
				Point p = (Point) curr;
				if(p.getSideline().equals(next))
				{
					pointSet.add(p + "-, ");
					if(routeDirection == Point.Orientation.UP)
					{
						sigSet.add(p.getUp() + ".DOWN, ");
					}
					else
					{
						sigSet.add(p.getDown() + ".UP, ");
					}
				}
				else if(p.getSideline().equals(prev))
				{
					pointSet.add(p + "-, ");
					if(routeDirection == Point.Orientation.UP)
					{
						sigSet.add(p.getDown() + ".UP ,");
					}
					else
					{
						sigSet.add(p.getUp() + ".DOWN, ");
					}
				}
				else
				{
					pointSet.add(p + "+, ");
					
					if(p.getOrientation() == Point.Orientation.UP)
					{
						sigSet.add(p.getSideline() + ".DOWN, ");
					}
					else
					{
						sigSet.add(p.getSideline() + ".UP, ");
					}
					
				}
			}
		}
		
		if(routeDirection == Point.Orientation.UP)
		{
			prev = curr;
			curr = next;
			next = curr.getUp();
			
			if(next != null)
			{
				if(next instanceof Section)
				{
					sigSet.add(next + ".DOWN");
				}
				else
				{
					Point p = (Point) next;
					
					if(p.getOrientation() == routeDirection)
					{
						//Cannot be diverted
						sigSet.add(p.getSideline() + ".DOWN, ");
						sigSet.add(p.getUp() + ".DOWN");
					}
					else
					{
						if(p.getSideline().equals(curr))
						{
							pointSet.add(p + "+");
						}
						else
						{
							pointSet.add(p + "-");
						}
					}
				}
			}	
		}
		else
		{
			prev = curr;
			curr = next;
			next = curr.getDown();
			
			if(next != null)
			{
				if(next instanceof Section)
				{
					sigSet.add(next + ".UP");
				}
				else
				{
					Point p = (Point) next;
					
					if(p.getOrientation() == routeDirection)
					{
						//Cannot be diverted
						sigSet.add(p.getSideline() + ".UP, ");
						sigSet.add(p.getDown() + "UP");
					}
					else
					{
						if(p.getSideline().equals(curr))
						{
							pointSet.add(p + "+");
						}
						else
						{
							pointSet.add(p + "-");
						}
					}
				}
			}
		}
		
		String[] res = new String[3];
		res[0] = r.getId();
		
		String s = "";
		for(int i = 0; i < sigSet.size(); i++)
		{
			s = s + sigSet.get(i);
		}
		res[1] = s;
		
		s = "";
		for(int i = 0; i < pointSet.size(); i++)
		{
			s = s + pointSet.get(i);
		}
		res[2] = s;
		
		return res;
	}
	
	public static String[] calculateConflicts(List<Route> routes)
	{
		return null;
	}
	
}
