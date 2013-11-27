package si.gto76.basketstats.coreclasses;

import java.util.Arrays;

public enum StatCats {
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
	PTS("pts")
	;;;;;
	String name;
	StatCats(String name) {
		this.name = name;
	}
	public String getName() {
		return name.toUpperCase();
	}
	public static StatCats[] nonScoringValues() {
		StatCats[] values = StatCats.values();
		return Arrays.copyOfRange(values, 4, values.length);
	}
}
