package ui.utilities;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import ui.Controller;

/**
 * Specifies the grid configuration and holds all the members of the grid
 * @author Jakub Gawron
 */
public class GridRectangles extends Group {
    public static final int GRID_HEIGHT = 50;
    public static final int GRID_WIDTH = 50;
    public static final int CELL_SIZE = 30;
    
	private GridRectangle[][] rectangles;
	private Controller controller;
	
	private int lastRectangleRow = 0;
	private int lastRectangleCol = 0;
	
        /**
         * Constructor of the GridRectangles class. It creates rectangles which will be part of the grid and assigns event listeners
         * @param controller Reference to the controller object
         */
	public GridRectangles(Controller controller) {
		this.controller = controller;
		this.rectangles = new GridRectangle[GRID_WIDTH][GRID_HEIGHT];
	
		ObservableList<Node> children = getChildren();
		
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                GridRectangle r1 = new GridRectangle(i*CELL_SIZE, j*CELL_SIZE, CELL_SIZE, CELL_SIZE, this);
                rectangles[i][j] = r1;
                children.add(r1);
            }
        }
        
        setupListeners();
	}
	
        /**
         * Used to attach GridRectangle to a event listener which supports drag & drop functionality
         */
	private void setupListeners() {		
        setOnDragExited(event -> {
        	//remove highlight from last rectangle
        	rectangles[lastRectangleRow][lastRectangleCol].highlight(event.getDragboard().getString(), Color.LIGHTGRAY);
        	
        	event.consume();
        });
        
        setOnDragOver(event -> {
        	Dragboard db = event.getDragboard();
        	
            if (event.getGestureSource() != this && db.hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                
                //highlight rectangle we are over
                int rectangleRow = xToRow(event.getX());
                int rectangleCol = yToCol(event.getY());
                
                if (!(rectangleRow == lastRectangleRow && rectangleCol == lastRectangleCol)) {
                	//remove highlight from last rectangle
                	rectangles[lastRectangleRow][lastRectangleCol].highlight(db.getString(), Color.LIGHTGRAY);
                	
                	//highlight current rectangle
                	rectangles[rectangleRow][rectangleCol].highlight(db.getString(), Color.GREEN);
                	
                	lastRectangleRow = rectangleRow;
                	lastRectangleCol = rectangleCol;
                }
            }

            event.consume();
        });
        
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            
            if (db.hasString()) {
            	TransferMode mode = event.getAcceptedTransferMode();
            	
            	if (mode == TransferMode.COPY) {
                	int row = xToRow(event.getX());
                	int col = yToCol(event.getY());
                	GridRectangle rectangle = rectangles[row][col];
                	
                    String type = db.getString();
                    UiBlock UiBlock;
                    
                    if (!rectangle.isPlacementValid(type))
                    {
                        System.out.println("Invalid placement!");
                        success = false;
                    }
                    else
                    {
                    	int x = rowToX(row);
                    	int y = colToY(col);
                    	
                        switch(type) 
                        {
                        case "UiSection":
                            UiBlock = new UiSection(x, y);
                            break;
                        case "UiPointUp":
                            UiBlock = new UiPointUp(x, y);
                            break;
                        case "UiPointDown":
                            UiBlock = new UiPointDown(x, y);
                            break;
                        case "UiPointUpInverse":
                            UiBlock = new UiPointUpInverse(x, y);
                            break;
                        case "UiPointDownInverse":
                            UiBlock = new UiPointDownInverse(x, y);
                            break;
                        default:
                            UiBlock = null;
                        }
                        if (UiBlock != null)
                        {
                        	rectangle.prepareForPlacement(type, UiBlock);
                        	controller.getUiNetwork().addUiBlock(UiBlock);
                        }
                        success = true;           
                    }
            	} else if (mode == TransferMode.MOVE) {
            		UiBlock uiBlock = (UiBlock) event.getGestureSource();
            		
                	int row = xToRow(event.getX());
                	int col = yToCol(event.getY());
                	
                	int x = rowToX(row);
                	int y = colToY(col);
                	
                	rectangles[row][col].prepareForPlacement(uiBlock.getClass().getSimpleName(), uiBlock);
                	
                	if ((int)uiBlock.getLayoutX() == x && (int)uiBlock.getLayoutY() == y) {
                		//block wasn't moved, so just add it back to UI
            	        controller.getGrid().getChildren().add(uiBlock);
                	} else {
                		uiBlock.setLayoutX(x);
                		uiBlock.setLayoutY(y);
                		uiBlock.block.detach();
                		controller.getUiNetwork().addUiBlock(uiBlock, false);	
                	}
            		
            		success = true;
            	}
            }
            
            event.setDropCompleted(success);
            event.consume();
        });
	}
	
        /**
         * Convert row X index to pixel coordinate
         * @param row row number which will be converted to coordinate
         * @return integer which represents x coordinate of a rectangle 
         */
	public static int rowToX(int row) {
		return row * CELL_SIZE;
	}
	
        /**
         * Convert row Y index to pixel coordinate
         * @param col column number which will be converted to coordinate
         * @return integer which represents y coordinate of a rectangle 
         */
	public static int colToY(int col) {
		return col * CELL_SIZE;
	}
	
        /**
         * Convert X coordinate in to a index value
         * @param x x coordinate which will be converted to index
         * @return index of x coordinate
         */
	public static int xToRow(double x) {
		return ((int) x) / CELL_SIZE;
	}
	
        /**
         * Convert Y coordinate in to a index value
         * @param y y coordinate which will be converted to index
         * @return index of y coordinate
         */
	public static int yToCol(double y) {
		return ((int) y) / CELL_SIZE;
	}
	
        /**
         * Get all rectangles of the grid
         * @return 2D array of GridRectangles
         */
	public GridRectangle[][] getRectangles() {
		return rectangles;
	}
	
        /**
         * Get controller associated with the grid
         * @return controller which is used to display the grid
         */
	public Controller getController() {
		return controller;
	}
}
