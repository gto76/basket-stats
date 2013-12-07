package si.gto76.basketstats.coreclasses;

public class Player {
	private String firstName, secondName;

	public Player(String firstName, String secondName) {
		this.firstName = firstName;
		this.secondName = secondName;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public String getShortName() {
		return firstName.charAt(0) + ". " + secondName;
	}
	public String getFullName() {
		return firstName + " " + secondName;
	}

	@Override
	public String toString() {
		return getFullName();
	}

}
