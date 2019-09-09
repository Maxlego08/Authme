package fr.maxlego08.auth.mail;

import fr.maxlego08.auth.auth.Auth;

public class MailLogin {

	private final String key;
	private final Auth auth;

	/**
	 * @param key
	 * @param auth
	 */
	public MailLogin(String key, Auth auth) {
		super();
		this.key = key;
		this.auth = auth;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the auth
	 */
	public Auth getAuth() {
		return auth;
	}

}
