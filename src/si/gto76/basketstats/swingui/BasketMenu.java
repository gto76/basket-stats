package si.gto76.basketstats.swingui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class BasketMenu {
	JMenuBar menuBar = new JMenuBar();

	JMenu menuFile = new JMenu();
	JMenuItem menuFileNew = new JMenuItem();
	JMenuItem menuFileOpen = new JMenuItem();
	JMenuItem menuFileSaveas = new JMenuItem();
	JMenuItem menuFileExit = new JMenuItem();
	JMenu menuEdit = new JMenu();
	JMenuItem menuEditUndo = new JMenuItem();
	JMenuItem menuEditAddPlayer1 = new JMenuItem();
	JMenuItem menuEditAddPlayer2 = new JMenuItem();
	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public BasketMenu() {
		menuFile.setText("File");
		menuFileNew.setText("New");
		menuFileOpen.setText("Open...");
		menuFileSaveas.setText("Save As...");
		menuFileExit.setText("Exit");
		menuEdit.setText("Edit");
		menuEditUndo.setText("Undo");
		menuEditAddPlayer1.setText("Add Player To Team 1");
		menuEditAddPlayer2.setText("Add Player To Team 2");
		menuHelp.setText("Help");
		menuHelpHelp.setText("Help");
		menuHelpAbout.setText("About");

		menuFile.add(menuFileNew);
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileSaveas);
		menuFile.add(menuFileExit);
		menuBar.add(menuFile);
		menuEdit.add(menuEditUndo);
		menuEdit.add(menuEditAddPlayer1);
		menuEdit.add(menuEditAddPlayer2);
		menuBar.add(menuEdit);
		menuHelp.add(menuHelpHelp);
		menuHelp.add(menuHelpAbout);
		menuBar.add(menuHelp);
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

}
