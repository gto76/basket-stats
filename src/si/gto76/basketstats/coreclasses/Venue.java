package si.gto76.basketstats.coreclasses;

public class Venue implements HasName {
	private String name;

	public Venue(String name) {
		setName(name);
	}

	@Override
	public void setName(String name) {
		name = name.trim();
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
