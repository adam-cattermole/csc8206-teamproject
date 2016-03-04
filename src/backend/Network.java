package backend;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author chris_curry
 * 
 * Interface defining behaviour of a Rail Network
 * Created so that the implementation can be modified without affecting
 * classes built on top of the network
 * 
 * If you need to add more functionality, at this stage, feel free
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=SimpleNetwork.class, name="SimpleNetwork")
})
public interface Network {

	/**
	 * Method to add a new block to the network
	 * @param block - block which will now be in the network set
	 * @return Network - return itself to allow for chaining
	 */
	public Network addBlock(Block block);
	
	/**
	 * Removes a block from the network.
	 * Does not guarantee that the network will be valid after removal.
	 * @param block - the block to remove from the network
	 * @return Network - return itself to allow for chaining
	 */
	public Network removeBlock(Block block);
	
	/**
	 * Returns all blocks in the network.
	 * @return Collection<Block> - the iterable collection of blocks that make up this network.
	 */
	public Collection<Block> getBlocks();
	
	/**
	 * Creates new Point block and adds it to this network
	 * @param orientation
	 * @return returns the created Point
	 */
	public Point makePoint(Point.Orientation orientation);
	
	/**
	 * Create new Section block and adds it to this network
	 * @return returns the created Section
	 */
	public Section makeSection();
	
	/**
	 * Serialize this network to JSON and write it to the OutputStream
	 * @param stream - OutputStream to which the serialized network will be written
	 * @throws NetworkSerializationException
	 */
	public Network save(OutputStream stream) throws NetworkSerializationException;
	
	/**
	 * Reads JSON from InputStream and deserializes into a Network object 
	 * @param stream - InputStream with the JSON input
	 * @throws NetworkDeserializationException
	 */
	public static Network load(InputStream stream) throws NetworkDeserializationException
	{
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			Network network = jsonMapper.readValue(stream, Network.class);
			return network;
		} catch (IOException e) {
			throw new NetworkDeserializationException(e);
		}
	}
	
	/**
	 * Checks the networks validity according to rules we haven't really decided yet
	 * @return True if the network is valid, False otherwise
	 */
	public boolean isValid() throws BlockInvalidException;
}