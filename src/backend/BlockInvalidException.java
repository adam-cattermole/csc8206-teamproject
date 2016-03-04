package backend;

public class BlockInvalidException extends Exception {
	private static final long serialVersionUID = 1L;
	private Block block;
	
	public BlockInvalidException(Block block){ 
		super("Block is invalid: " + block.toString());
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}
}
