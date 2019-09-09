package fr.maxlego08.auth.auth;

public class AuthHistorical {

	private final String date;
	private final String adress;

	/**
	 * @param date
	 * @param adress
	 */
	public AuthHistorical(String date, String adress) {
		super();
		this.date = date;
		this.adress = adress;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}

}
