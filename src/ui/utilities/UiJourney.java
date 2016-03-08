package ui.utilities;

import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import route.JourneyBuilder.Journey;

public class UiJourney {
	private final StringProperty id;
	private final StringProperty source;
	private final StringProperty destination;
	private final StringProperty routes;
	
	public UiJourney(Journey journey) {
		id = new SimpleStringProperty(journey.getID());
		source = new SimpleStringProperty(journey.getStartSignal().toString());
		destination = new SimpleStringProperty(journey.getDestinationSignal().toString());
		routes = new SimpleStringProperty(journey.getRoutes().stream().map(r -> r.getId()).collect(Collectors.joining("; ")));
	}
	
	public String getId() {
		return id.get();
	}
	
	public String getSource() {
		return source.get();
	}
	
	public String getDestination() {
		return destination.get();
	}
	
	public String getRoutes() {
		return routes.get();
	}	
}
