package si.gto76.basketstats.coreclasses;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;

public class Team {
	private static final DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	
	private String name;
	private Map<Player, PlayerStats> allPlayersStats = new HashMap<Player, PlayerStats>();
	Set<Player> playersOnTheFloor = new HashSet<Player>();
	
	public Team(String name, List<Player> players) {
		this.name = name;
		for (Player player : players) {
			PlayerStats ps = new PlayerStats(this);
			allPlayersStats.put(player, ps);
			//player.addToTeam(this);
			playersOnTheFloor.add(player);
		}
	}
	
	public String getName() {
		return name;
	}
	public PlayerStats getPlayersStats(Player player) {
		return allPlayersStats.get(player);
	}
	public Map<Player, PlayerStats> getAllPlayersStats() {
		return allPlayersStats;
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : allPlayersStats.keySet()) {
			players.add(player);
		}
		return players;
	}
	public int getNumberOfPlayers() {
		return allPlayersStats.size();
	}
	
	public void putPlayerOnTheFloor(Player player) {
		// if there's no passed player among teams players
		if (!allPlayersStats.keySet().contains(player)) {
			return;
		}
		playersOnTheFloor.add(player);
	}
	
	public void getPlayerOffTheFloor(Player player) {
		playersOnTheFloor.remove(player);
	}

	public void changePlusMinus(int points) {
		for (Player player : playersOnTheFloor) {
			PlayerStats playersStats = allPlayersStats.get(player);
			playersStats.changePlusMinus(points);
		}
	}
	
	public Integer get(StatCats statCat) {
		// plusMinus of team doesn't make sense;
		if (statCat == StatCats.PM) {
			return null;
		}
		int sum = 0;
		for (PlayerStats ps : allPlayersStats.values()) {
			sum += ps.get(statCat);
		}
		return sum;
	}
	
	public double getFgPercent() {
		int fgm = get(StatCats.FGM);
		int fga = get(StatCats.FGA);
		return zeroIfDevideByZero(fgm, fga);
	}
	public double getTpPercent() {
		int tpm = get(StatCats.TPM);
		int tpa = get(StatCats.TPA);
		return zeroIfDevideByZero(tpm, tpa);
	}
	
	private double zeroIfDevideByZero(int devidee, int devider) {
		if (devider == 0) {
			return 0;
		}
		return ((double) devidee / devider) * 100.0;
	}
	
	public boolean hasPlayer(Player player) {
		return allPlayersStats.keySet().contains(player);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		int numberOfTabsForPlayerName = 2;
		int tabWidth = 8;
		String theTabs = "";
		for (int i = numberOfTabsForPlayerName; i > 0; i--) {
			theTabs = theTabs + "\t";
		}
		
		// HEADER
		sb.append(name).append("\n").append(theTabs).append("FGM-A\t")
		.append("3PM-A\t");
		for (StatCats sc : StatCats.nonScoringValues()) {
			sb.append(sc.getName()).append("\t");
		}
		sb.append("\n");
		
		// PLAYER STATS
		for (Player player : allPlayersStats.keySet()) {
			String playersName = player.getShortName();
			int spaceForName = numberOfTabsForPlayerName * tabWidth;
			sb.append(Strings.padEnd(playersName, spaceForName , ' '))
			.append(allPlayersStats.get(player)).append("\n");
		}
		
		// TOTALS
		sb.append("Totals").append(theTabs).append(get(StatCats.FGM))
		.append("-").append(get(StatCats.FGA)).append("\t").append(get(StatCats.TPM))
		.append("-").append(get(StatCats.TPA)).append("\t");
		for (StatCats sc : StatCats.nonScoringValues()) {
			if (sc == StatCats.PM) {
				sb.append("\t");
				continue; 
			}
			sb.append(get(sc)).append("\t");
		}
		sb.append("\n");
		
		// PERCENTS
		sb.append(theTabs).append(oneDigit.format(getFgPercent())).append("%")
		.append("\t").append(oneDigit.format(getTpPercent())).append("%");
		sb.append("\n");
		
		return sb.toString();		
	}
	
}
