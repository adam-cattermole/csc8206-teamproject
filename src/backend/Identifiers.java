package backend;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class which stores and manages identifiers
 * Identifier is incremented every time a new id is requested.
 * If an element is deleted, it's ID will end up in a pool of IDs. When next ID is requested, firstly the IDs from the pool are retrieved.
 */
public class Identifiers 
{
	private static int sectionID = 0;
	private static SortedSet<Integer> sectionIDPool = new TreeSet<Integer>();
	
	private static int pointID = 0;
	private static SortedSet<Integer> pointIDPool = new TreeSet<Integer>();
	
	private static int signalID = 0;
	private static SortedSet<Integer> signalIDPool = new TreeSet<Integer>();
	
	public static int getSectionID()
	{
		if (!sectionIDPool.isEmpty())
		{
			int id = sectionIDPool.first();
			sectionIDPool.remove(id);
			return id;
		}
		
		sectionID++;
		return sectionID;
	}
	
	public static int getPointID()
	{
		if (!pointIDPool.isEmpty())
		{
			int id = pointIDPool.first();
			pointIDPool.remove(id);
			return id;
		}
		
		pointID++;
		return pointID;
	}
	
	public static int getSignalID()
	{
		if (!signalIDPool.isEmpty())
		{
			int id = signalIDPool.first();
			signalIDPool.remove(id);
			return id;
		}
		
		signalID++;
		return signalID;
	}
	
	public static void addToBlockPool(Block block)
	{
		int id = block.getID();
		
		if (block instanceof Section)
		{
			sectionIDPool.add(id);
			Section s = (Section) block;
			if (s.hasSignalUp())
			{
				signalIDPool.add(s.getSignalUp().getID());
			}
			if (s.hasSignalDown())
			{
				signalIDPool.add(s.getSignalDown().getID());
			}
		} else if (block instanceof Point) {
			pointIDPool.add(id);
		}
	}
	
	public static void setMaxBlockID(Block block)
	{
		int id = block.getID();
		
		if (block instanceof Section)
		{
			if (id > sectionID)
			{
				sectionID = id;
                                System.out.println("Set max section ID:" + id);
			}
		} else if (block instanceof Point) {
			if (id > pointID)
			{
				pointID = id;
                                System.out.println("Set max block ID:" + id);
			}
		}
	}
	
	public static void setMaxSignalID(int id)
	{
		if (id > signalID)
		{
			signalID = id;
		}
	}
}
