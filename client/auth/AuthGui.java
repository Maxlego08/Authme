package fr.oldfight.auth;

public enum AuthGui {

	LOGIN("Connexion", AuthAction.RECEIVE_LOGIN_PASSWORD),

	REGISTER("Inscription", AuthAction.RECEIVE_REGISTER_PASSWORD),

	CONFIRM("Confirmation", AuthAction.RECEIVE_LOGIN_CONFIRM, 10),

	UNREGISTER("Suppression du compte", AuthAction.RECEIVE_UNREGISTER),

	UNREGISTER_CONFIRM("Confirmation de suppression", AuthAction.RECEIVE_UNREGISTER_CONFIRM, 10)

	;

	private final String name;
	private final AuthAction action;
	private final int lenght;

	/**
	 * @param name
	 */
	private AuthGui(String name, AuthAction action) {
		this.name = name;
		this.action = action;
		this.lenght = 8;
	}

	/**
	 * @param name
	 * @param lenght
	 */
	private AuthGui(String name, AuthAction action, int lenght) {
		this.name = name;
		this.action = action;
		this.lenght = lenght;
	}

	/**
	 * @return the lenght
	 */
	public int getLenght() {
		return lenght;
	}

	public String getName() {
		return name;
	}

	public AuthAction getAction() {
		return action;
	}

}
