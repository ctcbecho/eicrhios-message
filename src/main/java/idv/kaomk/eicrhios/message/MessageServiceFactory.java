package idv.kaomk.eicrhios.message;

import idv.kaomk.eicrhios.message.outlook.OutlookMessageService;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageServiceFactory {
	private Logger logger = LoggerFactory.getLogger(MessageServiceFactory.class);
	
	private boolean mOutlookEnabled;
	private String mOutlookUser;
	private String mOutlookServerName;
	private BundleContext mBundleContext;
	private OutlookMessageService mOutlookMessageService;
	
	public void setBundleContext(BundleContext bundleContext) {
		mBundleContext = bundleContext;
	}

	public void setOutlookEnabled(boolean outlookEnabled) {
		this.mOutlookEnabled = outlookEnabled;
	}

	public void setOutlookUser(String outlookUser) {
		mOutlookUser = outlookUser;
	}

	public void setOutlookServerName(String outlookServerName) {
		mOutlookServerName = outlookServerName;
	}

	public void init() {
		if (mOutlookEnabled) {
			mOutlookMessageService = new OutlookMessageService(
					mOutlookServerName, mOutlookUser);
			mBundleContext.registerService(MessageService.class.getName(), mOutlookMessageService, null);
			logger.debug("register OutlookMessageService completed.");
		}
	}
	
	public void destroy(){
		if (mOutlookMessageService != null){
			mOutlookMessageService.destroy();
		}
	}
}
