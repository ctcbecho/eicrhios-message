package idv.kaomk.eicrhios.message.outlook;

import idv.kaomk.eicrhios.message.Message;
import idv.kaomk.eicrhios.message.MessageListener;
import idv.kaomk.eicrhios.message.MessageService;
import idv.kaomk.eicrhios.message.MessageSessionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.DispatchEvents;
import com.jacob.com.STA;
import com.jacob.com.Variant;

public class OutlookMessageService implements MessageService {
	private Logger logger = LoggerFactory
			.getLogger(OutlookMessageService.class);

	private String mUser;
	private String mServerName;
	private String mDefaultFolder = "";
	private String mDefaultSubject = "";
	private ConcurrentHashMap<String, STA> mEventSTA = new ConcurrentHashMap<String, STA>();
	private ConcurrentHashMap<String, MessageListenerWrapper> mWrapperMap = new ConcurrentHashMap<String, MessageListenerWrapper>();

	public OutlookMessageService(String serverName, String user) {
		super();
		mUser = user;
		mServerName = serverName;
	}

	public void setUser(String user) {
		mUser = user;
	}

	public void setServerName(String serverName) {
		mServerName = serverName;
	}

	public void setDefaultFolder(String defaultFolder) {
		mDefaultFolder = defaultFolder;
	}

	public void setDefaultSubject(String defaultSubject) {
		mDefaultSubject = defaultSubject;
	}

	public void destroy() {
		mWrapperMap.clear();
		logger.info("{} STAs will be destroyed.", mEventSTA.size());
		for (STA sta : mEventSTA.values()) {
			sta.quit();
		}
		logger.info("STAs has be destroyed.");
	}

	@Override
	public void sendMessage(Message message) {
		try {
			ComThread.InitMTA();
			String subject;
			ActiveXComponent outlook = new ActiveXComponent(
					"Outlook.Application");

			Dispatch item = Dispatch.call(outlook, "CreateItem", 0)
					.toDispatch();

			ActiveXComponent safeMailItem = new ActiveXComponent(
					"Redemption.SafeMailItem");
			Dispatch.put(safeMailItem, "Item", item);

			Dispatch recipients = Dispatch.get(safeMailItem, "Recipients")
					.toDispatch();
			for (String recipientStr : message.getRecipients()) {
				Dispatch recipient = Dispatch.call(recipients, "Add", recipientStr).toDispatch();
				Dispatch.call(recipient, "Resolve");
				Dispatch.put(recipient, "SendRichInfo", false);
				
			}

			Dispatch.put(safeMailItem, "Body", message.getContent());
			
//			Dispatch.call(recipients, "ResolveAll");

			if (message instanceof OutlookMessage) {
				OutlookMessage outlookMessage = (OutlookMessage) message;
				subject = outlookMessage.getSubject();

				// add attachments
				Dispatch attachments = Dispatch
						.get(safeMailItem, "attachments").toDispatch();

				for (File file : outlookMessage.getAttachments()) {
					Dispatch.call(attachments, "Add", file.getAbsolutePath());
				}
			} else {
				subject = mDefaultSubject;
			}

			Dispatch.put(safeMailItem, "Subject", subject);
			Dispatch.call(safeMailItem, "Send");
			logger.info("Send Outlook mail subject:{} successfully.", subject);
		} finally {
//			ComThread.Release();
		}

	}

	@Override
	public void addMessageListener(MessageListener listener) {
		logger.info(String.format("addMessageListener: %s", listener));
		final String folderName;
		if (listener instanceof OutlookMessageListener) {
			folderName = ((OutlookMessageListener) listener).getFolderName();
		} else {
			folderName = mDefaultFolder;
		}

		logger.info(String.format("folderName: %s", folderName));

		synchronized (mWrapperMap) {
			if (mWrapperMap.containsKey(folderName)) {
				mWrapperMap.get(folderName).addMesageListener(listener);
			} else {
				logger.debug("MessageListenerWrapper not found.. try to create");

				final Semaphore semaphore = new Semaphore(0);
				final MessageListenerWrapper mw = new MessageListenerWrapper();
				mw.addMesageListener(listener);
				mWrapperMap.put(folderName, mw);

				mEventSTA.put(folderName, new STA() {
					private ActiveXComponent mMapiSession;

					public void run() {
						ComThread.InitMTA();
						if (OnInit()) {
							// this call blocks in the win32 message loop
							// until quitMessagePump is called
							doMessagePump();
						}
						OnQuit();
						// uninit COM
						ComThread.Release();
					}

					public boolean OnInit() {
						try {
							mMapiSession = new ActiveXComponent(
									"Redemption.RDOSession");

							// Logon
							Dispatch.call(mMapiSession, "LogonExchangeMailbox",
									mUser, mServerName);

							Dispatch folder = Dispatch.call(mMapiSession,
									"GetFolderFromPath", folderName)
									.toDispatch();

							Dispatch items = Dispatch.get(folder, "Items")
									.toDispatch();

							new DispatchEvents(items, mw);

							return true;
						} catch (Throwable e) {
							logger.error("register DispatchEvents failed: ", e);
							return false;
						} finally {
							semaphore.release();
						}
					}

					@Override
					public void OnQuit() {
						Dispatch.call(mMapiSession, "Logoff");
						logger.info(String.format("STA %s destroyed", this));
					}

				});
				semaphore.acquireUninterruptibly();
				logger.info("STA init completed");
			}
		}
	}

	@Override
	public void removeMessageListener(MessageListener listener) {
		for (MessageListenerWrapper wrapper : mWrapperMap.values()){
			wrapper.removeMessageListener(listener);
		}

	}

	public final class MessageListenerWrapper {
		private Executor mExecutor = Executors.newCachedThreadPool();
		private List<MessageListener> mMessageListeners = new ArrayList<MessageListener>();

		private void addMesageListener(MessageListener listener) {
			mMessageListeners.add(listener);
		}

		public void removeMessageListener(MessageListener listener) {
			mMessageListeners.remove(listener);
			
		}

		public void ItemAdd(Variant[] args) {
			logger.debug("ItemAdd");
			try {
				ComThread.InitMTA();
				Dispatch rdoMail = args[0].getDispatch();
				String from = Dispatch.get(rdoMail, "SenderEmailAddress")
						.getString();
				String[] to = Dispatch.get(rdoMail, "To").getString()
						.split(";");
				String body = Dispatch.get(rdoMail, "Body").getString();
				String subject = Dispatch.get(rdoMail, "Subject").getString();

				logger.debug(String.format("Body: %s", body));

				Dispatch.call(rdoMail, "MarkRead", true);
				
				final OutlookMessage message = new OutlookMessage();
				message.setFrom(from);
				message.setContent(body);
				message.setSubject(subject);
				message.setRecipients(to);

				final MessageSessionContext context = new MessageSessionContext(
						from, OutlookMessageService.this);

				for (final MessageListener listener : mMessageListeners) {
					mExecutor.execute(new Runnable() {

						@Override
						public void run() {
							listener.onMessageReceived(context, message);
							listener.onSessionClosed(context);
						}
					});
				}
			} finally {
//				ComThread.Release();
			}
		}
	}

}
