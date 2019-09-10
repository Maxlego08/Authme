package fr.oldfight.auth;

public enum AuthAction {

	RECEIVE_LOGIN_PASSWORD(0), RECEIVE_REGISTER_PASSWORD(1),

	SEND_REGISTER(2, AuthGui.REGISTER), SEND_LOGIN(3, AuthGui.LOGIN),

	REGISTER_SUCCESS(4), REGISTER_ERROR(5),

	LOGIN_SUCCESS(6), LOGIN_ERROR(7),

	SEND_LOGIN_CONFIRM(8, AuthGui.CONFIRM), RECEIVE_LOGIN_CONFIRM(9),

	CONFIRM_SUCCESS(10), CONFIRM_ERROR(11),

	SEND_UNREGISTER(12, AuthGui.UNREGISTER), RECEIVE_UNREGISTER(13),

	SEND_UNREGISTER_CONFIRM(14, AuthGui.UNREGISTER_CONFIRM), RECEIVE_UNREGISTER_CONFIRM(15),

	;

	private final int id;
	private AuthGui gui;

	/**
	 * @param id
	 */
	private AuthAction(int id) {
		this.id = id;
	}

	/**
	 * @param id
	 * @param gui
	 */
	private AuthAction(int id, AuthGui gui) {
		this.id = id;
		this.gui = gui;
	}

	public AuthGui getGui() {
		return gui;
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
