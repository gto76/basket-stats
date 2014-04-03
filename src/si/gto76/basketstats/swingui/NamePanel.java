package si.gto76.basketstats.swingui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.HasName;
import si.gto76.basketstats.coreclasses.Team;

public class NamePanel {
	private static final int NAMEPANEL_WIDTH = 170;
	private static final int NAMEPANEL_HEIGHT = 25;
	private static final int TEXTFIELD_HEIGHT = 19;
	private SwinGui swinGui;
	
	public NamePanel(SwinGui swingFiller, JPanel nameContainer, HasName pot) {
		this.swinGui = swingFiller;
		addNamePanel(nameContainer, pot);
	}

	private void addNamePanel(final JPanel nameContainer, final HasName pot) {
		SwinGui.setAllSizes(nameContainer, NAMEPANEL_WIDTH, NAMEPANEL_HEIGHT);
		JLabel nameLabel = new JLabel(pot.getName()); //XXX
		
		// Updates team1Label or team2Label global variable, so that updateScore()
		// can update score located by teams name.
		if (pot instanceof Team) {
			swinGui.updateTeamLabelReference((Team) pot, nameLabel);
		}
		
		nameContainer.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				
				long clickedTimeOld = System.nanoTime();
				@Override
				public void mouseClicked(MouseEvent arg0) {
					long clickedTimeNew = System.nanoTime();
					long deltaTime = clickedTimeNew - clickedTimeOld;
					if (deltaTime < Conf.DOUBLE_CLICK_LAG) {
						switchNameLabelWithTextField(nameContainer, pot);
					}
					else {
						clickedTimeOld = clickedTimeNew;
					}
				}
			}
		);
		nameContainer.add(nameLabel);
	}

	private void switchNameLabelWithTextField(final JPanel nameContainer,
			final HasName pot) {

		nameContainer.removeAll();
		final JTextField textField = new JTextField (pot.getName());
		textField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			    	e.consume();
			    	String name = textField.getText();
			    	if (name.trim().length() != 0) {
			    		pot.setName(name);
			    	}
			    	switchBackToLabel(nameContainer, pot);
			    	System.out.println(swinGui.game);
			    }
			    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			    	switchBackToLabel(nameContainer, pot);
			    }
			}
		});
		
		SwinGui.setAllSizes(textField, NAMEPANEL_WIDTH, TEXTFIELD_HEIGHT); //XXX
		nameContainer.add(textField);
		nameContainer.validate();
		textField.requestFocus();
		textField.selectAll();
	}

	private void switchBackToLabel(JPanel nameContainer, HasName pot) {
		nameContainer.removeAll();
		nameContainer.updateUI();
		nameContainer.add(new JLabel(pot.getName()));
		swinGui.mainPanel.validate();
		if (pot instanceof Team) {
    		updateTeamLabel(nameContainer, pot);
    	}
	}
	
	private void updateTeamLabel(JPanel nameContainer, HasName pot) {
		// Extra work because of score by team name
		JLabel teamLabel = (JLabel) nameContainer.getComponent(0);
		Team team = (Team) pot;
		swinGui.updateTeamLabelReference(team, teamLabel);
		swinGui.updateScore();
	}
	
}
