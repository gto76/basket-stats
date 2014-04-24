package si.gto76.basketstats.coreclasses;

public class Venue implements HasName {
	private static final int MAX_NAME_LENGTH = 100;
	private String name;

	public Venue(String name) {
		setName(name);
	}

	@Override
	public void setName(String name) {
		name = name.trim();
		if (name.length() >= MAX_NAME_LENGTH) {	
			name = name.substring(0, MAX_NAME_LENGTH-1);
		}
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
