package si.gto76.basketstats.swingui;

import java.awt.GridBagConstraints;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Util;
import si.gto76.basketstats.coreclasses.Venue;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStatRecorder;
import si.gto76.basketstats.coreclasses.Team;

/**
 * Contains everything but menu.
 */
public class MainContainer {
	////////////////////////////////////////
	private final SwingGui mainWindow;
	private final JPanel mainPanel;
	private final Game game;
	private int lastFilledRow = 0;
	////////////////////////////////////////
	
	public static void fill(SwingGui mainWindow) {
		MainContainer instance = new MainContainer(mainWindow);
		instance.fillContainer();
	}
	
	private MainContainer(SwingGui mainWindow) {
		this.mainWindow = mainWindow;
		this.mainPanel = mainWindow.mainPanel;
		this.game = mainWindow.game;
	}

	////////////////////////////////////////
	
	private void fillContainer() {
		addPlaceContainer(game.getVenue());
		addTimeContainer();
		addTeamContainer(game.getTeam1());
		addTeamContainer(game.getTeam2());
	}

	private void addPlaceContainer(Venue location) {
		JPanel placeContainer = new JPanel();
		new NamePanel(mainWindow, placeContainer, location);
		
		GridBagConstraints c = Util.getGridBagConstraints(0, 0);
		c.anchor = GridBagConstraints.WEST;
		mainPanel.add(placeContainer, c);
	}
	
	private void addTimeContainer() {
		JPanel timeContainer = new JPanel();
		Border border = BorderFactory.createEmptyBorder(0,0,0,4);
		timeContainer.setBorder(border);
		new TimePanel(mainWindow, timeContainer);
	    
		GridBagConstraints c = Util.getGridBagConstraints(2, 0);
		c.gridwidth = 1;   
		c.anchor = GridBagConstraints.EAST;
		mainPanel.add(timeContainer, c);
		lastFilledRow++;
	}

	private void addTeamContainer(Team team) {
		addTeamName(team);
		addPlayers(team.getAllPlayersStatRecorders());
	}

	private void addTeamName(Team team) {
		// Team name and score
		JPanel panel = new JPanel();
		new NamePanel(mainWindow, panel, team);
		
		GridBagConstraints c = Util.getGridBagConstraints(0, lastFilledRow);
		c.gridwidth = 3;   
		c.weighty = 1.0;
		c.ipady = 0;
		mainPanel.add(panel, c);
		lastFilledRow++;
	}
	
	private void addPlayers(Map<Player, PlayerStatRecorder> allPlayerStats) {
		for (Entry<Player, PlayerStatRecorder> playerAndStats : allPlayerStats.entrySet()) {
			Player player = playerAndStats.getKey();
			PlayerStatRecorder playerStats = playerAndStats.getValue();
			boolean player_is_on_the_floor = game.getPlayersOnTheFloor().contains(player);
			PlayersRow playersRow = PlayersRow.fill(mainWindow, player, playerStats, mainPanel, 
					lastFilledRow, player_is_on_the_floor);
			mainWindow.playersRowMap.put(player, playersRow);
			lastFilledRow++;
		}
	}
	
}
