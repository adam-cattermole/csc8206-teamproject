package ui;

import backend.NetworkDeserializationException;
import backend.NetworkSerializationException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import org.controlsfx.control.StatusBar;

import ui.utilities.CtrlKeyListener;
import ui.utilities.GridRectangles;
import ui.utilities.UiBlock;
import ui.utilities.UiJourney;
import ui.utilities.UiNetwork;
import ui.utilities.UiRoute;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Controller implements Initializable {
    @FXML private Group grid;
    @FXML private GridPane palette;
    @FXML private List<UiBlock> blockList; //list of blocks in the Pallet
    @FXML private ScrollPane scrollPane;
    @FXML private StatusBar statusBar;
    @FXML private TableView<UiRoute> interlockTable;
    @FXML private TableView<UiJourney> journeysTable;
    
    private GridRectangles gridRectangles;
    private UiNetwork uiNetwork;
    private JourneysController journeysController;
    
    private EventHandler<MouseEvent> focusEnableHandler;
    private EventHandler<KeyEvent> ctrlDownHandler;
    private EventHandler<KeyEvent> ctrlUpHandler;
    
    private boolean controlCanChangeFocus = true;
    private Control focusedControl;
    private Control enteredControl;

    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	gridRectangles = new GridRectangles(this);
    	uiNetwork = new UiNetwork(this);
    	journeysController = new JourneysController(this);
    	
    	grid.getChildren().add(gridRectangles);

        for (UiBlock b: blockList) {
            addPaletteListener(b);
        }
        
        focusEnableHandler = (e) -> {
        	if (controlCanChangeFocus) {
        		focusedControl = (Control) e.getSource();
        		focusedControl.requestFocus();
        	}
        	
        	enteredControl = (Control) e.getSource();
        };
        
        ctrlDownHandler = (e) -> {
        	if (e.isControlDown()) {
        		controlCanChangeFocus = false;
        		
            	Control control = (Control) e.getSource();
            	Object userData = control.getUserData();
            	
            	if (userData instanceof Supplier) {
            		CtrlKeyListener listener = ((Supplier<CtrlKeyListener>) userData).get();
            		if (listener != null) {
            			listener.onCtrlDown();
            		}
            	}
            	
            	focusedControl = control;
        	}
        };
        
        ctrlUpHandler = (e) -> {
        	if (!e.isControlDown()) {
        		controlCanChangeFocus = true;
            	Object userData = focusedControl.getUserData();
            	
            	if (userData instanceof Supplier) {
            		CtrlKeyListener listener = ((Supplier<CtrlKeyListener>) userData).get();
            		if (listener != null) {
            			listener.onCtrlUp();
            		}
            	}
            	
            	if (focusedControl != enteredControl && enteredControl != null) {
            		focusedControl = enteredControl;
            		focusedControl.requestFocus();
            	}
        	}
        };
        
        Supplier<CtrlKeyListener> uiNetworkSupplier = () -> {
        	return getUiNetwork();
        };
        
        Supplier<CtrlKeyListener> journeysControllerSupplier = () -> {
        	return getJourneysController();
        };
        
        scrollPane.setUserData(uiNetworkSupplier);
        interlockTable.setUserData(journeysControllerSupplier);
        
        Stream.of(scrollPane, interlockTable, journeysTable).forEach(control -> {
        	control.setOnMouseEntered(focusEnableHandler);
        	control.setOnKeyPressed(ctrlDownHandler);
        	control.setOnKeyReleased(ctrlUpHandler);
        });
    }
    
    /**
     * Method to access the controller components.
     * @return the ui grid component.
     */
    public Group getGrid() {
    	return grid;
    }
    
    public GridRectangles getGridRectangles() {
    	return gridRectangles;
    }
    
    public TableView<UiRoute> getInterlockTable() {
    	return interlockTable;
    }
    
    public TableView<UiJourney> getJourneysTable() {
    	return journeysTable;
    }
    
    public JourneysController getJourneysController() {
    	return journeysController;
    }
    
    /**
     * Method to access the controller components.
     * @return the uiNetwork component.
     */
    public UiNetwork getUiNetwork() {
    	return uiNetwork;
    }
    
    public void setStatusText(String text) {
    	statusBar.setText(text);
    }

    private void addPaletteListener(final Canvas source) {
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* allow any transfer mode */
                Dragboard db = source.startDragAndDrop(TransferMode.COPY);
                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                String type = source.getClass().getSimpleName();
                content.putString(type);
                db.setContent(content);

                event.consume();
            }
        });
    }
    
    public void showErrorDialog(String content, Exception e) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Exception Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText(content);

    	
    	// Create expandable Exception.
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	e.printStackTrace(pw);
    	String exceptionText = sw.toString();

    	Label label = new Label("The exception stacktrace was:");

    	TextArea textArea = new TextArea(exceptionText);
    	textArea.setEditable(false);
    	textArea.setWrapText(true);

    	textArea.setMaxWidth(Double.MAX_VALUE);
    	textArea.setMaxHeight(Double.MAX_VALUE);
    	GridPane.setVgrow(textArea, Priority.ALWAYS);
    	GridPane.setHgrow(textArea, Priority.ALWAYS);

    	GridPane expContent = new GridPane();
    	expContent.setMaxWidth(Double.MAX_VALUE);
    	expContent.add(label, 0, 0);
    	expContent.add(textArea, 0, 1);

    	// Set expandable Exception into the dialog pane.
    	alert.getDialogPane().setExpandableContent(expContent);
    	alert.getDialogPane().setExpanded(true);
    	alert.showAndWait();
    }

    @FXML private void onNewAction(ActionEvent event) {
        uiNetwork.clear();
    	journeysController.clear();
        uiNetwork = new UiNetwork(this);
    }

    @FXML private void onLoadAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Network File");
        File networkFile = fileChooser.showOpenDialog(grid.getScene().getWindow());

        if (networkFile != null) {
            try {
            	FileInputStream inputStream = new FileInputStream(networkFile);
            	
    	        // Must delete currently active network from the frontend grid
    	    	uiNetwork.clear();
    	    	
    	    	//also clear the journeys
    	    	journeysController.clear();
            	
    	    	// and override with new network
    			uiNetwork = UiNetwork.load(inputStream);
    			uiNetwork.setController(this);
    			
    			//redraw network elements
    			uiNetwork.refreshUi();
    		} catch (FileNotFoundException | NetworkDeserializationException e) {
    			showErrorDialog("Could not load network", e);
    		}
        }
    }

    @FXML private void onSaveAction(ActionEvent event) {
    	
    	//Check network validity
    	//If invalid, show warning dialog
    	//Otherwise, continue
    	
    	if(!uiNetwork.getValidity())
    	{
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Network Invalid");
        	alert.setHeaderText("The Network you are trying to save is invalid. Are you sure you wish to save?");
        	
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() != ButtonType.OK)
        	{
        		return;
        	}
    	}
    	
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Save File");
        File networkFile = fileChooser.showSaveDialog(grid.getScene().getWindow());
        
        if (networkFile != null) {
            try {
            	FileOutputStream outputStream = new FileOutputStream(networkFile);
    			uiNetwork.save(outputStream);
    		} catch (FileNotFoundException | NetworkSerializationException e) {
    			showErrorDialog("Could not save network", e);
    		}	
        }
    }
    
    @FXML private void onExitAction(ActionEvent event) {
        Platform.exit();
    }
}
