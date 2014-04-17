package si.gto76.basketstats.swingui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStats;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class PlayersRow {
	////////////////////////////
	Player player;
	PlayerStats playerStats;
	
	SwinGui mainWindow;
	JPanel mainPanel; 
	int row;

	NamePanel namePanel;
	JCheckBox checkBox;
	List<JButton> buttons;
	////////////////////////////

	public static PlayersRow fill(SwinGui mainWindow, Player player, PlayerStats playerStats, 
			JPanel mainPanel, int row, boolean enabled) {
		PlayersRow playersRow = new PlayersRow(mainWindow, player, playerStats, mainPanel, row);
		playersRow.buildPlayersRow();
		playersRow.setEnabled(enabled);
		return playersRow;
	}
	
	private PlayersRow(SwinGui mainWindow, Player player,
			PlayerStats playerStats, JPanel mainPanel, int row) {
		this.mainWindow = mainWindow;
		this.player = player;
		this.playerStats = playerStats;
		this.mainPanel = mainPanel;
		this.row = row;
	}	
	
	//////////////////////////////
	
	public void setEnabled(boolean enabled) {
		if (enabled) {
			enable();
		} else {
			disable();
		}
	}

	/*
	 * Set label color to black, tick on checkbox, enable buttons.
	 */
	public void enable() {
		changeState(Color.BLACK, true);
		playerStats.getTeam().putPlayerOnTheFloor(player);
	}
	
	/*
	 * Grey out label, tick off checkbox, disable buttons.
	 */
	public void disable() {
		changeState(Color.GRAY, false);
		playerStats.getTeam().putPlayerOffTheFloor(player);
	}
	
	private void changeState(Color labelColor, boolean componentsSelected) {
		namePanel.getLabel().setForeground(labelColor);
		checkBox.setSelected(componentsSelected);
		for (JButton button : buttons) {
			button.setEnabled(componentsSelected);
		}
	}
	
	//////////////////////////////
	
	private void buildPlayersRow() {
		// NAME:
		namePanel = createPlayersLabel(player);
		// CHECKBOX:
		buttons = createPlayersButtons(playerStats);
		checkBox = createPlayersCheckBox(player, playerStats.getTeam(), buttons);
		//mainWindow.checkBoxMap.put(player, checkBox);
		addPlayersCheckBox(checkBox);
		// BUTTONS:
		addPlayersButtons(buttons);
	}

	/*
	 * NAME
	 */
	private NamePanel createPlayersLabel(Player player) {
		JPanel playersNameContainer = new JPanel();
		NamePanel namePanel = new NamePanel(mainWindow, playersNameContainer, player);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;       
		c.gridy = row;       
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(playersNameContainer, c);
		
		return namePanel;
	}
	
	/*
	 * ON FLOOR SELECTOR
	 */
	private JCheckBox createPlayersCheckBox(final Player player, Team team, List<JButton> buttons) {
		JCheckBox onFloorSelector = new JCheckBox();
		onFloorSelector.setSelected(true);
		onFloorSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent
	            	.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				if (selected) {
					enable();
				} else {
					disable();
				}
			}
		});
		return onFloorSelector;
	}
	
	private void addPlayersCheckBox(JCheckBox checkBox) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;       
		c.gridy = row;       
		c.weighty = 1.0;
		mainPanel.add(checkBox, c);
	}

	/*
	 * BUTTONS
	 */
	private List<JButton> createPlayersButtons(PlayerStats stats) {
		List<JButton> buttons = new ArrayList<JButton>();
		for (Action action : stats.getActions()) {
			// If we are recording this action, acording to recording stats:
			if (playerStats.getTeam().getRecordingStats().values.contains(action.getStat())) {
				JButton button = createActionButton(action);
				Util.setAllSizes(button, 100, 10);
				buttons.add(button);
				mainWindow.buttonMap.put(action, button);
			}
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
		c.gridy = row;       
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 0, 2, 0); 
		mainPanel.add(panel, c);
	}

	/*
	 * BUTTON
	 */
	private JButton createActionButton(final Action action) {
		Stat stat = action.getStat();
		final JButton button = new JButton(stat.getName());
		button.setMargin(new Insets(0, 0, 0, 0));
		// Colors
		if (stat == Stat.OFF || stat == Stat.DEF || stat == Stat.REB) {
			button.setBackground(Conf.REBOUND_BUTTON_COLOR);
		}
		if (Conf.COLORED_MADE_BUTTONS && (stat == Stat.IIPM || stat == Stat.TPM || stat == Stat.FTM)) {
			button.setBackground(Conf.MADE_SHOT_BUTTON_COLOR);
		}
		if (Conf.COLORED_MISSED_BUTTONS && (stat == Stat.IIPF || stat == Stat.TPF || stat == Stat.FTM)) {
			button.setBackground(Conf.MISSED_SHOT_BUTTON_COLOR);
		}
		if (Conf.COLORED_TURNOVER_BUTTONS && stat == Stat.TO) {
			button.setBackground(Conf.TURNOVER_BUTTON_COLOR);
		}
		// Tool Tip
		if (Conf.BUTTONS_TOOLTIP) {
			button.setToolTipText(action.getPlayer() + " - " + stat.getExplanation());
		}
		Util.setButtonText(button, action);
			
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Integer scoreDelta = action.trigger();
				action.trigger();
				mainWindow.updateScore();
				mainWindow.pushCommandOnStack(action);
				System.out.println(mainWindow.game);
				if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
					Util.setButtonText(button, action);
				}
			}
		});
		return button;
	}
	
}
