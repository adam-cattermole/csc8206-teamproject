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
	 * Checks the networks validity according to rules we haven't really decided yet
	 * @return True if the network is valid, False otherwise
	 */
	public boolean isValid();
}