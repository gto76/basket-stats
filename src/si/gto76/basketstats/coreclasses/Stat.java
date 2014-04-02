package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Stat {
	FGM("FGM", "Field goal made"),
	FGA("FGA", "Field goal attempted"),
	TPM("3PM", "Three pointer made"),
	TPA("3PA", "Three pointer attempted"),
	/////
	PM("+/-", "Scoring difference"),
	OFF("Off", "Offensive rebound"),
	DEF("Def", "Deffensive rebound"),
	REB("Tot", "Rebound"),
	AST("Ast", "Assist"),
	PF("Pf", "Personal foul"),
	ST("St", "Steal"),
	TO("To", "Turnover"),
	BS("Bs", "Blocked shot"),
	PTS("Pts", "Points"),
	/////
	TPF("3PF", "Three pointer missed"),
	IIPM("2PM", "Two pointer made"),
	IIPF("2PF", "Two pointer missed")
	;;;;;
	/////////////////////////////////////
	private final String name;
	private final String explanation;
	/////////////////////////////////////
	
	Stat(String name, String explanation) {
		this.name = name;
		this.explanation = explanation;
	}
	
	/////////////////////////////////////
	
	public String getName() {
		return name;
	}
	public String getExplanation() {
		return explanation;
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

}
