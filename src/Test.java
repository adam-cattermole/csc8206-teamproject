
public class Test
{
	// Class for testing random shit to see if it works
	// Eventually used for actual testing
	public static void main(String[] args)
	{
		/*Point p = new Point(null,null,null, null);
		Section s = new Section(null,null);
		
		Block bP = new Point(null,null,null, null);
		Block bS = new Section(null,null);
		
		Section castS = (Section) bS;
		
		castS.setUp(p);
		
		System.out.println(bS.getUp());*/
		
		Section b1 = new Section();
		Section b2 = new Section();
		Point p1 = new Point(Point.Orientation.UP);
		Section b3 = new Section();
		Section b4 = new Section();
		Point p2 = new Point(Point.Orientation.DOWN);
		Section b5 = new Section();
		Section b6 = new Section();
		
		b1.setUp(b2);
		b2.setUp(p1);
		
		p1.setUp(b4);
		p1.setSideline(b3);
		
		p2.setSideline(b3);
		p2.setUp(b5);
		p2.setDown(b4);
		
		b5.setUp(b6);
		
		SimpleNetwork network = new SimpleNetwork();
		
		network.addBlock(b1).addBlock(b2).addBlock(p1).addBlock(b3).addBlock(b4);
		network.addBlock(p2).addBlock(b5).addBlock(b6);
		
		System.out.println("network valid: " + network.isValid());
		
		network.removeBlock(p2);
		
		System.out.println("network valid: " + network.isValid());
				
	}
}