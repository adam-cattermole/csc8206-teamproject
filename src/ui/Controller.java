package ui;

import backend.NetworkDeserializationException;
import backend.NetworkSerializationException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import ui.utilities.*;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.StatusBar;

public class Controller implements Initializable {
    @FXML private Group grid;
    @FXML private GridPane palette;
    @FXML private List<UiBlock> blockList; //list of blocks in the Pallet
    @FXML private ScrollPane scrollPane;
    @FXML private StatusBar statusBar;
    @FXML private TableView<UiRoute> interlockTable;
    
    private GridRectangles gridRectangles;
    private UiNetwork uiNetwork;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	gridRectangles = new GridRectangles(this);
    	uiNetwork = new UiNetwork(this);
    	
    	grid.getChildren().add(gridRectangles);

        for (UiBlock b: blockList) {
            addPaletteListener(b);
        }
        
        scrollPane.setMaxSize(750, 750);
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

    @FXML private void onLoadAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Network File");
        File networkFile = fileChooser.showOpenDialog(grid.getScene().getWindow());

        if (networkFile != null) {
            try {
            	FileInputStream inputStream = new FileInputStream(networkFile);
            	
    	        // Must delete currently active network from the frontend grid
    	    	uiNetwork.clear();
            	
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
