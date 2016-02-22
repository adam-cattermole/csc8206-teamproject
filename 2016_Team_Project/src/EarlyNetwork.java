import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author chris_curry
 * 
 *         Early version of a network class Far from perfect, but hopefully
 *         enough to build on
 *
 */
public class EarlyNetwork implements Network
{

	private List<Block> blocks;

	public EarlyNetwork()
	{
		blocks = new ArrayList<Block>();
		blocks.add(new Section(null, null));
	}

	@Override
	public void removeBlock(Block toRemove)
	{
		Block b;
		for (int i = 0; i < blocks.size(); i++)
		{
			b = blocks.get(i);
			if (b.equals(toRemove))
			{
				if (b instanceof Section)
				{
					if (b.getUp() != null)
					{
						b.getUp().setDown(null);
					}

					if (b.getDown() != null)
					{
						b.getDown().setUp(null);
					}

					blocks.remove(i);
				}

				if (b instanceof Point)
				{
					Point p = (Point) b;
					
					if (p.getUp() != null)
					{
						p.getUp().setDown(null);
					}

					if (p.getDown() != null)
					{
						p.getDown().setUp(null);
					}

					if (p.getSideline() != null)
					{
						if (p.getSideline() instanceof Section)
						{
							Section s = (Section) p.getSideline();
							if(p.getOrientation() == Point.PointOrientation.UP)
							{
								s.setDown(null);
							}
							else if(p.getOrientation() == Point.PointOrientation.DOWN)
							{
								s.setUp(null);
							}
						}

						if(p.getSideline() instanceof Point)
						{
							Point sideP = (Point) p.getSideline();
							sideP.setSideline(null, sideP.getOrientation());
						}
						
					}

					blocks.remove(i);
				}
			}

		}
	}

	@Override
	public boolean validNetwork()
	{
		// Don't know rules, so...
		return true;
	}

	public List<Block> getNetworkList()
	{
		return blocks;
	}

	@Override
	public void addSection(Block addToBlock, BlockLink addToLink)
	{
		if(addToBlock instanceof Section && addToLink == BlockLink.SIDE)
		{
			throw new IllegalArgumentException("Section blocks do not have a SIDE link");
		}
		
		switch (addToLink)
		{
			case UP:
				if (addToBlock.getUp() != null)
				{
					addToBlock.setUp(new Section(null, addToBlock));
				} else
				{
					throw new IllegalStateException("There is already track attatched to the UP link of this block");
				}
				break;
			case DOWN:
				if (addToBlock.getDown() != null)
				{
					addToBlock.setDown(new Section(addToBlock, null));
				} else
				{
					throw new IllegalStateException("There is already track attatched to the DOWN link of this block");
				}
				break;
			case SIDE:
				Point p = (Point) addToBlock;
				
				if(p.getOrientation() == null)
				{
					throw new IllegalStateException("Points must have an orientation before their SIDE link may be used");
				}
				
				if(p.getOrientation() == Point.PointOrientation.UP)
				{
					p.setSideline(new Section(null,p), p.getOrientation());
				}
				
				
				break;
			default:
				throw new IllegalArgumentException("You must specify which link the new Section is to be added to");
		}

	}

	@Override
	public void addPoint(Block addToBlock, BlockLink addToLink, Point.PointOrientation orientation)
	{

	}

}
