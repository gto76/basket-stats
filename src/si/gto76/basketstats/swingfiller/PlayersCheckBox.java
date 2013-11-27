package si.gto76.basketstats.swingfiller;

import java.awt.Component;
import java.awt.Graphics;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class PlayersCheckBox extends JCheckBox {
	Player player;
	Team team;
	List<JButton> buttons;
	public PlayersCheckBox(Player player, Team team, List<JButton> buttons) {
		this.player = player;
		this.team = team;
		this.buttons = buttons;
	}
	public void enableAllButtons() {
		for (JButton button : buttons) {
			button.setEnabled(true);
		}
	}
	public void disableAllButtons() {
		for (JButton button : buttons) {
			button.setEnabled(false);
			/*
			setDisabledIcon(new Icon() {
				
				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public int getIconWidth() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public int getIconHeight() {
					// TODO Auto-generated method stub
					return 0;
				}
			});
			*/
		}
	}
}
