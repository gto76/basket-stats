package si.gto76.basketstats.coreclasses;

import si.gto76.basketstats.Conf;

public class Player implements HasName {
	private String name;

	public Player(String name) {
		setName(name);
	}
	
	/**
	 * Throws illegal argument exception if name is null, empty or only whitespaces.
	 */
	@Override
	public void setName(String name) {
		name = Util.checkNameForNullAndTrimIt(name, Conf.PLAYER_NAME_WIDTH);
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
