
package pt.aubay.cv.models;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class SSLEmail {

	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for SSL: 465
	 */
	public static void SSl(String toEmail, String emailBody) {
		final String fromEmail = "pt.aubay@gmail.com"; //requires valid gmail id
		final String password = "aubay123"; // correct password for gmail id
		//final String toEmail = "russoricardo1@gmail.com"; // can be any email id 

		System.out.println("SSLEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.transport.protocol", "smtp");

		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		System.out.println("Session created");
		new Thread(() -> {
			EmailUtil.sendEmail(session, toEmail,"Plataforma de Gestao de curriculos", emailBody);
		}).start();
	}
}
