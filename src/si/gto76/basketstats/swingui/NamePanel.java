package si.gto76.basketstats.swingui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
	private SwinGui mainWindow;
	private JLabel nameLabel;
	/////////////////////////////////	createPopupMenu
	public NamePanel(SwinGui mainWindow, JPanel nameContainer, HasName pot) {
		this.mainWindow = mainWindow;
		addNamePanel(nameContainer, pot);
		createPopupMenu(nameContainer);
	}
	/////////////////////////////////
	public JLabel getLabel() {
		return nameLabel;
	}
	
	/*
	 * ADD NAME PANEL
	 */
	private void addNamePanel(final JPanel nameContainer, final HasName pot) {
		Util.setAllSizes(nameContainer, NAMEPANEL_WIDTH, NAMEPANEL_HEIGHT);
		nameLabel = new JLabel(pot.getName()); //XXX
		
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
	
	/*
	 * SWITCH NAME LABEL WITH TEXT FIELD
	 */
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
			    	System.out.println(mainWindow.game);
			    }
			    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			    	switchBackToLabel(nameContainer, pot);
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
	private void switchBackToLabel(JPanel nameContainer, HasName pot) {
		nameContainer.removeAll();
		nameContainer.updateUI();
		nameLabel = new JLabel(pot.getName());
		nameContainer.add(nameLabel);
		
		mainWindow.mainPanel.validate();
		if (pot instanceof Team) {
    		updateTeamLabel(nameContainer, pot);
    	} else if (pot instanceof Player) {
    		// refresh whole gui, so that buttons get correct tool tip.
    		mainWindow.setUpNewContainer();
    	}
	}
	
	/*
	 * UPDATE TEAM LABEL
	 */
	private void updateTeamLabel(JPanel nameContainer, HasName pot) {
		// Extra work because of score by team name
		JLabel teamLabel = (JLabel) nameContainer.getComponent(0);
		Team team = (Team) pot;
		mainWindow.updateTeamLabelReference(team, teamLabel);
		mainWindow.updateScore();
	}
	
	//////////////////////////////////
	
	/*
	 * POPUP
	 */
	public void createPopupMenu(JPanel nameContainer) {
	    JMenuItem menuItem;
	    //Create the popup menu.
	    JPopupMenu popup = new JPopupMenu();
	    menuItem = new JMenuItem("Rename");
	    //menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem("Move Up");
	    //menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem("Move Down");
	    //menuItem.addActionListener(this);
	    popup.add(menuItem);
	    menuItem = new JMenuItem("Remove / Delete");
	    //menuItem.addActionListener(this);
	    popup.add(menuItem);

	    //Add listener to the text area so the popup menu can come up.
	    MouseListener popupListener = new PopupListener(popup);
	    nameContainer.addMouseListener(popupListener);
	}
	
	class PopupListener extends MouseAdapter {
	    JPopupMenu popup;

	    PopupListener(JPopupMenu popupMenu) {
	      popup = popupMenu;
	    }

	    public void mousePressed(MouseEvent e) {
	      maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	      maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	      if (e.isPopupTrigger()) {
	        popup.show(e.getComponent(), e.getX(), e.getY());
	      }
	    }
	  }
}
