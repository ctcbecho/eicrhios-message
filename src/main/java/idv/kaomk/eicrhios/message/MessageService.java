package idv.kaomk.eicrhios.message;

public interface MessageService {
	public void sendMessage(Message message);
	
	public void addMessageListener(MessageListener listener);
	
	public void removeMessageListener(MessageListener listener);
	
	
}
