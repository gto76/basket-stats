package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;

public class ListenerNewGame implements ActionListener {
	SwinGui mainWindow;

	public ListenerNewGame(SwinGui mainWindow) {
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
			Tuple<RecordingStats,ShotValues> statsAndValues = DialogNewGame.showDialog();
			if (statsAndValues == null) {
				return;
			}
			Game derbi = Conf.getDefaultGame(statsAndValues.x, statsAndValues.y);
			new SwinGui(derbi);
			System.out.println(derbi);
			mainWindow.frame.hide();
		}
	}

}
