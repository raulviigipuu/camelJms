package camelJms;

/**
 * Incoming message
 * 
 * @author raul
 */
public final class Message {

	public String id;
	public String timestamp;
	public String content;
	
	public boolean isValid() {
		
		return ( ! isNullOrEmpty(id) && ! isNullOrEmpty(timestamp) && ! isNullOrEmpty(content));
	}
	
	public boolean isNullOrEmpty(Object obj) {
		
		if(obj == null) return true;
		if(obj instanceof String && ((String) obj).length() == 0) return true;
		
		return false;
	}
}
