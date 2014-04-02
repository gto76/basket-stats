package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import si.gto76.basketstats.Conf;

public class Menu {
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

	public Menu() {
		menuFile.setText("File");
		menuFileNew.setText("New");
		menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuFileOpen.setText("Open...");
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuFileSaveas.setText("Save As...");
		menuFileSaveas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menuFileExit.setText("Exit");
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuEdit.setText("Edit");
		menuEditUndo.setText("Undo");
		menuEditUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuEditAddPlayer1.setText("Add Player to TEAM A");
		menuEditAddPlayer1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		menuEditAddPlayer2.setText("Add Player to TEAM B");
		menuEditAddPlayer2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		menuHelp.setText("Help");
		menuHelpHelp.setText("Help...");
		menuHelpHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuHelpAbout.setText("About...");
		menuHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));

		menuFile.add(menuFileNew);
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileSaveas);
		menuFile.addSeparator();
		menuFile.add(menuFileExit);
		menuBar.add(menuFile);
		menuEdit.add(menuEditUndo);
		menuEdit.addSeparator();
		menuEdit.add(menuEditAddPlayer1);
		menuEdit.add(menuEditAddPlayer2);
		menuBar.add(menuEdit);
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);
		menuBar.add(menuHelp);
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

}
