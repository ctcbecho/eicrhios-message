package idv.kaomk.eicrhios.message.outlook;

import com.jacob.com.Variant;



public class MailEvent  {
	
	public void ItemAdd(Variant[] args) {
		System.out.println("1111111111");
	}

	public void ItemRemove(Variant[] args) {
		System.out.println("222222222222");
	}
	
	
	  public void AttachmentAdd(Variant[] arguments) {
	        System.out.println("AttachmentAdd");
	    }

	    public void AttachmentRead(Variant[] arguments) {
	        System.out.println("AttachmentRead");
	    }

	    public void AttachmentRemove(Variant[] arguments) {
	        System.out.println("AttachmentRemove");
	    }

	    public void BeforeAttachmentAdd(Variant[] arguments) {
	        System.out.println("BeforeAttachmentAdd");
	    }

	    public void BeforeAttachmentPreview(Variant[] arguments) {
	        System.out.println("BeforeAttachmentPreview");
	    }

	    public void BeforeAttachmentRead(Variant[] arguments) {
	        System.out.println("BeforeAttachmentRead");
	    }

	    public void BeforeAttachmentSave(Variant[] arguments) {
	        System.out.println("BeforeAttachmentSave");
	    }

	    public void BeforeAttachmentWriteToTempFile(Variant[] arguments) {
	        System.out.println("BeforeAttachmentWriteToTempFile");
	    }

	    public void BeforeAutoSave(Variant[] arguments) {
	        System.out.println("BeforeAutoSave");
	    }

	    public void BeforeCheckNames(Variant[] arguments) {
	        System.out.println("BeforeCheckNames");
	    }

	    public void BeforeDelete(Variant[] arguments) {
	        System.out.println("BeforeDelete");
	    }

	    public void Close(Variant[] arguments) {
	        System.out.println("Close");
	    }

	    public void CustomAction(Variant[] arguments) {
	        System.out.println("CustomAction");
	    }

	    public void CustomPropertyChange(Variant[] arguments) {
	        System.out.println("CustomPropertyChange");
	    }

	    public void Forward(Variant[] arguments) {
	        System.out.println("Forward");
	    }

	    public void Open(Variant[] arguments) {
	        System.out.println("Open");
	    }

	    public void PropertyChange(Variant[] arguments) {
	        System.out.println("PropertyChange");
	    }

	    public void Read(Variant[] arguments) {
	        System.out.println("Read");
	    }

	    public void Reply(Variant[] arguments) {
	        System.out.println("Reply");
	    }

	    public void ReplyAll(Variant[] arguments) {
	        System.out.println("ReplyAll");
	    }

	    public void Send(Variant[] arguments) {
	        System.out.println("Send");
	    }

	    public void Unload(Variant[] arguments) {
	        System.out.println("Unload");
	    }

	    public void Write(Variant[] arguments) {
	        System.out.println("Write");
	    }


}
