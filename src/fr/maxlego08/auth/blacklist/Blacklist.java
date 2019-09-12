package fr.maxlego08.auth.blacklist;

public class Blacklist {

	private final String userName;
	private final String userAddress;
	private final String userUniqueAddress;

	/**
	 * @param userName
	 * @param userAddress
	 * @param userUniqueAddress
	 */
	public Blacklist(String userName, String userAddress, String userUniqueAddress) {
		super();
		this.userName = userName;
		this.userAddress = userAddress;
		this.userUniqueAddress = userUniqueAddress;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the userAddress
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * @return the userUniqueAddress
	 */
	public String getUserUniqueAddress() {
		return userUniqueAddress;
	}

}
