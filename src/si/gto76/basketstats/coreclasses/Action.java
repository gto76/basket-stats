package si.gto76.basketstats.coreclasses;

public class Action {
	////////////////////////////////////////
	private final Stat stat;
	private final PlayerStats playersStats;
	////////////////////////////////////////
	
	public Action(Stat stat, PlayerStats playersStats) {
		this.stat = stat;
		this.playersStats = playersStats;
	}
	
	////////////////////////////////////////
	
	public int getStatValue() {
		return playersStats.get(stat);
	}

	public String getStatName() {
		return stat.getButtonName();
	}

	public Team getTeam() {
		return playersStats.getTeam();
	}

	public Player getPlayer() {
		return playersStats.getPlayer();
	}

	public Integer trigger() {
		return playersStats.made(stat);
	}

	public Integer undo() {
		return playersStats.unMade(stat);
	}

}