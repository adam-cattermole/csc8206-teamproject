package ui.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import route.JourneyBuilder.Journey;
import utilities.Change;
import utilities.ChangeListener;
import utilities.ChangeType;
import utilities.Observable;

public class UiJourney implements Observable<UiJourney> {
	private final StringProperty id;
	private final StringProperty source;
	private final StringProperty destination;
	private final StringProperty routes;
	private final Journey journey;
	private final List<ChangeListener<UiJourney>> listeners = new ArrayList<ChangeListener<UiJourney>>();
	
	public UiJourney(Journey journey) {
		this.journey = journey;
		id = new SimpleStringProperty(journey.getID());
		source = new SimpleStringProperty(journey.getStartSignal().toString());
		destination = new SimpleStringProperty(journey.getDestinationSignal().toString());
		routes = new SimpleStringProperty(journey.getRoutes().stream().map(r -> r.getId()).collect(Collectors.joining("; ")));
		
		this.journey.addChangeListener(r -> {
			if (r.wasRemoved()) {
				listeners.stream().forEach(listener -> listener.onChange(new Change<UiJourney>(this, ChangeType.REMOVED)));
			}
		});
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

	@Override
	public void addChangeListener(ChangeListener<UiJourney> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeChangeListener(ChangeListener<UiJourney> listener) {
		listeners.remove(listener);
	}	
}
