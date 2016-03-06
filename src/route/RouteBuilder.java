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
		private final List<Block> sequence;
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
			this.sequence = path;//TODO Defensive copying; make programming easier too
			this.path = path.subList(1, path.size());
			
			startBlock = (Section) start;
			endBlock = (Section) end;
			
			//detect the direction based on next block

			if (startBlock.getUp() != null && startBlock.getUp().equals(path.get(1))) {
				//we are going in up direction
				source = startBlock.getSignalUp();
				destination = endBlock.getSignalUp();
			} else {
				//we are going in down direction
				source = startBlock.getSignalDown();
				destination = endBlock.getSignalDown();
			}
			
			calculateSettings();
		}

		public String getId() {
			return id;
		}
		
		public List<Block> getSequence()
		{
			return sequence;
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
			List<String> sigSet = signals;
			List<String> pointSet = points;
			
			if(sigSet.size() != 0 || pointSet.size() != 0)
			{
				//Already calculated
				return;
			}
			
			List<Block> routeBlocks = sequence;		
			
			Point.Orientation routeDirection;
			Block curr = routeBlocks.get(0);
			Block next = routeBlocks.get(1);
			Block prev;

			
			if(curr.getUp() != null && curr.getUp().equals(next))
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
						//sigSet.add(prev + ".UP, ");
						sigSet.add(((Section) prev).getSignalUp().toString());
					}
					else
					{
						//sigSet.add(prev + ".DOWN, ");
						sigSet.add(((Section) prev).getSignalDown().toString());
					}
				}
				else //prev is a Point
				{
					Point p = (Point) prev;

					if(p.getSideline().equals(curr))
					{
						pointSet.add("p" + p.getID() + ":p");
					}
					else
					{
						if(p.getOrientation() == routeDirection)
						{
							//Can be directed around
							pointSet.add("p" + p.getID() + ":m");
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
						//sigSet.add(curr + ".DOWN, ");
						sigSet.add(((Section) curr).getSignalDown().toString());
					}
					else
					{
						//sigSet.add(curr+ ".UP");
						sigSet.add(((Section) curr).getSignalUp().toString());
					}
				}
				else
				{
					Point p = (Point) curr;
					if(p.getSideline().equals(next))
					{
						pointSet.add("p" + p.getID() + ":m");
						if(routeDirection == Point.Orientation.UP)
						{
							//sigSet.add(p.getUp() + ".DOWN, ");
							sigSet.add(((Section) p.getUp()).getSignalDown().toString());
						}
						else
						{
							//sigSet.add(p.getDown() + ".UP, ");
							sigSet.add(((Section) p.getDown()).getSignalUp().toString());
						}
					}
					else if(p.getSideline().equals(prev))
					{
						pointSet.add("p" + p.getID() + ":m");
						if(routeDirection == Point.Orientation.UP)
						{
							//sigSet.add(p.getDown() + ".UP ,");
							sigSet.add(((Section) p.getDown()).getSignalUp().toString());
						}
						else
						{
							//sigSet.add(p.getUp() + ".DOWN, ");
							sigSet.add(((Section) p.getUp()).getSignalDown().toString());
						}
					}
					else
					{
						pointSet.add("p" + p.getID() + ":p");
						
						if(p.getOrientation() == Point.Orientation.UP)
						{
							//sigSet.add(p.getSideline() + ".DOWN, ");
							sigSet.add(((Section) p.getSideline()).getSignalDown().toString());
						}
						else
						{
							//sigSet.add(p.getSideline() + ".UP, ");
							sigSet.add(((Section) p.getSideline()).getSignalUp().toString());
						}
						
					}
				}
			}
			
			
			
			if(routeDirection == Point.Orientation.UP)
			{
				prev = curr;
				curr = next;
				next = curr.getUp();
				
				sigSet.add(((Section) curr).getSignalDown().toString());
				
				if(next != null)
				{
					if(next instanceof Section)
					{
						//sigSet.add(next + ".DOWN");
						sigSet.add(((Section) next).getSignalDown().toString());
					}
					else
					{
						Point p = (Point) next;
						
						if(p.getOrientation() == routeDirection)
						{
							//Cannot be diverted
							//sigSet.add(p.getSideline() + ".DOWN, ");
							//sigSet.add(p.getUp() + ".DOWN");
							sigSet.add(((Section) p.getSideline()).getSignalDown().toString());
							sigSet.add(((Section) p.getUp()).getSignalDown().toString());
						}
						else
						{
							if(p.getSideline().equals(curr))
							{
								pointSet.add("p" + p.getID() + ":p");
							}
							else
							{
								pointSet.add("p" + p.getID() + ":m");
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
				
				sigSet.add(((Section) curr).getSignalUp().toString());
				
				if(next != null)
				{
					if(next instanceof Section)
					{
						//sigSet.add(next + ".UP");
						sigSet.add(((Section) next).getSignalUp().toString());
					}
					else
					{
						Point p = (Point) next;
						
						if(p.getOrientation() == routeDirection)
						{
							//Cannot be diverted
							//sigSet.add(p.getSideline() + ".UP, ");
							//sigSet.add(p.getDown() + "UP");
							sigSet.add(((Section) p.getSideline()).getSignalUp().toString());
							sigSet.add(((Section) p.getDown()).getSignalUp().toString());
						}
						else
						{
							if(p.getSideline().equals(curr))
							{
								pointSet.add("p" + p.getID() + ":p");
							}
							else
							{
								pointSet.add("p" + p.getID() + ":m");
							}
						}
					}
				}
			}
		}

		public boolean validateRoute()
		{
			//validate route based on conflicts
			return true;
		}
	}
}