package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.basketstats.coreclasses.RecordingStats;

public class ListenerChangeStats implements ActionListener {
	private final SwingGui mainWindow;

	public ListenerChangeStats(SwingGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		RecordingStats newRecordingStats = 
				DialogChangeStats.showDialogReturnNullIfCanceled(mainWindow.game);
		if (newRecordingStats == null) {
			return;
		}
		mainWindow.game.recordingStats = newRecordingStats;
		mainWindow.setUpNewContainer();
		System.out.println(mainWindow.game);
	}
}
