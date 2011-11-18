package idv.kaomk.eicrhios.message.outlook;

import idv.kaomk.eicrhios.message.Message;
import idv.kaomk.eicrhios.message.TextMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OutlookMessage extends TextMessage {
	private List<File> mAttachments = new ArrayList<File>();

	private String mSubject;

	protected OutlookMessage() {
		super();
	}

	public String getSubject() {
		return mSubject;
	}

	public void setSubject(String subject) {
		mSubject = subject;
	}

	public void addAttachment(File file) {
		mAttachments.add(file);
	}

	public File[] getAttachments() {
		return mAttachments.toArray(new File[mAttachments.size()]);
	}
	
	@Override
	protected void setFrom(String from){
		super.setFrom(from);
	}
	
	
	@Override
	protected void setRecipients(String[] recipients) {
		// TODO Auto-generated method stub
		super.setRecipients(recipients);
	}

	@Override
	public Message reply() {
		OutlookMessage msg = new OutlookMessage();
		msg.setRecipients(new String[] { getFrom() });
		msg.setSubject(String.format("Re: %s", getSubject()));
		return msg;
	}
}
