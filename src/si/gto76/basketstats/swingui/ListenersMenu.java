package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Team;

public class ListenersMenu {
	static void add(Menu meni, final SwinGui mainWindow) {
		
		/*
		 * FILE
		 */
		// NEW
		meni.menuFileNew.addActionListener(new ListenerNewGame(mainWindow));
		// OPEN
		meni.menuFileOpen.addActionListener(new ListenerLoad(mainWindow));
		// SAVE AS
		meni.menuFileSaveas.addActionListener(new ListenerSave(mainWindow));
		// FILE EXIT
		meni.menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.onWindowClose();
			}
		});

		/*
		 * EDIT
		 */
		// UNDO
		meni.menuEditUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.undo();
			}
		});
		// ADD PLAYER TO TEAM 1
		meni.menuEditAddPlayer1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = mainWindow.game.getTeam1();
				mainWindow.addNewPlayerToTeam(team);
			}
		});		
		// ADD PLAYER TO TEAM 2
		meni.menuEditAddPlayer2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = mainWindow.game.getTeam2();
				mainWindow.addNewPlayerToTeam(team);
			}
		});
		// CHANGE STATS
		//meni.menuEditChangeStats.addActionListener(new ChangeStatsListener(mainWindow));
	
        /*
         * HELP
         */
		// HELP
        meni.menuHelpHelp.addActionListener (
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
        meni.menuHelpAbout.addActionListener (
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
