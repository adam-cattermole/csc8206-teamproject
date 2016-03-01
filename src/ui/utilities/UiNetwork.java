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
        grid.getChildren().add(uiBlock);
		uiBlocks.add(uiBlock);
		
		//TODO: Here we will need to find the neighbours (based on coordinates of uiBlock) and add the Block from uiBlock to the SimpleNetwork backend.
	}
	
	public void deleteUiBlocks(boolean selectedOnly) {
        ObservableList<Node> children = controller.getGrid().getChildren();
        
        if (selectedOnly) {
        	ArrayList<UiBlock> found = new ArrayList<UiBlock>();
        	
            for (UiBlock b : uiBlocks) {
                if (b.isSelected()) {
                    rectangles[((int) b.getX() / Controller.CELL_SIZE)][((int)b.getY() / Controller.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
                    children.remove(b);
                    found.add(b);
                }
            }
            
            uiBlocks.removeAll(found);
        } else {
            for (UiBlock b : uiBlocks) {
                rectangles[((int) b.getX() / Controller.CELL_SIZE)][((int)b.getY() / Controller.CELL_SIZE)].freeUpSpace(b.getClass().getSimpleName());
                children.remove(b);
            }
            
            uiBlocks.clear();
        }
	}
	
	public void refreshUi() {
		ObservableList<Node> children = controller.getGrid().getChildren();
		children.addAll(uiBlocks);
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
