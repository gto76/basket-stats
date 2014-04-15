package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public enum Stat {
	/////
	FGM("FGM", "Field goal made", false, true, true),
	FGA("FGA", "Field goal attempted", false, true, true),
	TPM("3PM", "Three pointer made", true, true, true),
	TPA("3PA", "Three pointer attempted", false, true, true),
	/////
	PM("+/-", "Scoring difference", true, true, false),
	/////
	OFF("Off", "Offensive rebound", true, true, false),
	DEF("Def", "Deffensive rebound", true, true, false),
	REB("Tot", "Rebound", true, true, false),
	/////
	AST("Ast", "Assist", true, true, false),
	PF("Pf", "Personal foul", true, true, false),
	ST("St", "Steal", true, true, false),
	TO("To", "Turnover", true, true, false),
	BS("Bs", "Blocked shot", true, true, false),
	/////
	PTS("Pts", "Points", false, true, false),
	/////
	TPF("3PF", "Three pointer missed", true, false, true),
	IIPM("2PM", "Two pointer made", true, false, true),
	IIPF("2PF", "Two pointer missed", true, false, true)
	;;;;;
	/////////////////////////////////////
	private final String name;
	private final String explanation;
	private final boolean inputValue;
	private final boolean outputValue;
	private final boolean scoringValue;
	/////////////////////////////////////
	
	Stat(String name, String explanation, boolean inputValue, boolean outputValue, boolean scoringValue) {
		this.name = name;
		this.explanation = explanation;
		this.inputValue = inputValue;
		this.outputValue = outputValue;
		this.scoringValue = scoringValue;
	}
	
	/////////////////////////////////////

	public static Stat[] actionSet = {IIPM, IIPF, TPM, TPF, OFF, DEF, REB, AST, PF, ST, TO, BS};
	public static Stat[] playerStatsValues = {PM, OFF, DEF, REB, AST, PF, ST, TO, BS};
	public static Stat[] nbaRecordingStats = {IIPM, IIPF, TPM, TPF, PM, OFF, DEF, AST, PF, ST, TO, BS};
	public static Stat[] scoringValues = {FGM, FGA, TPM, TPA, TPF, IIPM, IIPF};

	/////////////////////////////////////
	
	public String getName() {
		return name;
	}
	public String getExplanation() {
		return explanation;
	}
	
	public boolean isInputValue() {
		return inputValue;
	}
	public boolean isOutputValue() {
		return outputValue;
	}
	public boolean isScoringValue() {
		return scoringValue;
	}
	public boolean isScoringValueOrPoints() {
		return scoringValue || (this.equals(PTS));
		// OLD return Arrays.asList(scoringValuesAndPoints).contains(this);
	}
	
	public static Stat getByName(String name) {
		Stat[] stats = Stat.values();
	    for (Stat stat : stats) {
	    	if (stat.name.toUpperCase().equals(name.toUpperCase())) {
	    		return stat;
	    	}
	    }
	    return null;
	}
	
	/////////////////////////////////////
	
	public static Set<Stat> getInputStatsFromOutput(Set<Stat> outputStatsIn) {
		Set<Stat> outputStats = new HashSet<Stat>(outputStatsIn);
		Set<Stat> inputStats = new HashSet<Stat>();
		// SCORING
		// TPA -> IIPM, TPM, IIPF, TPF
		// TPM -> IIPM, TPM,
		// FGA -> IIPM, IIPF
		// else -> IIPM
		inputStats.add(Stat.IIPM);
		if (outputStats.contains(Stat.TPM)) {
			inputStats.add(Stat.TPM);
		}
		if (outputStats.contains(Stat.FGA)) {
			inputStats.add(Stat.IIPF);
		}
		if (outputStats.contains(Stat.TPA)) {
			inputStats.add(Stat.TPF);
		}
		// OFF, DEF, REB -> OFF, DEF
		// OFF -> OFF
		// DEF -> DEF
		// REB -> REB
		if (outputStats.contains(Stat.REB) && (outputStats.contains(Stat.OFF) || outputStats.contains(Stat.DEF))) {
			outputStats.remove(Stat.REB);
		}
		for (Stat outputStat : outputStats) {
			if (!outputStat.isScoringValue() && outputStat.isInputValue()) {
				inputStats.add(outputStat);
			}
		}
		
		return inputStats;
	}
	
	public static Stat[] getOutputStatsFromInput(Set<Stat> inputStats) {
		List<Stat> outputStats = new ArrayList<Stat>();
		// SCORING
		// if TPF -> FGM, FGA, TPM, TPA
		// else if TPM -> FGM, TPM
		// else if IIPF -> FGM, FGA
		// else -> FGM
		outputStats.add(Stat.FGM);
		if (inputStats.contains(Stat.IIPF)) {
			outputStats.add(Stat.FGA);
		}
		if (inputStats.contains(Stat.TPM)) {
			outputStats.add(Stat.TPM);
		}
		if (inputStats.contains(Stat.TPF)) {
			outputStats.add(Stat.TPA);
		}
		// NON SCORING
		Stat[] nonScoringStats = getNonScoringOutputStatsFromInput(inputStats);
		outputStats.addAll(Arrays.asList(nonScoringStats));
		
		return (Stat[]) outputStats.toArray(new Stat[outputStats.size()]);
	}
	
	public static Stat[] getNonScoringOutputStatsFromInput(Set<Stat> inputStats) {
		Set<Stat> outputStats = new LinkedHashSet<Stat>();
		// if OFF, DEF  -> OFF, DEF, REB
		// else if OFF -> OFF
		// else if DEF -> DEF
		// else if REB -> REB
		boolean offFlag = false, defFlag = false;
		for (Stat stat : Stat.values()) {
			if (offFlag == true && defFlag == true) {
				outputStats.add(Stat.REB);
				offFlag = false;
				defFlag = false;
			}
			if (!stat.isScoringValue() && stat.isOutputValue() && inputStats.contains(stat)) {
				outputStats.add(stat);
				if (stat == Stat.OFF) {
					offFlag = true;
				}
				if (stat == Stat.DEF) {
					defFlag = true;
				}
			}
		}
		// PTS
		outputStats.add(Stat.PTS);
		return (Stat[]) outputStats.toArray(new Stat[outputStats.size()]);
	}

}
