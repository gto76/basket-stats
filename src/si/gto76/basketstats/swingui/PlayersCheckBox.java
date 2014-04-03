package si.gto76.basketstats.swingui;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class PlayersCheckBox extends JCheckBox {
	private static final long serialVersionUID = -2718911995903818792L;
	////////////////////////////
	Player player;
	Team team;
	List<JButton> buttons;
	////////////////////////////
	public PlayersCheckBox(Player player, Team team, List<JButton> buttons) {
		this.player = player;
		this.team = team;
		this.buttons = buttons;
	}
	////////////////////////////
	public void enableAllButtons() {
		for (JButton button : buttons) {
			button.setEnabled(true);
		}
	}

	public void disableAllButtons() {
		for (JButton button : buttons) {
			button.setEnabled(false);
		}
	}
}
