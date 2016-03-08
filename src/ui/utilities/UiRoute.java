package ui.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import backend.Point;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import route.RouteBuilder.Route;
import utilities.Change;
import utilities.ChangeListener;
import utilities.ChangeType;
import utilities.Observable;

public class UiRoute implements Observable<UiRoute> {
	private List<UiBlock> uiBlocks;
	
	private final StringProperty id;
	private final StringProperty source;
	private final StringProperty destination;
	private final StringProperty points;
	private final StringProperty signals;
	private final StringProperty path;
	private final StringProperty conflicts;
	private final Route route;
	
	private final List<ChangeListener<UiRoute>> listeners = new ArrayList<ChangeListener<UiRoute>>();
	
	public UiRoute(Route route) {
		this.route = route;
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
		
		conflicts = new SimpleStringProperty();
		
		route.addChangeListener(r -> {
			if (r.wasChanged()) {
				conflicts.setValue(r.getElement().getConflicts().stream().collect(Collectors.joining("; ")));
			} else if (r.wasRemoved()) {
				listeners.stream().forEach(listener -> listener.onChange(new Change<UiRoute>(this, ChangeType.REMOVED)));
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
	
	public Route getRoute() {
		return route;
	}
	
	public void setHighlight(boolean highlight) {
		if (uiBlocks != null) {
			uiBlocks.stream().forEach(b -> b.setHighlight(highlight));
		}
	}
	
	public void setUiBlocks(List<UiBlock> blocks) {
		uiBlocks = blocks;
	}
	
	public List<UiBlock> getUiBlocks() {
		return uiBlocks;
	}

	@Override
	public void addChangeListener(ChangeListener<UiRoute> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeChangeListener(ChangeListener<UiRoute> listener) {
		listeners.remove(listener);
	}
}
