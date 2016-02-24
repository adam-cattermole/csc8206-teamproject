
public class NetworkDeserializationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception reason;
	
	public NetworkDeserializationException(Exception reason) {
		this.reason = reason;
	}
	
	public Exception getReason() {
		return reason;
	}
}
