package idv.kaomk.eicrhios.message;

public interface MessageListener {
	public void onMessageReceived(MessageSessionContext context, Message message);
	public void onSessionClosed(MessageSessionContext context);
}
