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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStatRecorder;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;

public class PlayersRow {
	////////////////////////////
	private final Player player;
	private final PlayerStatRecorder playerStats;
	
	private final SwinGui mainWindow;
	private final JPanel mainPanel; 
	private int row;

	private NamePanel namePanel;
	private JCheckBox checkBox;
	private List<JButton> buttons;
	////////////////////////////

	public static PlayersRow fill(SwinGui mainWindow, Player player, PlayerStatRecorder playerStats, 
			JPanel mainPanel, int row, boolean enabled) {
		PlayersRow playersRow = new PlayersRow(mainWindow, player, playerStats, mainPanel, row);
		playersRow.buildPlayersRow();
		playersRow.setEnabled(enabled);
		return playersRow;
	}
	
	private PlayersRow(SwinGui mainWindow, Player player,
			PlayerStatRecorder playerStats, JPanel mainPanel, int row) {
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
	 * Set label color to black, tick on checkbox, enable buttons, put player on the floor.
	 */
	public void enable() {
		changeState(Color.BLACK, true);
		playerStats.getTeam().putPlayerOnTheFloor(player);
	}
	
	/*
	 * Grey out label, tick off checkbox, disable buttons, put player off the floor.
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
		namePanel = createAndAddPlayersLabel(player);
		// CHECKBOX:
		buttons = createPlayersButtons(playerStats);
		checkBox = createPlayersCheckBox(player, playerStats.getTeam(), buttons);
		addPlayersCheckBox(checkBox);
		// BUTTONS:
		addPlayersButtons(buttons);
	}

	/*
	 * NAME
	 */
	private NamePanel createAndAddPlayersLabel(Player player) {
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
		onFloorSelector.addActionListener(new CheckBoxListener());
		return onFloorSelector;
	}
	
	class CheckBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
			boolean selected = abstractButton.getModel().isSelected();
			if (selected) {
				enable();
			} else {
				disable();
			}
		}
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
	private List<JButton> createPlayersButtons(PlayerStatRecorder stats) {
		List<JButton> buttons = new ArrayList<JButton>();
		for (Action action : stats.getActions()) {
			boolean we_are_recording_this_action =
					playerStats.getTeam().getRecordingStats().values.contains(action.getStat());
			if (we_are_recording_this_action) {
				JButton button = createPlayersButton(action);
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
	private JButton createPlayersButton(final Action action) {
		Stat stat = action.getStat();
		final JButton button = new JButton(stat.getName());
		// Margin
		button.setMargin(new Insets(0, 0, 0, 0));
		// Colors
		Color color = Conf.BUTTON_COLORS.get(stat);
		if (color != null) {
			button.setBackground(color);
		}
		// Tool Tip
		if (Conf.BUTTONS_TOOLTIP) {
			button.setToolTipText(action.getPlayer() + " - " + stat.getExplanation());
		}
		// Text
		Util.setButtonText(button, action);
		// Action Listener
		button.addActionListener(new PlayersButtonListaner(action, button));
		return button;
	}
	
	public class PlayersButtonListaner implements ActionListener {
		private Action action;
		private JButton button;
		private Game game;
		
		public PlayersButtonListaner(Action action, JButton button) {
			this.action = action;
			this.button = button;
			this.game = action.getTeam().game;
		}

		public void actionPerformed(ActionEvent arg0) {
			boolean shouldReturn = checkForUnevenSquads();
			if (shouldReturn) {
				return;
			}
			boolean suceeded = action.trigger();
			if (!suceeded) {
				JOptionPane.showMessageDialog(null,"One of the teams has no players on the floor.", "",
				        JOptionPane.WARNING_MESSAGE);
				return;
			}
			mainWindow.updateScore();
			mainWindow.pushCommandOnStack(action);
			System.out.println(mainWindow.game);
			if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
				Util.setButtonText(button, action);
			}
		}
		
		/*
		 * Returns weather action listener should terminate;
		 */
		private boolean checkForUnevenSquads() {
			if (!mainWindow.warnAboutUnevenSquads) {
				return false;
			}
			if (game.doBothTeamsHaveSameNumberOfPlayersOnFloor()) {
				return false;
			}
		    JCheckBox checkbox = new JCheckBox("Do not show this message again.");  
		    String message = "Teams don't have equal number of players on the floor.\n" +
					"Do you want to continue?";
		    Object[] params = {message, checkbox};  
			int returnValue = JOptionPane.showOptionDialog(null, params, 
					"", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, 
					null, null);
			if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CANCEL_OPTION) {
				return true;
			}
			if (checkbox.isSelected()) {
				mainWindow.warnAboutUnevenSquads = false;
			}
			return false;
		}
	}
	
}
