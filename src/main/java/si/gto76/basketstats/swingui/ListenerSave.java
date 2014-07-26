package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ListenerSave implements ActionListener {
	protected static final String WHAT_IS_BEING_SAVED = "Game";
	/////////////////////////
	SwingGui mainWindow;
	/////////////////////////
	public ListenerSave(SwingGui mainWindow) {
		this.mainWindow = mainWindow;
	}
	/////////////////////////

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = createFileChooser();
		String fileName = mainWindow.game.getTeam1().getName() + " vs "
				+ mainWindow.game.getTeam2().getName() + " " + mainWindow.game.getDate().toString();
		fileChooser.setSelectedFile(new File(fileName));
		fileChooser.setDialogTitle("Save As");
		// Add file filters
		for (FileFilterExtension filter : FileFilterExtension.all) {
			fileChooser.addChoosableFileFilter(filter);
		}
		fileChooser.setFileFilter(FileFilterExtension.all[0]);
		// Show dialog
		fileChooser.showSaveDialog(mainWindow.frame);
	}

	private void saveFile(String formatName, File outputFile) {
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(outputFile));
			out.print(mainWindow.game.toString());
			out.close();
			mainWindow.stateChangedSinceLastSave = false;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "SAVE ERROR!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//////////////////////////
	
	/*
	 * FILE CHOOSER:
	 */

	class Var {
		String formatName;
		String errMessage;
		File outputFile;
		String givenName;
	}
	
	private JFileChooser createFileChooser() {
		return new JFileChooser() {
			private static final long serialVersionUID = 291238218189760373L;
			@Override
			public void approveSelection() {
				Var var = new Var();
				var.formatName = "";
				var.errMessage = "";
				var.outputFile = this.getSelectedFile();
				var.givenName = var.outputFile.getName();

				// Filename Test:
				boolean all_files_filter_was_selected = this.getFileFilter().getDescription() == "All Files";
				if (all_files_filter_was_selected) {
					getFormatFromFilenameExtension(var);
				} else { // specific_file_filter_was_selected
					getFormatFromSelectedFilter(var);
				}
				
				// If test passed:
				boolean filename_is_valid_and_it_passed_through_the_filter = var.formatName != "";
				if (filename_is_valid_and_it_passed_through_the_filter) {
					checkIfFileAlreadyExistsAndSaveIt(var);
				} else { // filename_is_invalid_or_it_didn't_pass_through_the_filter
					JOptionPane.showMessageDialog(null, var.errMessage, "Error", JOptionPane.ERROR_MESSAGE);
				}
			} 

			private void getFormatFromFilenameExtension(Var var) {
				FileFilterExtension fileFilter = FileFilterExtension.getFilterOrNull(var.givenName);
				boolean entered_filename_has_no_extension = var.givenName.indexOf(".") == -1;
				boolean entered_filenames_extension_is_not_supported = fileFilter == null;

				if (entered_filename_has_no_extension) {
					var.errMessage = "No filename extension or file filter selected. "+WHAT_IS_BEING_SAVED+" was not saved.";
				}
				else if (entered_filenames_extension_is_not_supported) {
					var.errMessage = "Unknown filename extension. "+WHAT_IS_BEING_SAVED+" was not saved.";
				}
				else { // entered_filenames_extension_is_valid					
					var.formatName = fileFilter.getDescription();
				}
			}
			
			private void getFormatFromSelectedFilter(Var var) {
				FileFilterExtension selectedFilter = (FileFilterExtension) this.getFileFilter();
				boolean filter_and_entered_filenames_extension_match = selectedFilter.accept(var.givenName);
				boolean entered_filename_has_no_extension = var.givenName.indexOf(".") == -1;
				
				if (filter_and_entered_filenames_extension_match) {
					var.formatName = selectedFilter.getDescription();
				}
				else if (entered_filename_has_no_extension) {
					String newPathName = var.outputFile.getPath().concat(
							"." + selectedFilter.getDescription());
					var.outputFile = new File(newPathName);
					var.formatName = selectedFilter.getDescription();
				}
				else { // entered_filenames_extension_and_selected_filter_do_not_match
					var.errMessage = "Filename extension and file filter did not match. "+WHAT_IS_BEING_SAVED+" was not saved.";
				}
			}

			private void checkIfFileAlreadyExistsAndSaveIt(Var var) {
				boolean file_already_exists = var.outputFile.exists() && getDialogType() == SAVE_DIALOG;
				if (file_already_exists) {
					int result = JOptionPane.showConfirmDialog(this,
							"The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					// Overwrite:
					case JOptionPane.YES_OPTION:
						saveFile(var.formatName, var.outputFile);
						super.approveSelection();
						return;
					// Don't overwrite:
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				saveFile(var.formatName, var.outputFile);
				super.approveSelection();
			}
			
		}; // end of JFileChooser class
	}

}
