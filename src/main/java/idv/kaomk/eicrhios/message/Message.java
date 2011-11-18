package idv.kaomk.eicrhios.message;

public interface Message {
	public Object getContent();
	public String[] getRecipients();
	public String getFrom();
	public Message reply();
	public void setContent(Object content);
}
