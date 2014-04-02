package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Stat {
	FGM("fgm", "FGM", "", ""),
	FGA("fga", "FGA", "", ""),
	TPM("3pm", "3PM", "made3p", "unmade3p"),
	TPA("3pa", "3PA", "", ""),
	/////
	PM("+/-", "+/-", "", ""),
	OFF("off", "Off", "madeOff", "unMadeOff"),
	DEF("def", "Def", "madeDef", "unmadeDef"),
	REB("tot", "Tot", "", ""),
	AST("ast", "Ast", "madeAst", "unmadeAst"),
	PF("pf", "Pf", "madePf", "unmadePf"),
	ST("st", "St", "madeSt", "unmadeSt"),
	TO("to", "To", "madeTo", "unmadeTo"),
	BS("bs", "Bs", "madeBs", "unmadeBs"),
	PTS("pts", "Pts", "", ""),
	/////
	TPF("3pf", "3PF", "missed3p", "unMissed3p"),
	IIPM("2pm", "2PM", "made2p", "unMade2p"),
	IIPF("2pf", "2PF", "missed2p", "unMissed2p")
	;;;;;
	private static final Map<String, Stat> lookup = new HashMap<String, Stat>();
	static {
		for(Stat s : Stat.values())
			lookup.put(s.getName(), s);
	}
	//////////////////////////////
	private final String name;
	private final String buttonName;
	private final String triggerMethodName;
	private final String undoMethodName;
	//////////////////////////////
	
	Stat(String name, String buttonName, String triggerMethodName, String undoMethodName) {
		this.name = name;
		this.buttonName = buttonName;
		this.triggerMethodName = triggerMethodName;
		this.undoMethodName = undoMethodName;
	}
	/////////////////////////////////////
	public String getName() {
		return name;
	}
	public String getButtonName() {
		return buttonName;
	}
	public String getActionMethodName() {
		return triggerMethodName;
	}
	public String getUndoMethodName() {
		return undoMethodName;
	}
	public boolean isScoringValue() {
		return Arrays.asList(scoringValuesAndPoints()).contains(this);
	}
	public boolean isInputValueOrPlusMinus() {
		return Arrays.asList(inputValuesAndPlusMinus()).contains(this);
	}
	/////////////////////////////////////
	public static Stat[] nonScoringValuesAndPoints() {
		Stat[] values = Stat.values();
		return Arrays.copyOfRange(values, 4, values.length-3);
	}
	public static Stat[] boxScoreValues() {
		Stat[] values = Stat.values();
		return Arrays.copyOfRange(values, 0, values.length-3);
	}
	public static Stat[] inputValues() {
		Stat[] inputValues = {IIPM, IIPF, TPM, TPF, OFF, DEF, AST, PF, ST, TO, BS};
		return inputValues;
	}
	public static Stat[] inputValuesAndPlusMinus() {
		Stat[] inputValues = {IIPM, IIPF, TPM, TPF, PM, OFF, DEF, AST, PF, ST, TO, BS};
		return inputValues;
	}
	public static Stat[] nonScoringInputValuesAndPlusMinus() {
		Stat[] inputValues = {PM, OFF, DEF, AST, PF, ST, TO, BS};
		return inputValues;
	}
	public static Stat[] scoringValuesAndPoints() {
		Stat[] scoringValues = {FGM, FGA, TPM, TPA, TPF, IIPM, IIPF, PTS};
		return scoringValues;
	}
	/////////////////////////////////////
	public static Stat get(String name) { 
		Stat sc = lookup.get(name); 
		if (sc == null)
			throw new NullPointerException("Could not find proper enum for stat: " + name);
		return sc;
	}
}
