package si.gto76.basketstats.swingui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Action;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.PlayerStatRecorder;
import si.gto76.basketstats.coreclasses.Stat;
import si.gto76.basketstats.coreclasses.Team;
import si.gto76.basketstats.coreclasses.Util;

/**
 * Row with players name, on-the-floor selector, stat buttons, and players score.
 * New one gets created after every stat change.
 */
public class PlayersRow {
	////////////////////////////
	private final Player player;
	private final PlayerStatRecorder playerStats;
	
	private final SwingGui mainWindow;
	private final JPanel mainPanel; 
	private int row;

	private NamePanel namePanel;
	private JCheckBox checkBox;
	private List<JButton> buttons;
	////////////////////////////

	public static PlayersRow fill(SwingGui mainWindow, Player player, PlayerStatRecorder playerStats,
			JPanel mainPanel, int row, boolean enabled) {
		PlayersRow playersRow = new PlayersRow(mainWindow, player, playerStats, mainPanel, row);
		playersRow.buildPlayersRow();
		playersRow.setEnabled(enabled);
		return playersRow;
	}
	
	private PlayersRow(SwingGui mainWindow, Player player,
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
		// PLAYERS POINTS:
        JLabel playersPoints = new JLabel("99");
		// BUTTONS:
		addPlayersButtons(buttons);
	}

	/*
	 * NAME
	 */
	private NamePanel createAndAddPlayersLabel(Player player) {
		JPanel playersNameContainer = new JPanel();
		NamePanel namePanel = new NamePanel(mainWindow, playersNameContainer, player);
		
		GridBagConstraints c = Util.getGridBagConstraints(0, row);		
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
		GridBagConstraints c = Util.getGridBagConstraints(1, row);	
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
				SwingGuiUtil.setAllSizes(button, 100, 10);
				buttons.add(button);
				mainWindow.buttonMap.put(action, button);
			}
		}
		return buttons;
	}
	
	private void addPlayersButtons(List<JButton> bbb) {
		GridLayout layout = new GridLayout(1, bbb.size()+1);
		layout.setHgap(1);
		JPanel panel = new JPanel(layout);
		for (JButton b : bbb) {
			panel.add(b);
		}
        // Puts players score in last column
        JPanel playersScoreContainer = createPlayersScoreContainer();
        panel.add(playersScoreContainer);
        ////
		GridBagConstraints c = Util.getGridBagConstraints(2, row);	
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 0, 2, 0); 
		mainPanel.add(panel, c);
	}

    public JPanel createPlayersScoreContainer() {
        JPanel innerPanel = new JPanel();
        Integer points = (playerStats.get(Stat.PTS));
        JLabel playersScore = new JLabel(points.toString());
        innerPanel.add(playersScore);

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.add(innerPanel);

        return outerPanel;
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
		SwingGuiUtil.setButtonText(button, action);
		// Action Listener
		button.addActionListener(new PlayersButtonListener(action, button));
		return button;
	}
	
	public class PlayersButtonListener implements ActionListener {
		private Action action;
		private JButton button;
		private Game game;
		
		public PlayersButtonListener(Action action, JButton button) {
			this.action = action;
			this.button = button;
			this.game = action.getTeam().game;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (shouldButtonEventTerminate()) {
				return;
			}
			boolean action_failed = !action.trigger();
			if (action_failed) {
				JOptionPane.showMessageDialog(null,"One of the teams has no players on the floor.", "",
				        JOptionPane.WARNING_MESSAGE);
				return;
			}
			mainWindow.updateScoreLabel();
			mainWindow.pushCommandOnStack(action);
			System.out.println(mainWindow.game);
			if (Conf.SHOW_STAT_VALUE_ON_BUTTON_LABEL) {
				SwingGuiUtil.setButtonText(button, action);
			}
		}
		
		private boolean shouldButtonEventTerminate() {
			if (!mainWindow.warnAboutUnevenSquads) {
				return false;
			}
			if (game.doBothTeamsHaveSameNumberOfPlayersOnTheFloor()) {
				return false;
			}
		    JCheckBox checkbox = new JCheckBox("Do not show this message again during this game.");  
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
