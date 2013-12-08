package si.gto76.basketstats.coreclasses;

public class Player implements PlayerOrTeam {
	private String name;

	public Player(String name) {
		this.name = name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
/*
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
*/
	@Override
	public String toString() {
		return getName();
	}

}
