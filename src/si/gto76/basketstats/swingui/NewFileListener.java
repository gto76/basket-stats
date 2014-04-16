package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;

public class NewFileListener implements ActionListener {
	SwinGui mainWindow;

	public NewFileListener(SwinGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean exit = true;
		if (mainWindow.stateChangedSinceLastSave) {
			exit = SwinGui.exitDialog("Game was not saved.\n" +
					"Are you sure you want to open new game?");
		}
		if (exit) {
			//new NewFileDialog();
			Game derbi = Conf.getDefaultGame();
			new SwinGui(derbi);
			System.out.println(derbi);
			mainWindow.frame.hide();
		}
	}

}
