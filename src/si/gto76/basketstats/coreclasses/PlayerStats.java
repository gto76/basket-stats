package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStats {
	////////////////////////////////////////
	private Shots shots = new Shots();
	private Map<Stat, Integer> values = new HashMap<Stat, Integer>();	
	private final Team team;
	private final List<Action> actions = new ArrayList<Action>();
	////////////////////////////////////////
	
	public PlayerStats(Team team) {
		this.team = team;
		for (Stat stat :  Stat.inputValues()) {
			actions.add(new Action(stat, this));
		}
		for (Stat stat : Stat.nonScoringInputValuesAndPlusMinus()) {
			values.put(stat, 0);
		}
	}
	
	public PlayerStats(Team team, int fgm, int fga, int tpm, int tpa, 
			int plusMinus, int off, int def, int ast, int pf, int st, int to, int bs) {
		this(team);
		this.shots = new Shots(fgm, fga, tpm, tpa);
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
		if (stat.isScoringValue()) {
			return shots.made(stat);
		} else {
			addToValue(stat, 1);
			return null;
		}
	}
	
	public Integer unMade(Stat stat) {
		checkArgument(stat);
		if (stat.isScoringValue()) {
			return shots.unMade(stat);
		} else {
			addToValue(stat, -1);
			return null;
		}
	}
	
	private void checkArgument(Stat stat) {
		if (!stat.isInputValueOrPlusMinus()) {
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
	//FGM-A	3PM-A PM OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS 3PF IIPM IIPF
	public int get(Stat stat) {
		if (stat.isScoringValue()) {
			return shots.get(stat);
		} else if (stat == Stat.REB) {
			return values.get(Stat.OFF) + values.get(Stat.DEF);
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
		//FGM-A	3PM-A +/-	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
		StringBuilder sb = new StringBuilder();
		sb.append(Team.padTab(get(Stat.FGM)+"-"+get(Stat.FGA)))
		.append(Team.padTab(get(Stat.TPM)+"-"+get(Stat.TPA)));
		for (Stat sc : Stat.nonScoringValuesAndPoints()) {
			sb.append(Team.padTab(Integer.toString(get(sc))));
		}
		return sb.toString();
	}

}