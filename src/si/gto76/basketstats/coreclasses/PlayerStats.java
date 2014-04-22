package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;

/**
 * Class for storing players stat values.
 * What is actualy tracked is defined in Team.recordingStats set, so some values may be redundand;
 * for instance three point shoots are always initialized, even if they are not tracked.
 */
public class PlayerStats implements HasStats {
	////////////////////////////////////////	
	private final Team team;
	private Shots shootingValues;
	//private Rebounds reboundValues;
	private Map<Stat, Integer> values = new HashMap<Stat, Integer>();
	/*
	 * Used for linking buttons with this classes specific setters, and also
	 * kept on stack after execution for possible later undo.
	 */
	private final List<Action> actions = new ArrayList<Action>(); 
	////////////////////////////////////////
	
	public PlayerStats(Team team) {
		this.team = team;
		shootingValues = new Shots(team.game);
		//reboundValues = new Rebounds(team.game);
		initValuesAndActions(Stat.actionSet, Stat.playerStatsValues);
	}
		
	public PlayerStats(Team team, Map<Stat,Integer> outputStatsWithValues) {
		this(team);
		Map<Stat,Integer> shotStats = Util.subMap(outputStatsWithValues, Stat.scoringValues);
		if (Conf.DEBUG) System.out.println("SHot STats: " + Arrays.toString(shotStats.values().toArray()));
		this.shootingValues = new Shots(shotStats, team.game);
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
	
	private void initValuesAndActions(Stat[] actionSet, Stat[] valueSet) {
		for (Stat stat : actionSet) {
			actions.add(new Action(stat, this));
		}
		for (Stat stat : valueSet) {
			values.put(stat, 0);
		}
	}

	////////////////////////////////////////

	/*
	 * SETTERS
	 */
	//IIPM, IIPF, TPM, TPF, PM, OFF, DEF, AST, PF, ST, TO, BS
	/*
	 * Return value is for plus minus handling which is executed in SwinGui class.
	 */
	public void made(Stat stat) {
		changeState(stat, true, 1);
	}
	
	public void unMade(Stat stat) {
		changeState(stat, false, -1);
	}
	
	private void changeState(Stat stat, boolean made, int delta) {
		checkArgument(stat);
		if (stat.isScoringValueOrPoints()) {
			int scoreDelta;
			if (made) {
				scoreDelta = shootingValues.made(stat);
			} else {
				scoreDelta = shootingValues.unMade(stat);
			}
			if (scoreDelta != 0) {
				team.game.setPlusMinus(scoreDelta, team);
			}
		} else {
			addToValue(stat, delta);
		}
	}
	
	private void checkArgument(Stat stat) {
		if (!team.getRecordingStats().values.contains(stat)) {
			throw new IllegalArgumentException("Tried to input non input stat.");
		}
		if (stat == Stat.PM) {
			throw new IllegalArgumentException("Tried to change Plus minus with made/unmade method. " +
					"Must use protected method changePlusMinus");
		}
	}
	
	protected Integer changePlusMinus(int points) {
		addToValue(Stat.PM, points);
		return null;
	}
	
	private void addToValue(Stat stat, int add) {
		int value = values.get(stat);
		values.put(stat, value + add);
	}
	
	public boolean isEmpty() {
		if (!shootingValues.isEmpty()) {
			return false;
		}
		for (Entry<Stat, Integer> entry : values.entrySet()) {
			if (entry.getKey() == Stat.PM) {
				continue;
			}
			if (entry.getValue() != 0) {
				return false;
			}
		}
		return true;
	}
	
	////////////////////////////////////////

	/*
	 * GETTERS
	 */
	//FGM-A	3PM-A PM OFF DEF TOT AST PF ST TO BS PTS 3PF IIPM IIPF
	public int get(Stat stat) {
		if (stat.isScoringValueOrPoints()) {
			return shootingValues.get(stat);
		} else if (stat == Stat.TOT) {
			return values.get(Stat.REB) + values.get(Stat.OFF) + values.get(Stat.DEF);
// OLD	} else if (stat == Stat.REB) {
//			if (team.hasOnlyReb()) {
//				return values.get(Stat.REB);
//			} else {
//				return values.get(Stat.OFF) + values.get(Stat.DEF);
//			}
		} else {
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
		StringBuilder sb = new StringBuilder();
		team.appendStatsRow(sb, this);
		return sb.toString();
	}

}