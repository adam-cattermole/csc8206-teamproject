package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import route.JourneyBuilder;
import route.JourneyBuilder.Journey;
import ui.utilities.CtrlKeyListener;
import ui.utilities.UiJourney;
import ui.utilities.UiRoute;

public class JourneysController implements CtrlKeyListener {
	private Controller controller;
	private TableView<UiJourney> journeysTable;
	private TableView<UiRoute> routesTable;
	private JourneyBuilder journeyBuilder;
	private final ObservableList<UiJourney> journeys;
	private final TableViewSelectionModel<UiRoute> routeSelectionModel;
	private final TableViewSelectionModel<UiJourney> journeySelectionModel;
	
	private List<UiRoute> journeyRoutes;
	
	public JourneysController(Controller controller) {
		this.controller = controller;
		journeysTable = this.controller.getJourneysTable();
		routesTable = this.controller.getInterlockTable();
		journeys = journeysTable.getItems();
		
		routeSelectionModel = routesTable.getSelectionModel();
		journeySelectionModel = journeysTable.getSelectionModel();
		
        routeSelectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {        	
        	if (newSelection != null && oldSelection != newSelection) {
        		if (isBuildingJourney()) {
        			journeyBuilder.addToJourney(newSelection.getRoute());
        			journeyRoutes.add(newSelection);
        			
                	routeSelectionModel.getSelectedItems().stream().forEach(uiRoute -> {
                		uiRoute.setHighlight(true);
                	});
        		} else {
            		if (oldSelection != null) {
            			oldSelection.setHighlight(false);
            		}
            		newSelection.setHighlight(true);
        		}
        	}
        });
        
        journeySelectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        	if (newSelection != null && oldSelection != newSelection) {
        		if (oldSelection != null) {
        			oldSelection.setHighlight(false);
        		}
        		
        		newSelection.setHighlight(true);
        	}
        });
        
        
        routesTable.focusedProperty().addListener(change -> {
        	if (!routesTable.isFocused()) {
            	routeSelectionModel.getSelectedItems().stream().forEach(uiRoute -> {
            		if (uiRoute != null) {
            			uiRoute.setHighlight(false);
            		}
            	});	
        	}
        	
			try {
				routeSelectionModel.clearSelection();
			} catch (Exception e) {}
        });
        
        journeysTable.focusedProperty().addListener(change -> {
        	if (!journeysTable.isFocused()) {
            	journeySelectionModel.getSelectedItems().stream().forEach(uiJourney -> {
            		uiJourney.setHighlight(false);
            	});	
        	}
        	
			try {
				journeySelectionModel.clearSelection();
			} catch (Exception e) {}
        });
        
        routesTable.setEditable(false);
        routesTable.getColumns().stream().forEach(col -> col.setSortable(false));
        journeysTable.setEditable(false);
        journeysTable.getColumns().stream().forEach(col -> col.setSortable(false));
        routeSelectionModel.setSelectionMode(SelectionMode.SINGLE);
	}
	
	public void clear() {
		journeysTable.getItems().clear();
	}
	
	public boolean isBuildingJourney() {
		return journeyBuilder != null;
	}

	@Override
	public void onCtrlDown() {
		//start building journey
		if (!isBuildingJourney()) {
			journeyBuilder = new JourneyBuilder();
			journeyRoutes = new ArrayList<UiRoute>();
			
			try {
            	routeSelectionModel.getSelectedItems().stream().forEach(uiRoute -> {
            		uiRoute.setHighlight(false);
            	});
				routeSelectionModel.clearSelection();
			} catch (Exception e) {}
			routeSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
		}
	}

	@Override
	public void onCtrlUp() {
		//end building journey
		if (isBuildingJourney()) {
			try {
				Journey journey = journeyBuilder.build();
				UiJourney uiJourney = new UiJourney(journey);
				
				uiJourney.setUiRoutes(journeyRoutes);
				
				uiJourney.addChangeListener(change -> {
					if (change.wasRemoved()) {
						//remove journey
						journeys.remove(change.getElement());
					}
				});
				
            	routeSelectionModel.getSelectedItems().stream().forEach(uiRoute -> {
            		uiRoute.setHighlight(false);
            	});
				journeys.add(uiJourney);
			} catch (IllegalArgumentException e) {}
		}
		
		journeyBuilder = null;
		journeyRoutes = null;
		
		try {
			routeSelectionModel.clearSelection();
		} catch (Exception e) {}
		//routesTable.setFocusModel(null);
		routeSelectionModel.setSelectionMode(SelectionMode.SINGLE);
	}
}
