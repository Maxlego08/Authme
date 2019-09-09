package fr.oldfight.auth;

public enum AuthGui {

	LOGIN("Connection"), REGISTER("Inscription"), CONFIRM("Confirmation", 10),

	;

	private final String name;
	private final int lenght;

	/**
	 * @param name
	 */
	private AuthGui(String name) {
		this.name = name;
		this.lenght = 8;
	}

	/**
	 * @param name
	 * @param lenght
	 */
	private AuthGui(String name, int lenght) {
		this.name = name;
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

}
