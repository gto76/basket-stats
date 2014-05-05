package si.gto76.basketstats.coreclasses;

/**
 * Used for button listeners and undo stack. It tells which player made what move,
 * and enables to record it or undo it with use of trigger() and undo() methods.
 */
public class Action {
	////////////////////////////////////////
	private final Stat stat;
	private final PlayerStatRecorder playerStatRecorder;
	////////////////////////////////////////
	
	public Action(Stat stat, PlayerStatRecorder playerStatRecorder) {
		this.stat = stat;
		this.playerStatRecorder = playerStatRecorder;
	}
	
	////////////////////////////////////////

	/*
	 * SETTERS:
	 */
	public boolean trigger() {
		return playerStatRecorder.made(stat);
	}

	public void undo() {
		playerStatRecorder.unMade(stat);
	}
	
	/*
	 * GETTERS:
	 */
	public int getStatValue() {
		return playerStatRecorder.get(stat);
	}

	public String getStatName() {
		return stat.getName();
	}
	
	public Stat getStat() {
		return stat;
	}

	public Team getTeam() {
		return playerStatRecorder.getTeam();
	}

	public Player getPlayer() {
		return playerStatRecorder.getPlayer();
	}

}