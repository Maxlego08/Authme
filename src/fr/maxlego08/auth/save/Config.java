package fr.maxlego08.auth.save;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import fr.maxlego08.auth.zcore.utils.storage.Persist;
import fr.maxlego08.auth.zcore.utils.storage.Saver;

public class Config implements Saver {

	public static int packetId = 110;
	public static List<String> blacklistUsers = Arrays.asList("xFireman");
	public static byte[] salt = null;

	public static String spawnLocation = null;

	public static int maxHistorical = 10;
	public static int maxUserPerAdress = 1;
	public static int maxOnlineUserPerAdress = 2;
	public static boolean useMail = false;
	public static boolean sendMailWhenAdressIsSame = false;

	public static List<String> allowDomaine = Arrays.asList("sfr.fr", "gmail.com");

	public static String SMTP_SERVER = "127.0.0.1";
	public static String USERNAME = "";
	public static String PASSWORD = "";

	public static String EMAIL_CONTENT = "<div style=\"background-color: #282828; text-align: center\"><h1 style=\"color: red\">%code%</h1></div>";
	public static String EMAIL_CONTENT_LOG = "<div style=\"background-color: #282828; text-align: center\"><h3>Pseudo: %name%</h3><br><h3>Heure: %hour%</h3><br><h3>Adresse ip: %ip%</h3></div>";
	public static String EMAIL_CONTENT_LOGIN = "<div style=\"background-color: #282828; text-align: center\"><h1 style=\"color: red\">%code%</h1></div>";
	public static String EMAIL_CONTENT_UNREGISTER = "<div style=\"background-color: #282828; text-align: center\"><h1 style=\"color: #28F8AB\">%code%</h1></div>";
	public static String EMAIL_SUBJECT = "Oldfight - Verification du mail";

	public static String EMAIL_FROM = "noanswer@oldfight.fr";

	public static transient Config i = new Config();

	@Override
	public void save(Persist persist) {
		persist.save(i);
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Config.class, getClass().getSimpleName().toLowerCase());
		if (salt == null)
			try {
				salt = getSalt();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

}
