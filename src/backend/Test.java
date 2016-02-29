package backend;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerationException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test
{
	// Class for testing random shit to see if it works
	// Eventually used for actual testing
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		Network network = new SimpleNetwork();
		
		Section b1 = network.makeSection();
		Section b2 = network.makeSection();
		Point p1 = network.makePoint(Point.Orientation.UP);
		Section b3 = network.makeSection();
		Section b4 = network.makeSection();
		Point p2 = network.makePoint(Point.Orientation.DOWN);
		Section b5 = network.makeSection();
		Section b6 = network.makeSection();
		
		//left to right set up
		b1.setUp(b2);
		b2.setUp(p1);
		
		p1.setUp(b4);
		p1.setSideline(b3);
		
		p2.setSideline(b3);
		p2.setUp(b5);
		p2.setDown(b4);
		
		b5.setUp(b6);
		
		//right to left set up
		/*b6.setDown(b5);
		
		b5.setDown(p2);
		p2.setSideline(b3);
		p2.setDown(b4);
		
		b4.setDown(p1);
		p1.setSideline(b3);
		p1.setDown(b2);
		
		b2.setDown(b1);*/
		
		System.out.println(network);
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		jsonMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, network);

		/*network.removeBlock(b2);
		
		Section s = network.makeSection();
		b1.setUp(s);
		s.setUp(p1);
		
		System.out.println(network);*/
		
		
		//b3.setSignalDown(new Signal());
		//b3.setSignalUp(new Signal());
		
		//try {
			//FileInputStream in = new FileInputStream("network.json");
	        //FileOutputStream out = new FileOutputStream("network.json");
			//network.save(System.out);
			
			/*Network testNetwork = Network.load(System.in);
			
			System.out.println(testNetwork);
			
			Set<Block> blocks = testNetwork.getBlocks();
			Iterator<Block> iterator = blocks.iterator();
			iterator.next();
			testNetwork.removeBlock(iterator.next());
			
			Section s = testNetwork.makeSection();
			
			System.out.println(testNetwork);*/

			//System.out.println(network);
			
			//out.flush();
			//out.close();
			
			//in.close();
			
		/*} catch (NetworkSerializationException e) {
			e.getReason().printStackTrace();
		}*/
		/*} catch (NetworkDeserializationException e) {
			e.getReason().printStackTrace();
		}*/
		
		/*System.out.println("network valid: " + network.isValid());
		
		network.removeBlock(p2);
		
		System.out.println("network valid: " + network.isValid());*/
	}
}