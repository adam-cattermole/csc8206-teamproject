
public class NetworkSerializationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception reason;
	
	public NetworkSerializationException(Exception reason) {
		this.reason = reason;
	}
	
	public Exception getReason() {
		return reason;
	}
}
