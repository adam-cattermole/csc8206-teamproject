package route;

import java.util.List;

import backend.Block;
import backend.SimpleNetwork;
import backend.BlockFactory;

public class RouteTester
{
	Route r;
	Route.RouteBuilder build = Route.getRouteBuilder();
	
	public static void main(String args[])
	{
		RouteTester test = new RouteTester();
		for(int i = 0; i < 5; i++)
		{
			test.build.addToRoute(BlockFactory.getBlock("Section"));
		}
		
		List<Block> testPath = test.build.getCurrentPath();
		System.out.println(testPath.toString());
		test.r = test.build.build();
		
		System.out.println(test.r);
		System.out.println(test.r.getRoute());
		System.out.println(test.r.getStart());
		System.out.println(test.r.getEnd());
			
	}
	
}
