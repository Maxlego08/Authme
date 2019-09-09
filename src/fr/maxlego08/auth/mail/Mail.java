package fr.maxlego08.auth.mail;

public class Mail {

	private String mail;
	private String key;

	/**
	 * @param mail
	 * @param key
	 */
	public Mail(String mail, String key) {
		super();
		this.mail = mail;
		this.key = key;
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
