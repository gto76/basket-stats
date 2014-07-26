package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

import si.gto76.basketstats.coreclasses.Team;

public class ListenersMenu {
	static void add(Menu menu, final SwingGui mainWindow) {
		
		/*
		 * FILE
		 */
		// NEW
		menu.menuFileNew.addActionListener(new ListenerNewGame(mainWindow));
		// OPEN
		menu.menuFileOpen.addActionListener(new ListenerLoad(mainWindow));
		// SAVE AS
		menu.menuFileSaveAs.addActionListener(new ListenerSave(mainWindow));
		// FILE EXIT
		menu.menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.onWindowClose();
			}
		});

		/*
		 * EDIT
		 */
		// UNDO
		menu.menuEditUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.undo();
			}
		});
		// ADD PLAYER TO TEAM 1
		menu.menuEditAddPlayer1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = mainWindow.game.getTeam1();
				mainWindow.addNewPlayerToTeam(team);
			}
		});		
		// ADD PLAYER TO TEAM 2
		menu.menuEditAddPlayer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = mainWindow.game.getTeam2();
				mainWindow.addNewPlayerToTeam(team);
			}
		});
		// CHANGE STATS
		menu.menuEditChangeStats.addActionListener(new ListenerChangeStats(mainWindow));
	
        /*
         * HELP
         */
		// HELP
        menu.menuHelpHelp.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new DialogHelp();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );
        // ABOUT
        menu.menuHelpAbout.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new DialogAbout();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );
	}
}	
