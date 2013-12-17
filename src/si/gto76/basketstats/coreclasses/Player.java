package si.gto76.basketstats.coreclasses;

import si.gto76.basketstats.Conf;

public class Player implements HasName {
	private String name;

	public Player(String name) {
		setName(name);
	}
	
	@Override
	public void setName(String name) {
		name = name.trim();
		if (name.length() >= Conf.PLAYER_NAME_WIDTH) {	
			name = name.substring(0, Conf.PLAYER_NAME_WIDTH-1);
		}
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
