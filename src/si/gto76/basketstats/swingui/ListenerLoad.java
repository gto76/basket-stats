package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.coreclasses.Game;

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
					Game derbi = GameLoader.createGameFromString(gameString);
					new SwinGui(derbi);
					System.out.println(derbi);
					//mainWindow.frame.hide(); // OLD
					mainWindow.frame.setVisible(false);
				}
			} catch (IOException f) {
			}
		}
	}
	
}
