package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.GameLoader;

public class ListenerLoad implements ActionListener {
	SwinGui mainWindow;

	public ListenerLoad(SwinGui mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		// adds file filters
		for (FileFilterExtension filter : FileFilterExtension.all) {
			fc.addChoosableFileFilter(filter);
		}
		fc.setFileFilter(FileFilterExtension.hsg);
		int returnVal = fc.showOpenDialog(mainWindow.frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				boolean exit = true;
				if (mainWindow.stateChangedSinceLastSave) {
					exit = SwinGui.exitDialog("Game was not saved.\n" +
							"Are you sure you want to open another game?");
				}
				if (exit) {
					File fIn = fc.getSelectedFile();
					Scanner sc = new Scanner(fIn);
					String gameString = sc.useDelimiter("\\A").next();
					sc.close();
					try {
					 	Game derbi = GameLoader.createGameFromString(gameString);
						new SwinGui(derbi);
						System.out.println(derbi);
						mainWindow.frame.setVisible(false);
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(null, 
								"Could not load game.\nError at line " +e1.getErrorOffset()+1+ ":\n" +e1.getMessage(), 
								"Load Failure", JOptionPane.WARNING_MESSAGE);
					}
				}
			} catch (IOException f) {
			}
		}
	}
	
}
