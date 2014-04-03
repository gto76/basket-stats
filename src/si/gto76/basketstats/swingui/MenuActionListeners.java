package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.Team;

public class MenuActionListeners {
	static void add(Menu meni, final SwinGui mainWindow) {
		
		/*
		 * FILE
		 */
		// NEW
		meni.menuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean exit = true;
				if (mainWindow.stateChangedSinceLastSave) {
					exit = SwinGui.exitDialog("Game was not saved.\n" +
							"Are you sure you want to open new game?");
				}
				if (exit) {
					Game derbi = Conf.getDefaultGame();
					new SwinGui(derbi);
					System.out.println(derbi);
					mainWindow.frame.hide();
				}
			}
		});
		
		// OPEN
		meni.menuFileOpen.addActionListener(new LoadListener(mainWindow));
		// SAVE AS
		meni.menuFileSaveas.addActionListener(new SaveListener(mainWindow));//game, frame));
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
	
        /*
         * HELP
         */
        meni.menuHelpHelp.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new HelpDialog();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );
      
        meni.menuHelpAbout.addActionListener (
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						new AboutDialog();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
                }
            }
        );
	}
}	
