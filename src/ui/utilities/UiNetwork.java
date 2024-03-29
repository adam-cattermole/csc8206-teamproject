package ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.BlockInvalidException;
import backend.Network;
import backend.NetworkDeserializationException;
import backend.NetworkSerializationException;
import backend.Point;
import backend.SimpleNetwork;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import ui.Controller;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import route.RouteBuilder;
import route.RouteBuilder.Route;
import route.RouteConflictDetector;
import javafx.event.EventHandler;

/**
 * This class will hold and utilize the network from the backend.
 * It implements custom serializer/deserializer to store the locations of each UiBlock in the output.
 * @author kubatek94
 */
public class UiNetwork implements CtrlKeyListener {
	@JsonIgnore public static String NETWORK_VALID = "Network is VALID";
	@JsonIgnore public static String NETWORK_INVALID = "Network is INVALID";
	
	@JsonIgnore private Network network = new SimpleNetwork();
	private List<UiBlock> uiBlocks = new ArrayList<UiBlock>();
	
	@JsonIgnore private GridRectangle[][] rectangles;
	@JsonIgnore private Group grid;
	@JsonIgnore private Controller controller;
	
	@JsonIgnore private EventHandler<MouseEvent> blockClickHandler;
	@JsonIgnore private EventHandler<MouseEvent> blockMouseEnterHandler;
	@JsonIgnore private EventHandler<MouseEvent> blockMouseExitHandler;
	@JsonIgnore private EventHandler<MouseEvent> blockDragDetectedHandler;
	@JsonIgnore private EventHandler<DragEvent> blockDragDoneHandler;
	
	@JsonIgnore private RouteBuilder routeBuilder;
	@JsonIgnore private List<UiBlock> routeBlocks;
	@JsonIgnore ObservableList<UiRoute> routes;
	
	@JsonIgnore private boolean valid = false;
	
	public UiNetwork() {
		blockClickHandler = (event) -> {
			MouseButton button = event.getButton();
			
			if (event.getSource() instanceof UiBlock) {
				UiBlock b = (UiBlock) event.getSource();
				
				//only allow right mouse click, if we aren't building route
				if (isBuildingRoute()) {
					if (button == MouseButton.PRIMARY) {
						routeBlocks.add(b);
						routeBuilder.addToRoute(b.block);
						
						b.setHighlight(true);
					}
				} else {
					if (button == MouseButton.SECONDARY) {
		    			removeUiBlock(b);
		    		}
				}
			}
    		
    		event.consume();
		};
		
		blockMouseEnterHandler = (event) -> {
			if (event.getSource() instanceof UiBlock) {
				UiBlock b = (UiBlock) event.getSource();

				if (isBuildingRoute()) {
					b.setCursor(Cursor.CROSSHAIR);
				}
			}
		};
		
		blockMouseExitHandler = (event) -> {
			if (event.getSource() instanceof UiBlock) {
				UiBlock b = (UiBlock) event.getSource();
				b.setCursor(Cursor.DEFAULT);
			}
		};
		
		blockDragDetectedHandler = (event) -> {
			UiBlock block = (UiBlock) event.getSource();
			Dragboard db = block.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(block.getClass().getSimpleName());
			db.setContent(content);
			
			//remove block from UI
	        grid.getChildren().remove(block);
	        rectangles[((int) block.getLayoutX() / GridRectangles.CELL_SIZE)][((int)block.getLayoutY() / GridRectangles.CELL_SIZE)].freeUpSpace(block.getClass().getSimpleName());
			
			event.consume();
		};
		
		blockDragDoneHandler = (event) -> {
			if (event.getTransferMode() != TransferMode.MOVE) {
				//drag not successful, restore object
				UiBlock block = (UiBlock) event.getSource();
    	        grid.getChildren().add(block);
    	        rectangles[((int) block.getLayoutX() / GridRectangles.CELL_SIZE)][((int)block.getLayoutY() / GridRectangles.CELL_SIZE)].prepareForPlacement(block.getClass().getSimpleName(), block);
			}
			
			event.consume();
		};
	}

	public UiNetwork(Controller controller) {
		this();
		setController(controller);
	}
	
	@JsonIgnore
	public boolean isBuildingRoute() {
		return routeBuilder != null;
	}
	
	@JsonProperty
	public void setUiBlocks(List<UiBlock> uiBlocks) {
		this.uiBlocks = uiBlocks;
		for (UiBlock b : uiBlocks) {
			b.setOnMouseClicked(blockClickHandler);
			b.setOnMouseEntered(blockMouseEnterHandler);
			b.setOnMouseExited(blockMouseExitHandler);
			b.setOnDragDetected(blockDragDetectedHandler);
			b.setOnDragDone(blockDragDoneHandler);
			network.addBlock(b.block);
		}
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
		rectangles = controller.getGridRectangles().getRectangles();
		grid = controller.getGrid();
		routes = controller.getInterlockTable().getItems();
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public void removeUiBlock(UiBlock b) {
		//remove element from various places
        network.removeBlock(b.block);
        rectangles[((int) b.getLayoutX() / GridRectangles.CELL_SIZE)][((int)b.getLayoutY() / GridRectangles.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
        grid.getChildren().remove(b);
        uiBlocks.remove(b);
        revalidateNetwork();
	}
        
    public void addUiBlock(UiBlock uiBlock) {
        addUiBlock(uiBlock, true);
        uiBlock.setOnMouseClicked(blockClickHandler);
		uiBlock.setOnMouseEntered(blockMouseEnterHandler);
		uiBlock.setOnMouseExited(blockMouseExitHandler);
		uiBlock.setOnDragDetected(blockDragDetectedHandler);
		uiBlock.setOnDragDone(blockDragDoneHandler);
    }
	
	public void addUiBlock(UiBlock uiBlock, boolean addToUiBlocks) {
        int i = ((int) uiBlock.getLayoutX() / GridRectangles.CELL_SIZE);
        int j = ((int) uiBlock.getLayoutY() / GridRectangles.CELL_SIZE);
        
        boolean isUiBlockSection = uiBlock.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
        
        UiBlock target = (isUiBlockSection ? null : uiBlock);
        
        UiBlock leftNeighbourTop = null;
        UiBlock leftNeighbourBottom = null;
        
        UiBlock rightNeighbourTop = null;
        UiBlock rightNeighbourBottom = null;
        
        if (i != 0) {
        	leftNeighbourTop = rectangles[i-1][j].getUiBlock();
        	
        	if (j+1 < GridRectangles.GRID_HEIGHT) {
        		leftNeighbourBottom = rectangles[i-1][j+1].getUiBlock();
        	}
        }
        
        if (i+2 < GridRectangles.GRID_WIDTH) {
        	rightNeighbourTop = rectangles[i+2][j].getUiBlock();
        	
        	if (j+1 < GridRectangles.GRID_HEIGHT) {
        		rightNeighbourBottom = rectangles[i+2][j+1].getUiBlock();
        	}
        }
        
        if (isUiBlockSection) {
            if (leftNeighbourTop != null) {
                target = leftNeighbourTop;
                
                if (target instanceof UiSection) {
                    ((UiSection) target).block.setUp(uiBlock.block);
                } else {
                    Point point = (Point) target.block;
                    
                    if (isNeighbourOnMainLine(uiBlock, leftNeighbourTop)) {
                        point.setUp(uiBlock.block);
                    } else if (isNeighbourOnSideLine(uiBlock, leftNeighbourTop, true)) {
                        point.setSideline(uiBlock.block);
                    }
                }
            }
            
            if (rightNeighbourTop != null) {
                target = rightNeighbourTop;
                
                if (target instanceof UiSection) {
                    ((UiSection) target).block.setDown(uiBlock.block);
                } else {
                    Point point = (Point) target.block;
                    
                    if (isNeighbourOnMainLine(uiBlock, rightNeighbourTop)) {
                        point.setDown(uiBlock.block);
                    } else if (isNeighbourOnSideLine(uiBlock, rightNeighbourTop, false)) {
                        point.setSideline(uiBlock.block);
                    }
                }
            }
        } else {
            Point point = (Point) target.block;
            
            if (leftNeighbourTop instanceof UiSection) {
                if (isNeighbourOnMainLine(leftNeighbourTop, uiBlock)) {
                    point.setDown(leftNeighbourTop.block);
                } else if (isNeighbourOnSideLine(leftNeighbourTop, uiBlock, false)) {
                    point.setSideline(leftNeighbourTop.block);
                }
            }
            
            if (leftNeighbourBottom instanceof UiSection) {
                if (isNeighbourOnMainLine(leftNeighbourBottom, uiBlock)) {
                    point.setDown(leftNeighbourBottom.block);
                } else if (isNeighbourOnSideLine(leftNeighbourBottom, uiBlock, false)) {
                    point.setSideline(leftNeighbourBottom.block);
                }
            }
            
            if (rightNeighbourTop instanceof UiSection) {
                if (isNeighbourOnMainLine(rightNeighbourTop, uiBlock)) {
                    point.setUp(rightNeighbourTop.block);
                } else if (isNeighbourOnSideLine(rightNeighbourTop, uiBlock, true)) {
                    point.setSideline(rightNeighbourTop.block);
                }
            }
            
            if (rightNeighbourBottom instanceof UiSection) {
                if (isNeighbourOnMainLine(rightNeighbourBottom, uiBlock)) {
                    point.setUp(rightNeighbourBottom.block);
                } else if (isNeighbourOnSideLine(rightNeighbourBottom, uiBlock, true)) {
                    point.setSideline(rightNeighbourBottom.block);
                }
            }
        }
        
        grid.getChildren().add(uiBlock);
        if (addToUiBlocks) {
        	network.addBlock(uiBlock.block);
            uiBlocks.add(uiBlock);
        }
        
        revalidateNetwork();
	}
	
	private void revalidateNetwork() {
        //revalidate the network
        try {
        	valid = network.isValid();
        	controller.setStatusText(valid ? NETWORK_VALID : NETWORK_INVALID);
        } catch (BlockInvalidException e) {
        	valid = false;
        	controller.setStatusText(NETWORK_INVALID + ": " + e.getBlock());
        }
	}
        
    private boolean isNeighbourOnMainLine(UiBlock block, UiBlock neighbour)
    {
        boolean isInverse = neighbour.getClass().getSimpleName().contains("Inverse");
        boolean isAligned = isNeighbourAligned(block, neighbour);
        return !isInverse ? !isAligned : isAligned;
    }
    
    private boolean isNeighbourOnSideLine(UiBlock block, UiBlock neighbour, boolean leftNeighbour)
    {
        boolean isUp = neighbour.getClass().getSimpleName().contains("Up");
        boolean isOnMainline = isNeighbourOnMainLine(block, neighbour);
        
        if (isUp && !isOnMainline)
            return leftNeighbour;
        
        if (!isUp && !isOnMainline) {
            return !leftNeighbour;
        }
        
        return false;
    }
    
    
    private boolean isNeighbourAligned(UiBlock block, UiBlock neighbour)
    {
        if (block.getLayoutY() == neighbour.getLayoutY())
            return true;
        else
            return false;
    }
    
    public void clear() {
    	ObservableList<Node> children = controller.getGrid().getChildren();
    	
        for (UiBlock b : uiBlocks) {
            rectangles[((int) b.getLayoutX() / GridRectangles.CELL_SIZE)][((int)b.getLayoutY() / GridRectangles.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
            children.remove(b);
        }
        
        routes.clear();
    }
	
	public void refreshUi() {		
        for (UiBlock b : uiBlocks) {
            rectangles[((int) b.getLayoutX() / GridRectangles.CELL_SIZE)][((int)b.getLayoutY() / GridRectangles.CELL_SIZE)].prepareForPlacement(b.getClass().getSimpleName(), b);
            addUiBlock(b, false);
        }
	}
	
	/**
	 * Reads JSON from InputStream and deserializes into a UiNetwork object 
	 * @param stream - InputStream with the JSON input
	 * @throws NetworkDeserializationException
	 */
	public static UiNetwork load(InputStream stream) throws NetworkDeserializationException
	{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			UiNetwork uiNetwork = jsonMapper.readValue(stream, UiNetwork.class);
			return uiNetwork;
		} catch (IOException e) {
			throw new NetworkDeserializationException(e);
		}
	}
	
	public UiNetwork save(OutputStream stream) throws NetworkSerializationException
	{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			jsonMapper.writerWithDefaultPrettyPrinter().writeValue(stream, this);
			return this;
		} catch (IOException e) {
			throw new NetworkSerializationException(e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (UiBlock block : uiBlocks) {
			sb.append(block.toString()).append("\n");
		}
		
		return sb.toString();
	}

	@Override
	public void onCtrlDown() {
		if (valid && !isBuildingRoute()) {
			routeBuilder = new RouteBuilder();
			routeBlocks = new ArrayList<UiBlock>();
		}
	}

	@Override
	public void onCtrlUp() {
		if (isBuildingRoute()) {
			try {
				Route route = routeBuilder.build();
				
				UiRoute uiRoute = new UiRoute(route);
				uiRoute.setUiBlocks(routeBlocks);
				
				uiRoute.addChangeListener(change -> {
					if (change.wasRemoved()) {
						//remove route
						routes.remove(change.getElement());
						
						//and now recalculate the conflicts
						RouteConflictDetector.calculateConflicts(routes.stream().map(r -> r.getRoute()).collect(Collectors.toList()));
					}
				});
				
				routes.add(uiRoute);
				
				//calculate conflicts
				RouteConflictDetector.calculateConflicts(routes.stream().map(r -> r.getRoute()).collect(Collectors.toList()));
			} catch (IllegalArgumentException e) {}
			
			//remove highlights from the elements
			routeBlocks.stream().forEach(b -> b.setHighlight(false));
			
			routeBuilder = null;
			routeBlocks = null;
		}
	}
	
	@JsonIgnore
	public boolean getValidity()
	{
		return valid;
	}
}
