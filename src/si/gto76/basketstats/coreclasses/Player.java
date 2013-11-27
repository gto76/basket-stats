package si.gto76.basketstats.coreclasses;

public class Player {
	private String firstName, secondName;
	//private List<Team> teams = new ArrayList<Team>();
	
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
	
	// this should not wxist because of possible wrong usage 
	// in case one player plays on both teams in one game
	/*
	public void addToTeam(Team team) {
		teams.add(team);
	}
	 */

	@Override
	public String toString() {
		return firstName + " " + secondName;
	}
	
}
