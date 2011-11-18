package idv.kaomk.eicrhios.message.outlook;

import org.junit.Test;

import com.jacob.com.ComThread;

public class OutlookMessageServiceTest {
	@Test
	public void testSendTextMail() {
		try {
//
			ComThread.InitSTA();
//			OutlookMessageService service = new OutlookMessageService(
//					"echo.kao", "S0021EMAILBE04");
//			service.sendMessage(new TextMessage("hello!",
//					"echo.kao@chinatrust.com.tw"));
		} finally {
			ComThread.Release();
		}

	}

	@Test
	public void testSendAttachments() {
		try {

			ComThread.InitSTA();
//			OutlookMessageService service = new OutlookMessageService(
//					"echo.kao", "S0021EMAILBE04");
//			OutlookMessage message = new OutlookMessage("hello2~\n",
//					"echo.kao@chinatrust.com.tw");
//			message.addAttachment(new File("d:/progIdVsClsidDB.properties"));
//			message.addAttachment(new File("d:/trace.log"));
//			service.sendMessage(message);
		} finally {
			ComThread.Release();
		}

	}
}
