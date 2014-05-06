package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Enum with all statistical categories and their characteristics.
 * Two important characteristics are isRecordable and isDisplayable.
 * Basicly the first one tells wether the stat is meant to be inputed with gui,
 * and the other wether it's meant to be displayed in a box score.
 * Also two mapping functions are probided that transform a set of Recording Stats 
 * to a set of Displayable Stats and other way around.
 */
public enum Stat {
	/////
	FGM("FGM", "Field goal made", false, true, true),
	FGA("FGA", "Field goal attempted", false, true, true),
	//
	IIPM("2PM", "Two pointer made", true, false, true),
	IIPF("2PF", "Two pointer missed", true, false, true),
	//
	TPM("3PM", "Three pointer made", true, true, true),
	TPF("3PF", "Three pointer missed", true, false, true),
	TPA("3PA", "Three pointer attempted", false, true, true),
	//
	FTM("FTM", "Free throw made", true, true, true),
	FTF("FTF", "Free throw missed", true, false, true),
	FTA("FTA", "Free throw attempted", false, true, true),
	/////
	PM("+/-", "Scoring difference", true, true, false),
	/////
	OFF("Off", "Offensive rebound", true, true, false),
	DEF("Def", "Deffensive rebound", true, true, false),
	REB("Reb", "Rebound", true, true, false),
	TOT("Tot", "Total rebounds", false, true, false),
	/////
	AST("Ast", "Assist", true, true, false),
	PF("Pf", "Personal foul", true, true, false),
	ST("St", "Steal", true, true, false),
	TO("To", "Turnover", true, true, false),
	BS("Bs", "Blocked shot", true, true, false),
	BA("Ba", "Blocked attempt", true, true, false),
	/////
	PTS("Pts", "Points", false, true, false),
	/////
	;;;;;
	/////////////////////////////////////
	private final String name;
	private final String explanation;
	private final boolean isRecordable;
	private final boolean isDisplayable;
	private final boolean isScoring;
	/////////////////////////////////////
	
	Stat(String name, String explanation, boolean isRecordable, boolean isDisplayable, boolean isScoring) {
		this.name = name;
		this.explanation = explanation;
		this.isRecordable = isRecordable;
		this.isDisplayable = isDisplayable;
		this.isScoring = isScoring;
	}
	
	/////////////////////////////////////
	
	public String getName() {
		return name;
	}
	public String getExplanation() {
		return explanation;
	}
	
	public boolean isRecordable() {
		return isRecordable;
	}
	public boolean isDisplayable() {
		return isDisplayable;
	}
	public boolean isScoring() {
		return isScoring;
	}
	public boolean isScoringOrPoints() {
		return isScoring || (this.equals(PTS));
	}

	/////////////////////////////////////

	public static final Stat[] actionStats = {IIPM, IIPF, TPM, TPF, FTM, FTF, OFF, DEF, REB, AST, PF, ST, TO, BS, BA};
	public static final Stat[] playersStatRecorderStats = {PM, OFF, DEF, REB, AST, PF, ST, TO, BS, BA};
	public static final Stat[] nbaRecordingStats = {IIPM, IIPF, TPM, TPF, FTM, FTF, PM, OFF, DEF, AST, PF, ST, TO, BS, BA};
	public static final Stat[] scoreChangingStats = {IIPM, TPM, FTM};
	
	public static Stat[] recordableStats; // = {IIPM, IIPF, TPM, TPF, FTM, FTF, PM, OFF, DEF, REB, AST, PF, ST, TO, BS, BA};
	static {
		List<Stat> recordableStatsList = new ArrayList<Stat>();
		for (Stat value : values()) {
			if (value.isRecordable()) {
				recordableStatsList.add(value);
			}
		}
		recordableStats = recordableStatsList.toArray(new Stat[0]);
	}

	public static final Stat[] scoringStats; // = {FGM, FGA, TPM, TPA, TPF, IIPM, IIPF, FTM, FTA, FTF};
	static {
		List<Stat> scoringStatsList = new ArrayList<Stat>();
		for (Stat value : values()) {
			if (value.isScoring()) {
				scoringStatsList.add(value);
			}
		}
		scoringStats = scoringStatsList.toArray(new Stat[0]);
	}

	/*
	 * ################ ################ ################
	 * STATIC FUNCTIONS STATIC FUNCTIONS STATIC FUNCTIONS
	 * ################ ################ ################
	 */
	
	public static Stat getByNameOrNull(String name) {
		Stat[] stats = Stat.values();
	    for (Stat stat : stats) {
	    	if (stat.name.toUpperCase().equals(name.toUpperCase())) {
	    		return stat;
	    	}
	    }
	    return null;
	}
	
	//////////////////////////////////

	/*
	 * RECORDABLE -> DISPLAYABLE
	 */
	
	public static Set<Stat> getRecordableStatsFromDisplayables(Set<Stat> displayableStatsIn) {
		Set<Stat> displayableStats = new HashSet<Stat>(displayableStatsIn);
		Set<Stat> recordableStats = new HashSet<Stat>();
		transformDisplayableScoringStats(recordableStats, displayableStats);
		transformDisplayableNonScoringStats(recordableStats, displayableStats);
		return Util.getOrderedSet(recordableStats);
	}
	
	/**
	 * SCORING transformations:
	 * TPA -> IIPM, TPM, IIPF, TPF
	 * TPM -> IIPM, TPM,
	 * FGA -> IIPM, IIPF
	 * else -> IIPM
	 */
	private static void transformDisplayableScoringStats(Set<Stat> recordableStats, Set<Stat> displayableStats) {
		if (displayableStats.contains(Stat.FGM)) {
			recordableStats.add(Stat.IIPM);
		}
		if (displayableStats.contains(Stat.TPM)) {
			recordableStats.add(Stat.TPM);
		}
		if (displayableStats.contains(Stat.FGA)) {
			recordableStats.add(Stat.IIPF);
		}
		if (displayableStats.contains(Stat.TPA)) {
			recordableStats.add(Stat.TPF);
		}
		if (displayableStats.contains(Stat.FTM)) {
			recordableStats.add(Stat.FTM);
		}
		if (displayableStats.contains(Stat.FTA)) {
			recordableStats.add(Stat.FTF);
		}
	}

	/**
	 *  REBOUND transformations:
	 *  OFF, DEF, REB -> OFF, DEF
	 *  OFF -> OFF
	 *  DEF -> DEF
	 *  REB -> REB
	 */
	private static void transformDisplayableNonScoringStats(Set<Stat> recordableStats,	Set<Stat> displayableStats) {
		displayableStats.remove(Stat.TOT);
		if (displayableStats.contains(Stat.REB) 
				&& (displayableStats.contains(Stat.OFF) || displayableStats.contains(Stat.DEF))) {
			displayableStats.remove(Stat.REB);
		}
		for (Stat displayableStat : displayableStats) {
			if (!displayableStat.isScoring() && displayableStat.isRecordable()) {
				recordableStats.add(displayableStat);
			}
		}
	}

	//////////////////////////////////
	
	/*
	 *  DISPLAYABLE -> RECORDABLE
	 */
	
	public static Set<Stat> getDisplayableStatsFromRecordables(Set<Stat> recordableStats) {
		Set<Stat> displayableStats = new HashSet<Stat>();
		transformRecordableScoringStats(displayableStats, recordableStats);
		Stat[] nonScoringStats = getNonScoringDisplayableStatsFromRecordables(recordableStats);
		displayableStats.addAll(Arrays.asList(nonScoringStats));
		return Util.getOrderedSet(displayableStats);
	}

	/**
	 * SCORING transformations:
	 * if TPF -> FGM, FGA, TPM, TPA
	 * else if TPM -> FGM, TPM
	 * else if IIPF -> FGM, FGA
	 * else -> FGM
	 */
	private static void transformRecordableScoringStats(
			Set<Stat> displayableStats, Set<Stat> recordableStats) {
		if (Util.containsAny(recordableStats, Stat.IIPM, Stat.TPM)) {
			displayableStats.add(Stat.FGM);
		}
		if (recordableStats.contains(Stat.IIPF)) {
			displayableStats.add(Stat.FGA);
		}
		if (recordableStats.contains(Stat.TPM)) {
			displayableStats.add(Stat.TPM);
		}
		if (recordableStats.contains(Stat.TPF)) {
			displayableStats.add(Stat.TPA);
		}
		if (recordableStats.contains(Stat.FTM)) {
			displayableStats.add(Stat.FTM);
		}
		if (recordableStats.contains(Stat.FTF)) {
			displayableStats.add(Stat.FTA);
		}
	}

	/**
	 * REBOUND transformations:
	 * if OFF, DEF  -> OFF, DEF, REB
	 * else if OFF -> OFF
	 * else if DEF -> DEF
	 * else if REB -> REB
	 */
	public static Stat[] getNonScoringDisplayableStatsFromRecordables(Set<Stat> recordableStats) {
		Set<Stat> displayableStats = new LinkedHashSet<Stat>();
		boolean offFlag = false, defFlag = false;
		for (Stat stat : Stat.values()) {
			if (offFlag == true && defFlag == true) {
				displayableStats.add(Stat.TOT);
				offFlag = false;
				defFlag = false;
			}
			if (!stat.isScoring() 
					&& stat.isDisplayable() 
					&& recordableStats.contains(stat)) {
				displayableStats.add(stat);
				if (stat == Stat.OFF) {
					offFlag = true;
				}
				if (stat == Stat.DEF) {
					defFlag = true;
				}
			}
		}
		// PTS:
		if (Util.containsAny(recordableStats, scoreChangingStats)) {
			displayableStats.add(Stat.PTS);
		}
		return (Stat[]) displayableStats.toArray(new Stat[displayableStats.size()]);
	}

}
