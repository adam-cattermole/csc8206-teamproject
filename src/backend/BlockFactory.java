package backend;
import java.util.HashMap;

public class BlockFactory
{
	static HashMap<Integer, Point> points = new HashMap<Integer, Point>();
	static HashMap<Integer, Section> sections = new HashMap<Integer, Section>();
	
	public static void reset()
	{
		points.clear();
		sections.clear();
	}
	
	public static Block getBlock(String blockType)
	{
		if (blockType.equals("Section"))
		{
			Section s = new Section();
			sections.put(s.getID(), s);
			return s;
		}
		
		if (blockType.equals("Point"))
		{
			Point p = new Point();
			points.put(p.getID(), p);
			return p;
		}
		
		return null;
	}
	
	public static Block getBlock(String blockType, int id)
	{
		Block b = null;
		
		if (blockType.equals("Section"))
		{
			b = sections.get(id);
			if (b == null)
			{
				b = new Section(id);
				sections.put(id, (Section)b);
			}
		} else if (blockType.equals("Point")) {
			b = points.get(id);
			if (b == null)
			{
				b = new Point(id);
				points.put(id, (Point)b);
			}
		}
		
		return b;
	}
}
