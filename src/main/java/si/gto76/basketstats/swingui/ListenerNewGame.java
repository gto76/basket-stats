package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.basketstats.Conf;
import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.RecordingStats;
import si.gto76.basketstats.coreclasses.ShotValues;

public class ListenerNewGame implements ActionListener {
	///////////////////
	SwingGui mainWindow;
	///////////////////
	public ListenerNewGame(SwingGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	///////////////////
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean current_game_is_not_saved = mainWindow.stateChangedSinceLastSave;
		if (current_game_is_not_saved && user_wants_to_abort_loading()) {
			return;
		}
		Tuple<RecordingStats,ShotValues> statsAndValues = DialogNewGame.showDialogReturnNullIfCanceled();
		if (statsAndValues == null) {
			return;
		}
		createNewGameAndSetUpGui(statsAndValues);
	}
	
	private boolean user_wants_to_abort_loading() {
		return !SwingGui.exitDialog("Game was not saved.\nAre you sure you want to open another game?");
	}

	private void createNewGameAndSetUpGui(Tuple<RecordingStats, ShotValues> statsAndValues) {
		Game newGame = Conf.getDefaultGame(statsAndValues.x, statsAndValues.y);
		new SwingGui(newGame);
		System.out.println(newGame);
		mainWindow.frame.setVisible(false);
	}
	
}
