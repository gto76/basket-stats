package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

public class PopUpMenu {
	///////////////////////
	private final NamePanel namePanel;
	private final Game game;
	private Player player;
	private Team team;
	///////////////////////
	
	public PopUpMenu(final NamePanel namePanel) {
	    this.namePanel = namePanel;
	    this.game = namePanel.mainWindow.game;
		
	    JPopupMenu popupMenu = new JPopupMenu();
	    
	    addNewItemToMenu(popupMenu, "Rename", new RenameActionListener());
	    if (namePanel.hasName instanceof Player) {
	    	addPlayersItems(popupMenu);
	    }
	    if (namePanel.hasName instanceof Team) {
	    	addNewItemToMenu(popupMenu, "Add New Player", new NewPlayerTeamActionListener());
	    }
	    //Add listener to the text area so the popup menu can come up.
	    MouseListener popupListener = new PopupListener(popupMenu);
	    namePanel.nameContainer.addMouseListener(popupListener);
	}

	class RenameActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			namePanel.switchNameLabelWithTextField();
		}
	}
	
	class NewPlayerTeamActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			namePanel.mainWindow.addNewPlayerToTeam((Team) namePanel.hasName);
		}
	}

	///////////////////////
	
	private void addPlayersItems(JPopupMenu popupMenu) {
	    this.player = (Player) namePanel.hasName;
	    this.team = game.getPlayersTeam(player);
		
		if (team.getNumberOfPlayers() > 1) {
			popupMenu.addSeparator();
		}
		if (!team.isPlayerFirst(player)) {
			addNewItemToMenu(popupMenu, "Move Up", new MovePlayerUpActionListener());
		}
		if (!team.isPlayerLast(player)) {
			addNewItemToMenu(popupMenu, "Move Down", new MovePlayerDownActionListener());
		}
	    popupMenu.addSeparator();
	    addNewItemToMenu(popupMenu, "Add New Player", new AddNewPlayerActionListener());
	    
	    boolean player_has_not_yet_booked_any_stats = game.getPlayersStatRecorder(player).areAllValuesZero();
	    boolean player_is_not_alone_on_the_team = team.getNumberOfPlayers() > 1;
	    if (player_has_not_yet_booked_any_stats 
	    		&& player_is_not_alone_on_the_team) {
	    	addNewItemToMenu(popupMenu, "Remove Player", new RemovePlayerActionListener());
	    }
	}
	
	class MovePlayerUpActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			team.moveUpOneRow(player);
			System.out.println(game);
			namePanel.mainWindow.setUpNewContainer();
		}
	}
	
	class MovePlayerDownActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			team.moveDownOneRow(player);
			System.out.println(game);
			namePanel.mainWindow.setUpNewContainer();
		}
	}
	
	class AddNewPlayerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			namePanel.mainWindow.addNewPlayerToTeam(team);
		}
	}
	
	class RemovePlayerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			team.removePlayer(player);
			System.out.println(game);
			namePanel.mainWindow.setUpNewContainer();
		}
	}
	
	///////////////////////

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
	
	/*
	 * UTIL:
	 */
	
	private static void addNewItemToMenu(JPopupMenu popupMenu, String name, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(name);
	    menuItem.addActionListener(actionListener);
	    popupMenu.add(menuItem);
	}
	
}
