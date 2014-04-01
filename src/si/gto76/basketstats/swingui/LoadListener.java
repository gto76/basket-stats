package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import si.gto76.basketstats.GameLoader;
import si.gto76.basketstats.coreclasses.Game;

public class LoadListener implements ActionListener {
	SwinGui frame;

	public LoadListener(SwinGui frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		// adds file filters
		for (ExtensionFileFilter filter : ExtensionFileFilter.all) {
			fc.addChoosableFileFilter(filter);
		}
		fc.setFileFilter(ExtensionFileFilter.hsg);
		int returnVal = fc.showOpenDialog(frame.frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				boolean exit = true;
				if (frame.stateChangedSinceLastSave) {
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
					frame.frame.hide();
				}
			} catch (IOException f) {
			}
		}
	}
	
}
