package si.gto76.basketstats.swingui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.Util;
import si.gto76.basketstats.coreclasses.HasName;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class NamePanel {
	private static final int NAMEPANEL_WIDTH = 170;
	private static final int NAMEPANEL_HEIGHT = 25;
	private static final int TEXTFIELD_HEIGHT = 19;
	/////////////////////////////////
	protected final SwinGui mainWindow;
	protected final JPanel nameContainer;
	protected final HasName pot;
	protected JLabel nameLabel;
	/////////////////////////////////	createPopupMenu
	public NamePanel(SwinGui mainWindow, JPanel nameContainer, HasName pot) {
		this.mainWindow = mainWindow;
		this.pot = pot;
		this.nameContainer = nameContainer;
		addNamePanel();
		new PopUpMenu(this);
	}
	/////////////////////////////////
	public JLabel getLabel() {
		return nameLabel;
	}
	
	/*
	 * ADD NAME PANEL
	 */
	private void addNamePanel() {
		Util.setAllSizes(nameContainer, NAMEPANEL_WIDTH, NAMEPANEL_HEIGHT);
		nameLabel = new JLabel(pot.getName());
		
		// Updates team1Label or team2Label global variable, so that updateScore()
		// can update score located by teams name.
		if (pot instanceof Team) {
			mainWindow.updateTeamLabelReference((Team) pot, nameLabel);
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
						switchNameLabelWithTextField();
					}
					else {
						clickedTimeOld = clickedTimeNew;
					}
				}
			}
		);
		nameContainer.add(nameLabel);
	}
	
	/*
	 * SWITCH NAME LABEL WITH TEXT FIELD
	 */
	protected void switchNameLabelWithTextField() {
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
			    	switchBackToLabel();
			    	System.out.println(mainWindow.game);
			    }
			    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			    	switchBackToLabel();
			    }
			}
		});
		
		Util.setAllSizes(textField, NAMEPANEL_WIDTH, TEXTFIELD_HEIGHT);
		nameContainer.add(textField);
		nameContainer.validate();
		textField.requestFocus();
		textField.selectAll();
	}
	
	/*
	 * SWITCH BACK TO LABEL
	 */
	private void switchBackToLabel() {
		nameContainer.removeAll();
		nameContainer.updateUI();
		nameLabel = new JLabel(pot.getName());
		nameContainer.add(nameLabel);
		
		mainWindow.mainPanel.validate();
		if (pot instanceof Team) {
    		updateTeamLabel();
    	} else if (pot instanceof Player) {
    		// refresh whole gui, so that buttons get correct tool tip.
    		mainWindow.setUpNewContainer();
    	}
	}
	
	/*
	 * UPDATE TEAM LABEL
	 */
	private void updateTeamLabel() {
		// Extra work because of score by team name
		JLabel teamLabel = (JLabel) nameContainer.getComponent(0);
		Team team = (Team) pot;
		mainWindow.updateTeamLabelReference(team, teamLabel);
		mainWindow.updateScore();
	}
	
}
