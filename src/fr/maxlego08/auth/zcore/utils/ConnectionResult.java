package fr.maxlego08.auth.zcore.utils;

public enum ConnectionResult {

	INVALID_NAME("�cUne erreur est survenu lors du chargement de votre pseudo !"),
	NUMBER_OF_USERS_REACHED("�cTrop de joueur avec la m�me adresse son connect� !"),
	BLACKLIST("�cVous �tes blacklist du serveur !"),
	CONNECT(""),
	
	;

	private final String reason;

	/**
	 * @param reason
	 */
	private ConnectionResult(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

}
