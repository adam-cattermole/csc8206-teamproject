package backend;

public class Identifiers 
{
	private static int sectionID = 0;
	private static int pointID = 0;
	private static int signalID = 0;
	
	public static int getSectionID()
	{
		sectionID++;
		return sectionID;
	}
	
	public static int getPointID()
	{
		pointID++;
		return pointID;
	}
	
	public static int getSignalID()
	{
		signalID++;
		return signalID;
	}
}
