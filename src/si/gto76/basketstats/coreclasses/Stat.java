package si.gto76.basketstats.coreclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Stat {
	FGM("fgm"),
	FGA("fga"),
	TPM("3pm"),
	TPA("3pa"),
	PM("+/-"),
	OFF("off"),
	DEF("def"),
	REB("tot"),
	AST("ast"),
	PF("pf"),
	ST("st"),
	TO("to"),
	BS("bs"),
	PTS("pts"),
	/////
	TPF("3pf"),
	IIPM("2pm"),
	IIPF("2pf")
	;;;;;
	private static final Map<String, Stat> lookup = new HashMap<String, Stat>();
	static {
		for(Stat s : Stat.values())
			lookup.put(s.getName(), s);
	}
  
	String name;
	Stat(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;//.toUpperCase();
	}
	public static Stat get(String name) { 
		Stat sc = lookup.get(name); 
		if (sc == null)
			throw new NullPointerException("Could not find proper enum for stat: " + name);
		return sc;
	}
	public static Stat[] nonScoringValues() {
		Stat[] values = Stat.values();
		return Arrays.copyOfRange(values, 4, values.length-3);
	}
	public static Stat[] boxValues() {
		Stat[] values = Stat.values();
		return Arrays.copyOfRange(values, 0, values.length-3);
	}
}
