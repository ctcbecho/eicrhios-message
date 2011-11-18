package idv.kaomk.eicrhios.message;

public class MessageSessionContext {
	public String mFrom;
	public MessageService mMessageService;
	
	
	public MessageSessionContext(String from, MessageService messageService) {
		super();
		mFrom = from;
		mMessageService = messageService;
	}

	public String getFrom() {
		return mFrom;
	}

	public MessageService getMessageService() {
		return mMessageService;
	}
	
	
}
