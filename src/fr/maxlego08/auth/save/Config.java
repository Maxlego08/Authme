package fr.maxlego08.auth.save;

import java.util.Arrays;
import java.util.List;

import fr.maxlego08.auth.zcore.utils.storage.Persist;
import fr.maxlego08.auth.zcore.utils.storage.Saver;

public class Config implements Saver {

	public static int packetId = 110;
	public static List<String> blacklistUsers = Arrays.asList("xfireman");

	public static transient List<String> whitelistUsers = Arrays.asList("Maxlego08", "Keirowz", "Quiibz");
	
	public static String spawnLocation = null;

	public static int maxHistorical = 10;
	public static int maxUserPerAdress = 1;
	public static int maxOnlineUserPerAdress = 2;
	public static boolean useMail = false;
	public static boolean sendMailWhenAdressIsSame = false;

	public static List<String> allowDomaine = Arrays.asList("sfr.fr", "gmail.com");

	public static String SMTP_SERVER = "localhost";
	public static int SMTP_PORT = 25;
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
	}

	public static String getBlacklist() {
		String string = "";
		for (int a = 0; a < blacklistUsers.size(); a++)
			string += "§2" + blacklistUsers.get(a) + (a < blacklistUsers.size() - 1 ? "§7, " : "");
		return string;
	}
	
	public static boolean isBlacklist(String playerName){
		return blacklistUsers.stream().filter(name -> playerName.equalsIgnoreCase(name)).findAny().isPresent();
	}
	
	public static boolean isWhitelist(String playerName){
		return whitelistUsers.stream().filter(name -> playerName.equalsIgnoreCase(name)).findAny().isPresent();
	}

}
