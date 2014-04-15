package si.gto76.basketstats.swingui;

import java.awt.GridBagConstraints;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Team;

public class MainContainer {
	////////////////////////////////////////
	private final SwinGui mainWindow;
	private final JPanel mainPanel;
	private final Game game;
	private int lastFilledRow = 0;
	////////////////////////////////////////
	
	public static void fill(SwinGui mainWindow, JLabel team1Label, JLabel team2Label) {
		MainContainer instance = new MainContainer(mainWindow);
		instance.fillInstance(team1Label, team2Label);
	}
	
	private MainContainer(SwinGui mainWindow) {
		this.mainWindow = mainWindow;
		this.mainPanel = mainWindow.mainPanel;
		this.game = mainWindow.game;
	}
	
	private void fillInstance(JLabel team1Label, JLabel team2Label) {
		addPlace(game.getLocation());
		addTime();
		addTeam(game.getTeam1(), team1Label);
		addTeam(game.getTeam2(), team2Label);
	}
	
	////////////////////////////////////////

	private void addPlace(Location location) {
		JPanel placeContainer = new JPanel();
		new NamePanel(mainWindow, placeContainer, location);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = 0;       
		c.anchor = GridBagConstraints.WEST;
		mainPanel.add(placeContainer, c);
	}
	
	private void addTime() {
		JPanel timeContainer = new JPanel();
		timeContainer.setBorder(BorderFactory.createEmptyBorder(0,0,0,4));
		new TimePanel(mainWindow, timeContainer);
	    
	    GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;       
		c.gridy = 0;       
		c.gridwidth = 1;   
		c.anchor = GridBagConstraints.EAST;
		//c.ipadx = 15;
		mainPanel.add(timeContainer, c);
		lastFilledRow++;
	}

	private void addTeam(Team team, JLabel label) {
		addTeamName(team);
		// Players names and buttons
		addPlayers(team.getAllPlayersStats());
	}

	private void addTeamName(Team team) {
		// Team name and score
		JPanel p = new JPanel();
		new NamePanel(mainWindow, p, team);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = lastFilledRow;       
		c.gridwidth = 3;   
		c.weighty = 1.0;
		c.ipady = 0;
		mainPanel.add(p, c);
		lastFilledRow++;
	}
	
	private void addPlayers(Map<Player, PlayerStats> allPlayerStats) {
		for (Entry<Player, PlayerStats> playerAndStats : allPlayerStats.entrySet()) {
			Player player = playerAndStats.getKey();
			PlayerStats playerStats = playerAndStats.getValue();
			boolean enabled = game.getPlayersOnTheFloor().contains(player);
			PlayersRow playersRow = PlayersRow.fill(mainWindow, player, playerStats, mainPanel, 
					lastFilledRow, enabled);
			mainWindow.playersRowMap.put(player, playersRow);
			lastFilledRow++;
		}
	}
	
}
