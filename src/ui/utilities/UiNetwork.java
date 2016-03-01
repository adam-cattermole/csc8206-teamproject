package ui.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.Network;
import backend.NetworkDeserializationException;
import backend.NetworkSerializationException;
import backend.Point;
import backend.Section;
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
	@JsonIgnore public Network network = new SimpleNetwork();
	protected List<UiBlock> uiBlocks = new ArrayList<UiBlock>();
	
	@JsonIgnore private GridRectangle[][] rectangles;
	@JsonIgnore private Group grid;
	@JsonIgnore private Controller controller;
	
	public UiNetwork() {}

	public UiNetwork(Controller controller) {
		//TODO: add null checks?
		setController(controller);
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
		rectangles = controller.getRectangles();
		grid = controller.getGrid();
	}
        
        public void addUiBlock(UiBlock uiBlock) {
            addUiBlock(uiBlock, true);
        }
	
	public void addUiBlock(UiBlock uiBlock, boolean addToUiBlocks) {
            int i = ((int) uiBlock.getLayoutX() / Controller.CELL_SIZE);
            int j = ((int) uiBlock.getLayoutY() / Controller.CELL_SIZE);
            
            boolean isUiBlockSection = uiBlock.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
            
            UiBlock target = (isUiBlockSection ? null : uiBlock);
            
            UiBlock leftNeighbourTop = rectangles[i-1][j].getUiBlock();
            UiBlock leftNeighbourBottom = rectangles[i-1][j+1].getUiBlock();
            
            UiBlock rightNeighbourTop = rectangles[i+2][j].getUiBlock();
            UiBlock rightNeighbourBottom = rectangles[i+2][j+1].getUiBlock();
            
            //boolean isLeftNeighbourSection = leftNeighbourTop.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
            
            /*if (isUiBlockSection) {
                if (leftNeighbourTop != null) {
                    target = leftNeighbourTop;
                    
                    if (target instanceof UiSection) {
                        ((UiSection) target).block.setUp(uiBlock.block);
                        System.out.println("Left: add section to section");
                    } else {
                        Point point = (Point) target.block;
                        
                        if (isNeighbourOnMainLine(uiBlock, leftNeighbourTop)) {
                            point.setUp(uiBlock.block);
                            System.out.println("Left: add section to mainline");
                        } else if (isNeighbourOnSideLine(uiBlock, leftNeighbourTop, true)) {
                            point.setSideline(uiBlock.block);
                            System.out.println("Left: add section to sideline");
                        }
                    }
                }
                
                if (rightNeighbourTop != null) {
                    target = rightNeighbourTop;
                    
                    if (target instanceof UiSection) {
                        ((UiSection) target).block.setDown(uiBlock.block);
                        System.out.println("Right: add section to section");
                    } else {
                        Point point = (Point) target.block;
                        
                        if (isNeighbourOnMainLine(uiBlock, rightNeighbourTop)) {
                            point.setDown(uiBlock.block);
                            System.out.println("Right: add section to mainline");
                        } else if (isNeighbourOnSideLine(uiBlock, rightNeighbourTop, false)) {
                            point.setSideline(uiBlock.block);
                            System.out.println("Right: add section to sideline");
                        }
                    }
                }
            } else {
                Point point = (Point) target.block;
                
                if (leftNeighbourTop instanceof UiSection) {
                    if (isNeighbourOnMainLine(uiBlock, leftNeighbourTop)) {
                        point.setDown(leftNeighbourTop.block);
                        System.out.println("Right: add section to mainline");
                    } else if (isNeighbourOnSideLine(uiBlock, leftNeighbourTop, false)) {
                        point.setSideline(leftNeighbourTop.block);
                        System.out.println("Right: add section to sideline");
                    }
                }
                
                if (leftNeighbourBottom instanceof UiSection) {
                    if (isNeighbourOnMainLine(uiBlock, leftNeighbourBottom)) {
                        point.setDown(leftNeighbourBottom.block);
                        System.out.println("Right: add section to mainline");
                    } else if (isNeighbourOnSideLine(uiBlock, leftNeighbourBottom, false)) {
                        point.setSideline(leftNeighbourBottom.block);
                        System.out.println("Right: add section to sideline");
                    }
                }
                
                if (rightNeighbourTop instanceof UiSection) {
                    if (isNeighbourOnMainLine(uiBlock, rightNeighbourTop)) {
                        point.setUp(rightNeighbourTop.block);
                        System.out.println("Right: add section to mainline");
                    } else if (isNeighbourOnSideLine(uiBlock, rightNeighbourTop, true)) {
                        point.setSideline(rightNeighbourTop.block);
                        System.out.println("Right: add section to sideline");
                    }
                }
                
                if (rightNeighbourBottom instanceof UiSection) {
                    if (isNeighbourOnMainLine(uiBlock, rightNeighbourBottom)) {
                        point.setUp(rightNeighbourBottom.block);
                        System.out.println("Right: add section to mainline");
                    } else if (isNeighbourOnSideLine(uiBlock, rightNeighbourBottom, true)) {
                        point.setSideline(rightNeighbourBottom.block);
                        System.out.println("Right: add section to sideline");
                    }
                }
            }*/
            
            grid.getChildren().add(uiBlock);
            network.addBlock(uiBlock.block);
            
            //if (addToUiBlocks) {
                uiBlocks.add(uiBlock);
            //}

            
            /*if (leftNeighbour != null) {
                boolean isLeftNeighbourSection = leftNeighbour.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
                
                if (target == null && !isLeftNeighbourSection) {
                    target = leftNeighbour;
                } else if (target != null && !isLeftNeighbourSection) {
                    System.out.println("Point + Point");
                }
            }
            
            System.out.println("Left:" + leftNeighbour + " Target:" + target.block);*/
            
            /*if (rightNeighbour != null) {
                boolean isRightNeighbourSection = leftNeighbour.getClass().getSimpleName().equals(UiSection.class.getSimpleName());
            }*/
            
            
            
            
            /*if (uiBlock.getClass().getSimpleName().equals(UiSection.class.getSimpleName())) {
                
            } else {
                if (leftNeighbour != null) {
                    System.out.println(isNeighbourOnSideLine(uiBlock, leftNeighbour, true));
                }

                if (rightNeighbour != null) {
                    System.out.println(isNeighbourOnSideLine(uiBlock, rightNeighbour, false));
                }  
            }*/
            

           
            // If block is a section
            /*if (uiBlock.getClass().getSimpleName().equals(UiSection.class.getSimpleName()))
            {
                // Check left neighbour
                if (rectangles[i-1][j].isUsed())
                {
                    UiBlock neighbour = rectangles[i-1][j].getUiBlock();
                    boolean aligned = areBlocksAligned(uiBlock, neighbour);
                    System.out.println("Current Block: "+uiBlock.getClass().getSimpleName()+", Neighbour Block: "+neighbour.getClass().getSimpleName()+", Alignment: "+aligned);
                    
                    
                    if (neighbour.getClass().getSimpleName().equals(UiSection.class.getSimpleName()))
                    {
                        // Neighbour = Section
                        //TODO: Section connects to a section
                        Section section = (Section) neighbour.block;
                        section.setUp(uiBlock.block);
                    }
                    else if (neighbour.getClass().getSimpleName().equals(UiPointUp.class.getSimpleName()))
                    {
                        // Neighbour = PointUp
                        if (aligned)
                        {
                            //TODO: Section connects to a side line of point up
                            Point point = (Point) neighbour.block;
                            point.setSideline(uiBlock.block);
                        }
                        else
                        {
                            //TODO: Section connects to a main line of point up
                            Point point = (Point) neighbour.block;
                            point.setUp(uiBlock.block);
                        }
                    }
                    else if (neighbour.getClass().getSimpleName().equals(UiPointDown.class.getSimpleName()))
                    {
                        // Neighbour = PointDown
                        if (aligned)
                        {
                            //TODO: Section connects to a main line of point down
                           
                        }
                        else
                        {
                             //TODO: Section connects to a side line of point down
                        }
                    }
                    else if (neighbour.getClass().getSimpleName().equals(UiPointUpInverse.class.getSimpleName()))
                    {
                        // Neighbour = PointUpInverse
                        if (!aligned)
                        {
                            //TODO: Section connects to a main line of point up inverse
                        }
                    }
                    else if (neighbour.getClass().getSimpleName().equals(UiPointDownInverse.class.getSimpleName()))
                    {
                        // Neighbour = PointDownInverse
                        if (aligned)
                        {
                            //TODO: Section connects to a side line of point down inverse
                        }
                    }
                }
            }*/      
            
	    //TODO: Here we will need to find the neighbours (based on coordinates of uiBlock) and add the Block from uiBlock to the SimpleNetwork backend.
	}
        
        private boolean isNeighbourOnMainLine(UiBlock block, UiBlock neighbour)
        {
            boolean isInverse = neighbour.getClass().getSimpleName().contains("Inverse");
            boolean isAligned = isNeighbourAligned(block, neighbour);
            
            return !isInverse ? !isAligned : isAligned;
        }
        
        private boolean isNeighbourOnSideLine(UiBlock block, UiBlock neighbour, boolean leftNeighbour)
        {
            boolean isInverse = neighbour.getClass().getSimpleName().contains("Inverse");
            boolean isAligned = isNeighbourAligned(block, neighbour);
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
                network.removeBlock(b.block);
            }
            
            uiBlocks.clear();
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
	public static UiNetwork load(Controller controller, InputStream stream) throws NetworkDeserializationException
	{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			UiNetwork uiNetwork = jsonMapper.readValue(stream, UiNetwork.class);
			uiNetwork.setController(controller);
			
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
