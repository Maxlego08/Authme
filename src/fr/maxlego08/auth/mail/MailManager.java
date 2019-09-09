package fr.maxlego08.auth.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.bukkit.entity.Player;

import com.sun.mail.smtp.SMTPTransport;

import fr.maxlego08.auth.auth.Auth;
import fr.maxlego08.auth.auth.AuthManager;
import fr.maxlego08.auth.save.Config;
import fr.maxlego08.auth.zcore.ZPlugin;
import fr.maxlego08.auth.zcore.utils.storage.Persist;
import fr.maxlego08.auth.zcore.utils.storage.Saver;

public class MailManager implements Saver {

	private static Map<String, Mail> verifEmail = new HashMap<String, Mail>();
	private static transient Map<String, MailLogin> verifEmailLogin = new HashMap<String, MailLogin>();

	/**
	 * @param player
	 * @return true if player verif mail
	 */
	public boolean mailAlreadySend(Player player) {
		return verifEmail.containsKey(player.getName());
	}

	/**
	 * @param player
	 * @param mail
	 */
	public void sendVerificationEmail(Player player, String mail) {
		String key = generateRandomKey();
		Timer timer = new Timer();
		verifEmail.put(player.getName(), new Mail(mail, key));
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (sendMail(Config.EMAIL_SUBJECT, Config.EMAIL_CONTENT.replace("%code%", key), mail))
					player.sendMessage(
							ZPlugin.z().getPrefix() + " §aMail envoyé avec succès à §2" + getBlur(mail) + "§a !");
				else
					player.sendMessage(ZPlugin.z().getPrefix()
							+ " §cUne erreur est survenue lors de l'envoie du mail à §6" + getBlur(mail) + "§c !");
			}
		}, 100);
	}

	/**
	 * @param auth
	 * @param player
	 */
	public void sendLogEmail(Auth auth, Player player) {
		if (auth.getMail() == null)
			return;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String htmlContent = Config.EMAIL_CONTENT_LOG.replace("%name%", player.getName())
						.replace("%hour%", new Date().toString()).replace("%ip%", player.getAddress().getHostName());
				sendMail("Oldfight - Connection", htmlContent, auth.getMail());
			}
		}, 100);
	}

	/**
	 * @param auth
	 * @param player
	 */
	public void sendLoginEmail(Auth auth, Player player) {
		if (auth.getMail() == null)
			return;
		Timer timer = new Timer();
		String key = generateRandomKey();
		verifEmailLogin.put(player.getName(), new MailLogin(key, auth));
		auth.setMailLogin(true);
		player.sendMessage(ZPlugin.z().getPrefix() + " §aEnvoie du mail...");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String htmlContent = Config.EMAIL_CONTENT_LOGIN.replace("%code%", key);
				if (sendMail("Oldfight - Connection", htmlContent, auth.getMail()))
					player.sendMessage(ZPlugin.z().getPrefix() + " §aMail envoyé avec succès à §2"
							+ getBlur(auth.getMail()) + "§a !");
				else
					player.sendMessage(
							ZPlugin.z().getPrefix() + " §cUne erreur est survenue lors de l'envoie du mail à §6"
									+ getBlur(auth.getMail()) + "§c !");
			}
		}, 100);
	}

	/**
	 * @param mail
	 * @return current string in blur
	 */
	private String getBlur(String mail) {
		String string = mail.substring(0, 3);
		for (String content : mail.substring(4, mail.length()).split(""))
			if (content.equals("@") || content.equals("."))
				string += content;
			else
				string += "*";
		return string;
	}

	/**
	 * @param player
	 * @param code
	 */
	public void verifCode(Player player, String code) {
		if (!verifEmail.containsKey(player.getName()))
			return;
		Mail mail = verifEmail.get(player.getName());
		if (mail.getKey().equals(code)) {
			player.sendMessage(ZPlugin.z().getPrefix() + " §aVous venez de vérifier votre mail avec succès !");
			Auth auth = AuthManager.i.getUser(player.getName());
			auth.setMail(mail.getMail());
			verifEmail.remove(player.getName());
		} else
			player.sendMessage(ZPlugin.z().getPrefix() + " §cLe code §6" + code + " §cest erroné !");
	}

	/**
	 * @return random String
	 */
	private String generateRandomKey() {
		int leftLimit = 97;
		int rightLimit = 122;
		int targetStringLength = 10;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append(random.nextBoolean() ? (char) randomLimitedInt
					: String.valueOf((char) randomLimitedInt).toUpperCase());
		}
		return buffer.toString();
	}

	/**
	 * @param code
	 * @return true if code is correct
	 */
	public boolean verifCodeConfirm(String name, String code) {
		if (!verifEmailLogin.containsKey(name))
			return true;
		boolean isCorrect = verifEmailLogin.get(name).getKey().equals(code);
		if (isCorrect) 
			verifEmailLogin.remove(name);
		return isCorrect;

	}

	/**
	 * @param subject
	 * @param htmlContent
	 * @param email
	 * @return true if mail is send
	 */
	public boolean sendMail(String subject, String htmlContent, String email) {
		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", Config.SMTP_SERVER);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "25");

		Session session = Session.getInstance(prop, null);
		Message msg = new MimeMessage(session);

		try {
			msg.setFrom(new InternetAddress(Config.EMAIL_FROM));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
			msg.setSubject(subject);
			msg.setDataHandler(new DataHandler(new HTMLDataSource(htmlContent)));
			SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			t.connect(Config.SMTP_SERVER, Config.USERNAME, Config.PASSWORD);
			t.sendMessage(msg, msg.getAllRecipients());
			// System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (MessagingException e) {
			return false;
		}
		return true;
	}

	public static transient MailManager i = new MailManager();

	@Override
	public void save(Persist persist) {
		persist.save(i, "mail");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, MailManager.class, "mail");
	}

	static class HTMLDataSource implements DataSource {

		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("html message is null!");
			return new ByteArrayInputStream(html.getBytes());
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		@Override
		public String getContentType() {
			return "text/html";
		}

		@Override
		public String getName() {
			return "HTMLDataSource";
		}
	}

}
