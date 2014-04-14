package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class for storing players stat values.
 * What is actualy tracked is defined in Team.recordingStats set, so some values may be redundand;
 * for instance three point shoots are always initialized, even if they are not tracked.
 */
public class PlayerStats implements HasStats {
	////////////////////////////////////////	
	private final Team team;
	private Shots shootingValues = new Shots();
	private Map<Stat, Integer> values = new HashMap<Stat, Integer>();
	/*
	 * Used for linking buttons with this classes specific setters, and also
	 * kept on stack after execution for possible later undo.
	 */
	private final List<Action> actions = new ArrayList<Action>(); 
	////////////////////////////////////////
	
	public PlayerStats(Team team) {
		this.team = team;
		// Diferent init depending on are we deferentiating between OFF and DEF rebounds,
		// or are we loging them together under REB.
		// not necesary:
//		if (team.hasOnlyReb()) {
//			initValuesAndActions(Stat.inputValuesNoOffDef, 
//					Stat.nonScoringInputValuesNoOffDefAndPlusMinus);
//		} else {
			initValuesAndActions(Stat.inputValues, 
					Stat.nonScoringInputValuesAndPlusMinus);
//		}
	}
	
	private void initValuesAndActions(Stat[] actionSet, Stat[] valueSet) {
		for (Stat stat :  actionSet) {
			actions.add(new Action(stat, this));
		}
		for (Stat stat : valueSet) {
			values.put(stat, 0);
		}
	}

	boolean DEBUG = true;
	
	public PlayerStats(Team team, Map<Stat,Integer> outputStatsWithValues) {
		this(team);
		if (DEBUG)
			System.out.println("OUtput STats: " + Arrays.toString(outputStatsWithValues.keySet().toArray()));
		Map<Stat,Integer> shotStats = subMap(outputStatsWithValues, Stat.scoringValues);
		if (DEBUG)
			System.out.println("SHot STats: " + Arrays.toString(shotStats.values().toArray()));
		this.shootingValues = new Shots(shotStats);
		//Map<Stat,Integer> otherStats = subMap(outputStatsWithValues, Stat.nonScoringValuesWithouthPoints);
		//values.putAll(otherStats);
		// OFF, DEF -> OFF, DEF
		// OFF -> OFF
		// DEF -> DEF
		// REB -> REB
		for (Stat outputStat : outputStatsWithValues.keySet()) {
			if (!outputStat.isScoringValue() && outputStat.isInputValue()) {
				values.put(outputStat, outputStatsWithValues.get(outputStat));
			}
		}
	
	}
	
	public static Map<Stat,Integer> subMap(Map<Stat,Integer> mapIn, Stat[] set) {
		Map<Stat,Integer> mapOut = new HashMap<Stat,Integer>();
		for (Stat stat : set) {
			if (mapIn.containsKey(stat)) {
				mapOut.put(stat, mapIn.get(stat));
			}
		}
		return mapOut;
	}
	
	public PlayerStats(Team team, int fgm, int fga, int tpm, int tpa, 
			int plusMinus, int off, int def, int ast, int pf, int st, int to, int bs) {
		this(team);
		this.shootingValues = new Shots(fgm, fga, tpm, tpa);
		values.put(Stat.PM, plusMinus);
		values.put(Stat.OFF, off);
		values.put(Stat.DEF, def);
		values.put(Stat.AST, ast);
		values.put(Stat.PF, pf);
		values.put(Stat.ST, st);
		values.put(Stat.TO, to);
		values.put(Stat.BS, bs);
	}
	
	////////////////////////////////////////

	/*
	 * SETTERS
	 */
	//IIPM, IIPF, TPM, TPF, PM, OFF, DEF, AST, PF, ST, TO, BS
	/*
	 * Return value is for plus minus handling which is executed in SwinGui class.
	 */
	public Integer made(Stat stat) {
		checkArgument(stat);	
		if (stat.isScoringValueOrPoints()) {
			return shootingValues.made(stat);
		} else {
			addToValue(stat, 1);
			return null;
		}
	}
	
	public Integer unMade(Stat stat) {
		checkArgument(stat);
		if (stat.isScoringValueOrPoints()) {
			return shootingValues.unMade(stat);
		} else {
			addToValue(stat, -1);
			return null;
		}
	}
	
	private void checkArgument(Stat stat) {
		if (!team.getRecordingStats().contains(stat)) {
			throw new IllegalArgumentException("Tried to input non input stat.");
		}
	}
	
	public Integer changePlusMinus(int points) {
		addToValue(Stat.PM, points);
		return null;
	}
	
	private void addToValue(Stat stat, int add) {
		int value = values.get(stat);
		values.put(stat, value + add);
	}
	
	////////////////////////////////////////

	/*
	 * GETTERS
	 */
	//FGM-A	3PM-A PM OFF DEF TOT AST PF ST TO BS PTS 3PF IIPM IIPF
	public int get(Stat stat) {
		if (stat.isScoringValueOrPoints()) {
			return shootingValues.get(stat);
		} else if (stat == Stat.REB) {
			if (team.hasOnlyReb()) {
				return values.get(Stat.REB);
			} else {
				return values.get(Stat.OFF) + values.get(Stat.DEF);
			}
		} else {
			if (DEBUG) System.out.println("Stat: " + stat);
			return values.get(stat);
		}
	}

	public List<Action> getActions() {
		return actions;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Player getPlayer() {
		return team.getPlayer(this);
	}
	
	////////////////////////////////////////
	
	@Override
	public String toString() {
		//FGM-A	3PM-A +/-	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
		StringBuilder sb = new StringBuilder();
		team.appendStatsRow(sb, this);
		return sb.toString();
	}

}