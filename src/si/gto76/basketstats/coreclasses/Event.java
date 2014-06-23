package si.gto76.basketstats.coreclasses;

import java.util.Collections;
import java.util.Set;

/**
 * Tells who was on the floor when action occured.
 * You need a stack of Events to implement undo.
 */
public class Event {
	public final Action action;
	public final Set<Player> team1PlayersOnTheFloor;
	public final Set<Player> team2PlayersOnTheFloor;

	public Event(Action action, Set<Player> team1PlayersOnTheFloor,
			Set<Player> team2PlayersOnTheFloor) {
		if (action == null || team1PlayersOnTheFloor == null || team2PlayersOnTheFloor == null) {
			throw new NullPointerException();
		}
		if (team1PlayersOnTheFloor.size() == 0 || team2PlayersOnTheFloor.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.action = action;
		this.team1PlayersOnTheFloor = Collections.unmodifiableSet(team1PlayersOnTheFloor);
		this.team2PlayersOnTheFloor = Collections.unmodifiableSet(team2PlayersOnTheFloor);
	}

}
