package ui.utilities;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import ui.Controller;

public class GridRectangles extends Group {
    public static final int GRID_HEIGHT = 50;
    public static final int GRID_WIDTH = 50;
    public static final int CELL_SIZE = 30;
    
	private GridRectangle[][] rectangles;
	private Controller controller;
	
	private int lastRectangleRow = 0;
	private int lastRectangleCol = 0;
	
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
            	int row = xToRow(event.getX());
            	int col = yToCol(event.getY());
            	GridRectangle rectangle = rectangles[row][col];
            	
                String type = db.getString();
                UiBlock UiBlock;
                
                if (!rectangle.isPlacementValid(type))
                {
                    System.out.println("Invalid placement!");
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
            }
            
            event.setDropCompleted(success);
            event.consume();
        });
	}
	
	public static int rowToX(int row) {
		return row * CELL_SIZE;
	}
	
	public static int colToY(int col) {
		return col * CELL_SIZE;
	}
	
	public static int xToRow(double x) {
		return ((int) x) / CELL_SIZE;
	}
	
	public static int yToCol(double y) {
		return ((int) y) / CELL_SIZE;
	}
	
	public GridRectangle[][] getRectangles() {
		return rectangles;
	}
	
	public Controller getController() {
		return controller;
	}
}
