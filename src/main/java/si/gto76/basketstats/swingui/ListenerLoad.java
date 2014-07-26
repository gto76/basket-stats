package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import si.gto76.basketstats.coreclasses.Game;
import si.gto76.basketstats.coreclasses.GameLoader;

public class ListenerLoad implements ActionListener {
	///////////////////
	SwingGui mainWindow;
	///////////////////
	public ListenerLoad(SwingGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	///////////////////
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		for (FileFilterExtension filter : FileFilterExtension.all) {
			fileChooser.addChoosableFileFilter(filter);
		}
		fileChooser.setFileFilter(FileFilterExtension.hsg);
		int returnVal = fileChooser.showOpenDialog(mainWindow.frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
                loadGame(fileChooser);
            } catch (FileNotFoundException g) {
                JOptionPane.showMessageDialog(null,
                        "File with that name does not exist.", "Load Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void loadGame(JFileChooser fileChooser) throws FileNotFoundException {
		boolean current_game_is_not_saved = mainWindow.stateChangedSinceLastSave;
		if (current_game_is_not_saved && user_wants_to_abort_loading()) {
			return;
		}
		String gameString = getGameString(fileChooser);
		createNewGameAndSetUpGui(gameString);
	}

	private boolean user_wants_to_abort_loading() {
		return !SwingGui.exitDialog("Game was not saved.\nAre you sure you want to open another game?");
	}

	private String getGameString(JFileChooser fileChooser) throws FileNotFoundException {
		File fIn = fileChooser.getSelectedFile();
		Scanner sc = new Scanner(fIn);
		String gameString = sc.useDelimiter("\\A").next();
		sc.close();
		return gameString;
	}

	private void createNewGameAndSetUpGui(String gameString) {
		try {
		 	Game game = GameLoader.createGameFromString(gameString);
			new SwingGui(game);
			System.out.println(game);
			mainWindow.frame.setVisible(false);
		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(null, 
					"Could not load game.\nError at line " +(e1.getErrorOffset()+1)+ ":\n" +e1.getMessage(), 
					"Load Failure", JOptionPane.ERROR_MESSAGE);
		}
	}	
	
}
