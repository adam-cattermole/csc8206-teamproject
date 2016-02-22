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
public interface Network
{
	enum BlockLink
	{
		UP, DOWN, SIDE;
	}
	
	/**
	 * Method to add a new section to the network
	 * @param addTo - the block to which the new Section will be added to
	 * @param up - 	True if the new section will be added in the "up" direction, 
	 * 				false if added in the "down" direction
	 */
	public void addSection(Block addToBlock, BlockLink addToLink);
	
	/**
	 * Method to add a new point to the network
	 * @param addTo - Block to which the new Point block will be added to
	 * @param up - 	True if the point will be added in the "up" direction,
	 * 				false if added in the "down" direction
	 */
	public void addPoint(Block addToBlock, BlockLink addToLink, Point.PointOrientation orientation);
	
	/**
	 * Removes a block from the network.
	 * Does not guarantee that the network will be valid after removal.
	 * @param toRemove - the block to remove from the network
	 */
	public void removeBlock(Block toRemove);
	
	/**
	 * Checks the networks validity according to rules we haven't really decided yet
	 * @return True if the network is valid, False otherwise
	 */
	public boolean validNetwork();
	
	
}