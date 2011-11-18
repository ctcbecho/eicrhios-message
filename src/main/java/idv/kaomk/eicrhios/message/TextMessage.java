package idv.kaomk.eicrhios.message;

public class TextMessage implements Message{
	private String mText;
	private String[] mRecipients;
	private String mFrom;
	
	protected TextMessage(){
		super();
	}

	@Override
	public Object getContent() {
		return mText;
	}

	public String getText() {
		return mText;
	}

	public String[] getRecipients() {
		return mRecipients;
	}

	protected void setFrom(String from){
		this.mFrom = from;
	}
	
	
	public String getFrom() {
		return mFrom;
	}

	protected void setRecipients(String[] recipients) {
		mRecipients = recipients;
	}

	@Override
	public Message reply() {
		TextMessage msg = new TextMessage();
		msg.setRecipients(new String[]{mFrom});
		return msg;
	}

	@Override
	public void setContent(Object content) {
		if (content instanceof String[]){
			StringBuffer sb = new StringBuffer();
			String[] lines = (String[]) content;
			
			for (String line: lines){
				sb.append(line.trim()).append("\n");
			}
			setText(sb.toString());
		}
		else{
			setText(content.toString());
		}
		
	}
	
	protected void setText(String text){
		this.mText = text;
	}
}
