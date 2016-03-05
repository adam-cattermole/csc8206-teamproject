package route;

import backend.SimpleNetwork;
import backend.Network;
import backend.Point;
import backend.Section;
import route.RouteBuilder;
import route.RouteBuilder.Route;

public class RouteTester
{
	Route r;
	RouteBuilder build = new RouteBuilder();

	public static void main(String args[])
	{
		RouteTester test = new RouteTester();

		Network network = new SimpleNetwork();

		Section b1 = network.makeSection();
		Section b2 = network.makeSection();
		Point p1 = network.makePoint(Point.Orientation.UP);
		Section b3 = network.makeSection();
		Section b4 = network.makeSection();
		Point p2 = network.makePoint(Point.Orientation.DOWN);
		Section b5 = network.makeSection();
		Section b6 = network.makeSection();

		// left to right set up
		b1.setUp(b2);
		b2.setUp(p1);

		p1.setUp(b4);
		p1.setSideline(b3);

		p2.setSideline(b3);
		p2.setUp(b5);
		p2.setDown(b4);

		b5.setUp(b6);

		// TEST CASES DEFINED HERE
		// CASE 1
		test.build.addToRoute(b1);
		test.build.addToRoute(b2);
		test.build.addToRoute(p1);
		test.build.addToRoute(b3);

		String s[];

		test.r = test.build.build();

		s = (InterlockCalculator.calculateSettings(test.r));
		System.out.println(s[0] + "||" + s[1] + "||" + s[2]);

		// CASE 2
		test.build.addToRoute(b1);
		test.build.addToRoute(b2);
		test.build.addToRoute(p1);
		test.build.addToRoute(b4);

		test.r = test.build.build();

		s = (InterlockCalculator.calculateSettings(test.r));
		System.out.println(s[0] + "||" + s[1] + "||" + s[2]);

		// CASE 3
		test.build.addToRoute(b2);
		test.build.addToRoute(p1);
		test.build.addToRoute(b4);
		test.build.addToRoute(p2);
		test.build.addToRoute(b5);

		test.r = test.build.build();

		s = (InterlockCalculator.calculateSettings(test.r));
		System.out.println(s[0] + "||" + s[1] + "||" + s[2]);

		// CASE 3
		test.build.addToRoute(b1);
		test.build.addToRoute(b2);
		test.build.addToRoute(p1);
		test.build.addToRoute(b3);
		test.build.addToRoute(p2);
		test.build.addToRoute(b5);
		test.build.addToRoute(b6);

		test.r = test.build.build();

		s = (InterlockCalculator.calculateSettings(test.r));
		System.out.println(s[0] + "||" + s[1] + "||" + s[2]);

	}

}
