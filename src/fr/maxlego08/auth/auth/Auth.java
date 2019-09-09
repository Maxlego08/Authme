package fr.maxlego08.auth.auth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Auth {

	private final String name;
	private String password;
	private String twoFactorAuthentication;
	private String mail;
	private boolean logMail = false;
	private boolean loginMail = false;
	private List<AuthHistorical> historical = new ArrayList<AuthHistorical>();
	private transient boolean isLogin = false;
	private transient boolean isMailLogin = false;
	private transient Location location;

	/**
	 * @param name
	 */
	public Auth(String name) {
		super();
		this.name = name;
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
	 * @return the twoFactorAuthentication
	 */
	public String getTwoFactorAuthentication() {
		return twoFactorAuthentication;
	}

	/**
	 * @param twoFactorAuthentication
	 *            the twoFactorAuthentication to set
	 */
	public void setTwoFactorAuthentication(String twoFactorAuthentication) {
		this.twoFactorAuthentication = twoFactorAuthentication;
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
	 * @param isMailLogin the isMailLogin to set
	 */
	public void setMailLogin(boolean isMailLogin) {
		this.isMailLogin = isMailLogin;
	}

	
	
}
