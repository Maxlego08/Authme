package fr.maxlego08.auth.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.maxlego08.auth.save.Config;

public class Auth {

	private final String name;
	private final byte[] salt;
	private String password;
	private String mail;
	private boolean logMail = false;
	private boolean loginMail = false;
	private List<AuthHistorical> historical = new ArrayList<AuthHistorical>();
	private transient boolean isLogin = false;
	private transient boolean isMailLogin = false;
	private transient Location location;

	/**
	 * @param name
	 * @param salt
	 */
	public Auth(String name, byte[] salt) {
		super();
		this.name = name;
		this.salt = salt;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the historical
	 */
	public List<AuthHistorical> getHistorical() {
		return historical;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param password
	 * @return
	 */
	public boolean same(String password) {
		return password.equals(this.password);
	}

	/**
	 * 
	 * @param authHistorical
	 */
	public void add(AuthHistorical authHistorical) {
		this.historical.add(authHistorical);
		updateHistorical();
	}

	/**
	 * @return the isLogin
	 */
	public boolean isLogin() {
		return isLogin;
	}

	/**
	 * @param isLogin
	 *            the isLogin to set
	 */
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	/**
	 * @param historical
	 *            the historical to set
	 */
	public void setHistorical(List<AuthHistorical> historical) {
		this.historical = historical;
	}

	/**
	 * @return the logMail
	 */
	public boolean isLogMail() {
		return logMail;
	}

	/**
	 * @param logMail
	 *            the logMail to set
	 */
	public void setLogMail(boolean logMail) {
		this.logMail = logMail;
	}

	/**
	 * @return the loginMail
	 */
	public boolean isLoginMail() {
		return loginMail;
	}

	/**
	 * @param loginMail
	 *            the loginMail to set
	 */
	public void setLoginMail(boolean loginMail) {
		this.loginMail = loginMail;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the isMailLogin
	 */
	public boolean isMailLogin() {
		return isMailLogin && isLoginMail();
	}

	/**
	 * @param isMailLogin
	 *            the isMailLogin to set
	 */
	public void setMailLogin(boolean isMailLogin) {
		this.isMailLogin = isMailLogin;
	}

	private void updateHistorical() {
		if (historical.size() > Config.maxHistorical)
			historical = historical.stream().skip(historical.size() - Config.maxHistorical)
					.collect(Collectors.toList());
	}

	public boolean sameAdress(String address) {
		if (historical.size() == 0)
			return false;
		return historical.get(historical.size() - 1).getAddress().equals(address);
	}

	/**
	 * @return AuthHistorical
	 */
	public AuthHistorical getLast() {
		if (historical.size() == 0)
			return null;
		return historical.get(historical.size() - 1);
	}

	/**
	 * @return last adress
	 */
	public String getLastAddress() {
		if (historical.size() == 0)
			return "127.0.0.1";
		return historical.get(historical.size() - 1).getAddress();
	}
	
	public String getLastUniqueAddress() {
		if (historical.size() == 0)
			return "00-00-00-00-00-00";
		return historical.get(historical.size() - 1).getAddressMac();
	}

	/**
	 * @return
	 */
	public boolean isOnline() {
		return Bukkit.getPlayer(name) != null;
	}

	public byte[] getSalt() {
		return salt;
	}
	
}
