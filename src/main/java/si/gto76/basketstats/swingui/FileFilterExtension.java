package si.gto76.basketstats.swingui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import si.gto76.basketstats.Conf;

/**
 * Accepts files that end in one of the given extensions.
 */
public final class FileFilterExtension extends FileFilter {
	private final String description;
	private final String[] extensions;

	static final String[] hsgExtensions = { "."+Conf.FILE_EXTENSION };
	public static final FileFilterExtension hsg = new FileFilterExtension(
			Conf.FILE_EXTENSION, hsgExtensions);

	public static final FileFilterExtension[] all = { hsg };

	/**
	 * Returns matching filter for a filename, null if none match.
	 */
	public static FileFilterExtension getFilterOrNull(String fileName) {
		for (FileFilterExtension filter : all) {
			if (filter.accept(fileName)) {
				return filter;
			}
		}
		return null;
	}

	/**
	 * Creates a new ExtensionFileFilter.
	 * 
	 * @param description
	 *            a description of the filter.
	 * @param extensions
	 *            an array of acceptable extensions.
	 */
	public FileFilterExtension(String description, String[] extensions) {
		this.description = description;
		this.extensions = extensions;
	}

	// Implementations for FileFilter

	public String getDescription() {
		return description;
	}

	public boolean accept(String fileName) {
        for (String extension : extensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
		return false;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		String fileName = file.getName().toLowerCase();
		return accept(fileName);
	}
	
}
