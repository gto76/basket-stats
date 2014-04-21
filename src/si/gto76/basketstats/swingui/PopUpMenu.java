package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Location;
import si.gto76.basketstats.coreclasses.Player;
import si.gto76.basketstats.coreclasses.Team;

//import si.gto76.basketstats.swingui.NamePanel.PopupListener;

public class PopUpMenu {
	public PopUpMenu(final NamePanel namePanel) {
	    JMenuItem menuItem;
	    //Create the popup menu.
	    JPopupMenu popup = new JPopupMenu();
	    
	    menuItem = new JMenuItem("Rename");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				namePanel.switchNameLabelWithTextField();
			}
		});
	    popup.add(menuItem);

	    if (namePanel.pot instanceof Player) {
	    	addPlayersItems(popup, namePanel);
	    }
	    
	    if (namePanel.pot instanceof Team) {
	    	menuItem = new JMenuItem("Add New Player");
	 	    menuItem.addActionListener(new ActionListener() {
	 			@Override
	 			public void actionPerformed(ActionEvent e) {
	 				namePanel.mainWindow.addNewPlayerToTeam((Team) namePanel.pot);
	 			}
	 		});
	 	    popup.add(menuItem);
	    	
	    }
	    
	    //Add listener to the text area so the popup menu can come up.
	    MouseListener popupListener = new PopupListener(popup);
	    namePanel.nameContainer.addMouseListener(popupListener);
	}
	
	private void addPlayersItems(JPopupMenu popup, final NamePanel namePanel) {
		final Player player = (Player) namePanel.pot;
		final Game game = namePanel.mainWindow.game;
		final Team team = game.getPlayersTeam(player);

		JMenuItem menuItem;
		
		if (team.getNumberOfPlayers() > 1) {
			popup.addSeparator();
		}
		
		if (!team.isPlayerFirst(player)) {
			menuItem = new JMenuItem("Move Up");
		    menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					team.moveUpOneRow(player);
					System.out.println(game);
					namePanel.mainWindow.setUpNewContainer();
				}
			});
		    popup.add(menuItem);
		}
		    
		if (!team.isPlayerLast(player)) {
		    menuItem = new JMenuItem("Move Down");
		    menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					team.moveDownOneRow(player);
					System.out.println(game);
					namePanel.mainWindow.setUpNewContainer();
				}
			});
		    popup.add(menuItem);
		}
	    
	    popup.addSeparator();
	    
	    menuItem = new JMenuItem("Add New Player");
	    menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				namePanel.mainWindow.addNewPlayerToTeam(team);
			}
		});
	    popup.add(menuItem);
	    
	    if (game.getPlayersStats(player).isEmpty() && team.getNumberOfPlayers() > 1) {
		    menuItem = new JMenuItem("Remove Player");
		    menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					team.removePlayer(player);
					System.out.println(game);
					namePanel.mainWindow.setUpNewContainer();
				}
			});
		    popup.add(menuItem);
	    }
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
