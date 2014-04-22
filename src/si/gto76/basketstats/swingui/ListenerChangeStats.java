package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;

public class ListenerChangeStats implements ActionListener {
	SwinGui mainWindow;

	public ListenerChangeStats(SwinGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// send current stats, make shure you can still undo stats that are not in recordingStats
		// anymore. -> But what about shot values??? -> you cant change them: we need new dialog.
		// need to change gui after that too!, Now undo throws exception... -> se pravi za unmake
		// ne rabimo testirat ali je stat v recording stats
		// stats that already gave points can not be removed...
		// reb can only go from off to off, def...
		RecordingStats newRecordingStats = 
				DialogChangeStats.showDialog(mainWindow.game);
		if (newRecordingStats == null) {
			return;
		}
		mainWindow.game.recordingStats = newRecordingStats;
		mainWindow.setUpNewContainer();
		System.out.println(mainWindow.game);
	}
}
