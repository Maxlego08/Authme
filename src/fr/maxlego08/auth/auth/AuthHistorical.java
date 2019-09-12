package fr.maxlego08.auth.auth;

public class AuthHistorical {

	private final String date;
	private final String address;
	private final String addressMac;

	/**
	 * @param date
	 * @param address
	 * @param addressMac
	 */
	public AuthHistorical(String date, String address, String addressMac) {
		super();
		this.date = date;
		this.address = address;
		this.addressMac = addressMac;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the addressMac
	 */
	public String getAddressMac() {
		return addressMac;
	}

}
