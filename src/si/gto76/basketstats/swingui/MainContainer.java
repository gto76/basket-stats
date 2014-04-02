package si.gto76.basketstats.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Stat;
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
		//c.gridwidth = 2;   
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
			addPlayer(playerAndStats.getKey(), playerAndStats.getValue());
		}
	}
	
	private void addPlayer(Player player, PlayerStats playerStats) {
		// NAME:
		addPlayersName(player);
		// CHECKBOX:
		List<JButton> buttons = createPlayersButtons(playerStats);
		PlayersCheckBox checkBox = createPlayersCheckBox(player, playerStats.getTeam(), buttons);
		addPlayersCheckBox(checkBox);
		// BUTTONS:
		addPlayersButtons(buttons);
		lastFilledRow++;
	}

	private void addPlayersName(Player player) {
		JPanel playersNameContainer = new JPanel();
		new NamePanel(mainWindow, playersNameContainer, player);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(playersNameContainer, c);
	}
	
	private PlayersCheckBox createPlayersCheckBox(Player player, Team team, List<JButton> buttons) {
		// ON FLOOR SELECTOR
		PlayersCheckBox onFloorSelector = new PlayersCheckBox(player,
				team, buttons);
		onFloorSelector.setSelected(true);
		onFloorSelector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				PlayersCheckBox cb = (PlayersCheckBox) itemEvent.getItem();
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					cb.team.putPlayerOnTheFloor(cb.player);
					cb.enableAllButtons();
				} else if (state == ItemEvent.DESELECTED) {
					cb.team.getPlayerOffTheFloor(cb.player);
					cb.disableAllButtons();
				}
			}
		});
		return onFloorSelector;
	}
	
	private void addPlayersCheckBox(PlayersCheckBox checkBox) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		mainPanel.add(checkBox, c);
	}

	private List<JButton> createPlayersButtons(PlayerStats stats) {
		List<JButton> buttons = new ArrayList<JButton>();
		for (Action action : stats.getActions()) {
			JButton button = createActionButton(action);
			SwinGui.setAllSizes(button, 100, 10);
			buttons.add(button);
			mainWindow.buttonMap.put(action, button);
		}
		return buttons;
	}
	
	private void addPlayersButtons(List<JButton> bbb) {
		GridLayout layout = new GridLayout(1, bbb.size());
		layout.setHgap(1);
		JPanel panel = new JPanel(layout);
		for (JButton b : bbb) {
			panel.add(b);
		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;       
		c.gridy = lastFilledRow;       
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 0, 2, 0); 
		mainPanel.add(panel, c);
	}

	private JButton createActionButton(final Action action) {
		Stat stat = action.getStat();
		final JButton button = new JButton(stat.getName());
		// Colors
		if (stat == Stat.OFF || stat == Stat.DEF) {
			button.setBackground(Conf.REBOUND_BUTTON_COLOR);
		}
		if (Conf.COLORED_MADE_BUTTONS && (stat == Stat.IIPM || stat == Stat.TPM)) {
			button.setBackground(Conf.MADE_SHOT_BUTTON_COLOR);
		}
		if (Conf.COLORED_MISSED_BUTTONS && (stat == Stat.IIPF || stat == Stat.TPF)) {
			button.setBackground(Conf.MISSED_SHOT_BUTTON_COLOR);
		}
		if (Conf.COLORED_TURNOVER_BUTTONS && stat == Stat.TO) {
			button.setBackground(Conf.TURNOVER_BUTTON_COLOR);
		}
		// Tool Tip
		if (Conf.BUTTONS_TOOLTIP) {
			button.setToolTipText(stat.getExplanation());
		}
		setButtonText(button, action);
			
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer scoreDelta = action.trigger();
				if (scoreDelta != null) {
					Team playersTeam = action.getTeam();
					mainWindow.setPlusMinus(scoreDelta, playersTeam);
				}
				mainWindow.pushCommandOnStack(action);
				System.out.println(game);
				if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
					setButtonText(button, action);
				}
			}
		});
		return button;
	}
	
	/*
	 * UTILS
	 */
	
	protected static void setButtonText(JButton button, Action action) {
		if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
			button.setText(formatButtonText(action.getStatName(), action.getStatValue()));
		} else {
			button.setText(action.getStatName());
		}
	}
	
	private static String formatButtonText(String name, int value) {
		return name + Conf.BUTTON_TEXT_SEPARATOR + value;
	}
	
}
