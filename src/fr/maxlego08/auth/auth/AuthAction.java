package fr.maxlego08.auth.auth;

public enum AuthAction {

	RECEIVE_LOGIN_PASSWORD(0),
	RECEIVE_REGISTER_PASSWORD(1),
	SEND_REGISTER(2),
	SEND_LOGIN(3),
	REGISTER_SUCCESS(4),
	REGISTER_ERROR(5),
	LOGIN_SUCCESS(6),
	LOGIN_ERROR(7),
	SEND_LOGIN_CONFIRM(8),
	RECEIVE_LOGIN_CONFIRM(9),
	CONFIRM_SUCCESS(10),
	CONFIRM_ERROR(11),

	;

	private final int id;

	/**
	 * @param id
	 */
	private AuthAction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static AuthAction get(int id) {
		for (AuthAction action : values()) {
			if (action.getId() == id)
				return action;
		}
		return null;
	}

}
