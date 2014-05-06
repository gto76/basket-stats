package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import si.gto76.basketstats.Conf;

/**
 * Class for storing players stat values.
 * What is actualy tracked is defined in Team.recordingStats set, so some values may be redundand;
 * for instance three point shots are always initialized, even if they are not tracked.
 */
public class PlayerStatRecorder implements HasStats {
	////////////////////////////////////////	
	private final Team team;
	private ShotRecorder shootingStatRecorder;
	private Map<Stat, Integer> values = new HashMap<Stat, Integer>();
	/**
	 * Used for linking buttons with this classes specific setters, and also
	 * kept on stack after execution for possible later undo.
	 */
	private final List<Action> actions = new ArrayList<Action>(); 
	////////////////////////////////////////
	
	public PlayerStatRecorder(Team team) {
		this.team = team;
		shootingStatRecorder = new ShotRecorder(team.game);
		initValuesAndActions(Stat.actionStats, Stat.playersStatRecorderStats);
	}
		
	public PlayerStatRecorder(Team team, Map<Stat,Integer> displayableStatsWithValues) {
		this(team);
		Map<Stat,Integer> shotStats = Util.subMap(displayableStatsWithValues, Stat.scoringStats);
		if (Conf.DEBUG) System.out.println("SHot STats: " + Arrays.toString(shotStats.values().toArray()));
		this.shootingStatRecorder = new ShotRecorder(shotStats, team.game);
		for (Stat stat : displayableStatsWithValues.keySet()) {
			boolean stat_is_not_of_scoring_type = !stat.isScoring();
			if (stat_is_not_of_scoring_type && stat.isRecordable()) {
				values.put(stat, displayableStatsWithValues.get(stat));
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
	 * ####### ####### ####### ####### ####### #######
	 * SETTERS SETTERS SETTERS SETTERS SETTERS SETTERS
	 * ####### ####### ####### ####### ####### #######
	 */
	
	/**
	 * Accepts: IIPM, IIPF, TPM, TPF, FTM, FTF, PM, OFF, DEF, AST, PF, ST, TO, BS
	 * Returns false if stat could not be changed, due to one of the teams
	 * not having any players on the floor.
	 */
	public boolean made(Stat stat) {
		 // Checked only when made, so we can still undo stats that are no longer tracked.
		isStatInRecordingStats(stat);
		if (team.game.oneTeamHasNoPlayersOnTheFloor()) {
			return false;
		}
		changeState(stat, true, 1);
		return true;
	}
	
	public void unMade(Stat stat) {
		changeState(stat, false, -1);
	}
	
	private void changeState(Stat stat, boolean made, int delta) {
		if (stat.isScoringOrPoints()) {
			int scoreDelta;
			if (made) {
				scoreDelta = shootingStatRecorder.made(stat);
			} else {
				scoreDelta = shootingStatRecorder.unMade(stat);
			}
			if (scoreDelta != 0) {
				team.game.setPlusMinus(scoreDelta, team);
			}
		} else {
			addToValue(stat, delta);
		}
	}
	
	private void isStatInRecordingStats(Stat stat) {
		boolean stat_is_not_in_recording_stats = !team.getRecordingStats().values.contains(stat);
		if (stat_is_not_in_recording_stats) {
			throw new IllegalArgumentException("Tried to input non input stat.");
		}
		if (stat == Stat.PM) {
			throw new IllegalArgumentException("Tried to change Plus minus with made/unmade method. " +
					"Must use protected method changePlusMinus");
		}
	}
	
	protected void changePlusMinus(int points) {
		addToValue(Stat.PM, points);
	}
	
	private void addToValue(Stat stat, int add) {
		int value = values.get(stat);
		values.put(stat, value + add);
	}
	
	////////////////////////////////////////

	/*
	 * ####### ####### ####### ####### ####### #######
	 * GETTERS GETTERS GETTERS GETTERS GETTERS GETTERS
	 * ####### ####### ####### ####### ####### #######
	 */
	
	/**
	 * Accepts: FGM-A 3PM-A PM OFF DEF TOT AST PF ST TO BS PTS 3PF IIPM IIPF
	 */
	public int get(Stat stat) {
		if (stat.isScoringOrPoints()) {
			return shootingStatRecorder.get(stat);
		} else if (stat == Stat.TOT || stat == Stat.REB) {
			return values.get(Stat.REB) + values.get(Stat.OFF) + values.get(Stat.DEF);
		} else {
			return values.get(stat);
		}
	}
	
	/**
	 * Checks only for REB, and not for OFF or DEF.
	 * Needed, because get(REB) returns sum of them all.
	 */
	public boolean hasUsedRebounds() {
		if (values.get(Stat.REB) == 0) {
			return false;
		} else {
			return true;
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
	
	public boolean areAllValuesZero() {
		boolean some_shooting_values_are_not_zero = !shootingStatRecorder.allValuesAreZero();
		if (some_shooting_values_are_not_zero) {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		PrinterCommon.appendStatsRow(sb, this, team);
		return sb.toString();
	}

}