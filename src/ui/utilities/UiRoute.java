package ui.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import route.RouteBuilder.Route;

public class UiRoute {
	private final StringProperty routeID;
	private final StringProperty source;
	private final StringProperty destination;
	private final StringProperty points;
	private final StringProperty signals;
	private final StringProperty path;
	private final StringProperty conflicts;
	
	
	public UiRoute(Route route) {
		routeID = new SimpleStringProperty(route.getId());
		
		source = new SimpleStringProperty(route.getStart().toString());
		destination = new SimpleStringProperty(route.getEnd().toString());
		
		points = new SimpleStringProperty(route.toString());
		
		
		signals = new SimpleStringProperty(route.getId());
		path = new SimpleStringProperty(route.getId());
		conflicts = new SimpleStringProperty(route.getId());
		
	}
	
	public void setConflicts(String conflicts) {
		
	}
	
	public String getRouteID(){
		return routeID.get();
	}

}
