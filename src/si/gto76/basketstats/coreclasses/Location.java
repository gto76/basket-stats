package si.gto76.basketstats.coreclasses;

public class Location implements HasName {
	private String name;

	public Location(String name) {
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

	@Override
	public String toString() {
		return name;
	}
	
}
