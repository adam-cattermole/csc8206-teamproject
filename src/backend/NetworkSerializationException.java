package backend;

public class NetworkSerializationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NetworkSerializationException(Exception cause) {
		super(cause);
	}
}
