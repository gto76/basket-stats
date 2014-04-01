package si.gto76.basketstats.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveListener implements ActionListener {
	SwinGui mainFrame;

	public SaveListener(SwinGui mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	private void saveFile(String formatName, File outputFile) {
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(outputFile));
			out.print(mainFrame.game.toString());
			out.close();
			mainFrame.stateChangedSinceLastSave = false;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "SAVE ERROR!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent e) {

		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = 291238218189760373L;

			@Override
			public void approveSelection() {

				String formatName = "";
				String errMessage = "";
				File outputfile = this.getSelectedFile();
				final String givenName = outputfile.getName();

				// Filename Test:
				// No filter chosen
				if (this.getFileFilter().getDescription() == "All Files") {
					ExtensionFileFilter fileFilter = ExtensionFileFilter
							.getFilter(givenName);
					// No extension given - ERR
					if (givenName.indexOf(".") == -1) {
						errMessage = "No filename extension or file filter selected. Image was not saved.";
					}
					// No file filter matches the extension - ERR
					else if (fileFilter == null) {
						errMessage = "Unknown filename extension. Image was not saved.";
					}
					// Valid extension - OK
					else {
						formatName = fileFilter.getDescription();
					}
				}
				// Filter selected
				else {
					ExtensionFileFilter selectedFilter = (ExtensionFileFilter) this
							.getFileFilter();
					// Filter and extension match - OK
					if (selectedFilter.accept(givenName)) {
						formatName = selectedFilter.getDescription();
					}
					// No extension given - OK
					else if (givenName.indexOf(".") == -1) {
						String newPathName = outputfile.getPath().concat(
								"." + selectedFilter.getDescription());
						outputfile = new File(newPathName);
						formatName = selectedFilter.getDescription();
					}
					// Extension does not match selected filter - ERR
					else {
						errMessage = "Filename extension and file filter did not match. Image was not saved.";
					}
				}

				// If it passed the test above -> Filename was valid and passed
				// trough filter.
				if (formatName != "") {
					// ********************************************//
					// File already exists check:
					if (outputfile.exists() && getDialogType() == SAVE_DIALOG) {
						int result = JOptionPane.showConfirmDialog(this,
								"The file exists, overwrite?", "Existing file",
								JOptionPane.YES_NO_CANCEL_OPTION);
						switch (result) {
						case JOptionPane.YES_OPTION:

							// SAVE (overwrite existing file)
							saveFile(formatName, outputfile);

							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CLOSED_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
						}
					}

					// SAVE (new file)
					saveFile(formatName, outputfile);

					super.approveSelection();
					return;
				}
				// If it didn't pass the test from above -> Filename wasn't
				// valid or it didn't pass trough filter.
				else {
					JOptionPane.showMessageDialog(null, errMessage, "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

			}
		};

		String fileName = mainFrame.game.getTeam1().getName() + " vs "
				+ mainFrame.game.getTeam2().getName() + " " + mainFrame.game.getDate().toString();
		fc.setSelectedFile(new File(fileName));
		fc.setDialogTitle("Save As");

		for (ExtensionFileFilter filter : ExtensionFileFilter.all) {
			fc.addChoosableFileFilter(filter);
		}
		fc.setFileFilter(ExtensionFileFilter.all[0]);

		// OPEN DIALOG
		fc.showSaveDialog(mainFrame.frame);
	}

}
