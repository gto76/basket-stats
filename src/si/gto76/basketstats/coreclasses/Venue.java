package si.gto76.basketstats.coreclasses;

import si.gto76.basketstats.Conf;

public class Venue implements HasName {
	private String name;

	public Venue(String name) {
		setName(name);
	}

	/**
	 * Throws illegal argument exception if name is null, empty or only whitespaces.
	 */
	@Override
	public void setName(String name) {
		name = Util.checkNameForNullAndTrimIt(name, Conf.MAX_VENUE_NAME_LENGTH);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
