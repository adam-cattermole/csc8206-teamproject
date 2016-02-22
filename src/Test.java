
public class Test
{
	// Class for testing random shit to see if it works
	// Eventually used for actual testing
	public static void main(String[] args)
	{
		Point p = new Point(null,null,null, null);
		Section s = new Section(null,null);
		
		Block bP = new Point(null,null,null, null);
		Block bS = new Section(null,null);
		
		Section castS = (Section) bS;
		
		castS.setUp(p);
		
		System.out.println(bS.getUp());
		
		
	}
}
