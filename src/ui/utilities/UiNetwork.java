package ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.Network;
import backend.NetworkDeserializationException;
import backend.NetworkSerializationException;
import backend.Point;
import backend.SimpleNetwork;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import ui.Controller;

/**
 * This class will hold and utilize the network from the backend.
 * It implements custom serializer/deserializer to store the locations of each UiBlock in the output.
 * @author kubatek94
 */
public class UiNetwork {
	@JsonIgnore private Network network = new SimpleNetwork();
	private List<UiBlock> uiBlocks = new ArrayList<UiBlock>();
	
	@JsonIgnore private GridRectangle[][] rectangles;
	@JsonIgnore private Group grid;
	@JsonIgnore private Controller controller;
	
	public UiNetwork() {}

	public UiNetwork(Controller controller) {
		//TODO: add null checks?
		this();
		setController(controller);
	}
	
	@JsonProperty
	public void setUiBlocks(List<UiBlock> uiBlocks) {
		this.uiBlocks = uiBlocks;
		for (UiBlock b : uiBlocks) {
			network.addBlock(b.block);
		}
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
		rectangles = controller.getRectangles();
		grid = controller.getGrid();
	}
	
	public Network getNetwork() {
		return network;
	}
        
    public void addUiBlock(UiBlock uiBlock) {
        addUiBlock(uiBlock, true);
    }
	
	public void addUiBlock(UiBlock uiBlock, boolean addToUiBlocks) {
        int i = ((int) uiBlock.getLayoutX() / Controller.CELL_SIZE);
        int j = ((int) uiBlock.getLayoutY() / Controller.CELL_SIZE);
        
        boolean isUiBlockSection = uiBlock.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
        
        UiBlock target = (isUiBlockSection ? null : uiBlock);
        
        UiBlock leftNeighbourTop = null;
        UiBlock leftNeighbourBottom = null;
        
        UiBlock rightNeighbourTop = null;
        UiBlock rightNeighbourBottom = null;
        
        if (i != 0) {
        	leftNeighbourTop = rectangles[i-1][j].getUiBlock();
        	
        	if (j+1 < Controller.GRID_HEIGHT) {
        		leftNeighbourBottom = rectangles[i-1][j+1].getUiBlock();
        	}
        }
        
        if (i+2 < Controller.GRID_WIDTH) {
        	rightNeighbourTop = rectangles[i+2][j].getUiBlock();
        	
        	if (j+1 < Controller.GRID_HEIGHT) {
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
	
	public void deleteUiBlocks(boolean selectedOnly) {
        ObservableList<Node> children = controller.getGrid().getChildren();
        
        if (selectedOnly) {
        	ArrayList<UiBlock> found = new ArrayList<UiBlock>();
        	
            for (UiBlock b : uiBlocks) {
                if (b.isSelected()) {
                    network.removeBlock(b.block);
                    rectangles[((int) b.getLayoutX() / Controller.CELL_SIZE)][((int)b.getLayoutY() / Controller.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
                    children.remove(b);
                    found.add(b);
                }
            }
            
            uiBlocks.removeAll(found);
        } else {
            for (UiBlock b : uiBlocks) {
                rectangles[((int) b.getLayoutX() / Controller.CELL_SIZE)][((int)b.getLayoutY() / Controller.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
                children.remove(b);
            }
        }
	}
	
	public void refreshUi() {		
        for (UiBlock b : uiBlocks) {
            rectangles[((int) b.getLayoutX() / Controller.CELL_SIZE)][((int)b.getLayoutY() / Controller.CELL_SIZE)].prepareForPlacement(b.getClass().getSimpleName(), b);
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
}
