import com.fasterxml.jackson.core.JsonGenerationException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Test
{
	// Class for testing random shit to see if it works
	// Eventually used for actual testing
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		/*Network network = new SimpleNetwork();
		
		Section b1 = network.makeSection();
		Section b2 = network.makeSection();
		Point p1 = network.makePoint(Point.Orientation.UP);
		Section b3 = network.makeSection();
		Section b4 = network.makeSection();
		Point p2 = network.makePoint(Point.Orientation.DOWN);
		Section b5 = network.makeSection();
		Section b6 = network.makeSection();
		
		b1.setUp(b2);
		b2.setUp(p1);
		
		p1.setUp(b4);
		p1.setSideline(b3);
		
		p2.setSideline(b3);
		p2.setUp(b5);
		p2.setDown(b4);
		
		b5.setUp(b6);*/
		
		//b3.setSignalDown(new Signal());
		//b3.setSignalUp(new Signal());
		
		try {
			FileInputStream in = new FileInputStream("network.txt");
	        //FileOutputStream out = new FileOutputStream("network.txt");
			//network.save(out);
			
			Network testNetwork = Network.load(in);

			
			System.out.println(testNetwork);
			
			//System.out.println(network);
			
			//out.flush();
			//out.close();
			
			in.close();
			
		//} catch (NetworkSerializationException e) {
		//	e.getReason().printStackTrace();
		//}
		} catch (NetworkDeserializationException e) {
			e.getReason().printStackTrace();
		}
		
		/*System.out.println("network valid: " + network.isValid());
		
		network.removeBlock(p2);
		
		System.out.println("network valid: " + network.isValid());*/
	}
}