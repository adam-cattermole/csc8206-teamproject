package ui.utilities;

import java.util.stream.Collectors;

import backend.Point;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import route.RouteBuilder.Route;

public class UiRoute {
	private final StringProperty id;
	private final StringProperty source;
	private final StringProperty destination;
	private final StringProperty points;
	private final StringProperty signals;
	private final StringProperty path;
	private final StringProperty conflicts;
	
	public UiRoute(Route route) {
		id = new SimpleStringProperty(route.getId());
		
		source = new SimpleStringProperty(route.getSource().toString());
		destination = new SimpleStringProperty(route.getDestination().toString());
			
		points = new SimpleStringProperty(route.getPoints().stream().collect(Collectors.joining("; ")));
		signals = new SimpleStringProperty(route.getSignals().stream().collect(Collectors.joining("; ")));
		
		path = new SimpleStringProperty(
				route.getPath()
					.stream()
					.map(b -> ((b instanceof Point) ? ("p" + b.getID()) : ("b" + b.getID())))
					.collect(Collectors.joining("; "))
		);
		
		conflicts = new SimpleStringProperty(route.getConflicts().stream().collect(Collectors.joining("; ")));
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
	
	public String getPoints() {
		return points.get();
	}
	
	public String getSignals() {
		return signals.get();
	}
	
	public String getPath() {
		return path.get();
	}
	
	public String getConflicts() {
		return conflicts.get();
	}
}
