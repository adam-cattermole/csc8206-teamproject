package backend;

public class NetworkDeserializationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NetworkDeserializationException(Exception cause) {
		super(cause);
	}
}
