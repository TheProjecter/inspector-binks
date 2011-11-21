package be.planetsizebrain.inspectorbinks.swing;

import java.io.File;

import javax.swing.table.DefaultTableModel;

import de.schlichtherle.truezip.file.TFile;

public class FileListingTableModel extends DefaultTableModel {

	private static String[] COLUMN_NAMES = new String[] {"Name", "Size"};
	
	private static final int INDEX_NAME_COLUMN = 0;
	private static final int INDEX_SIZE_COLUMN = 1;
	
	public FileListingTableModel() {
		setDataVector(new String[][] {{"No archive loaded",}}, new String[] {""});
	}
	
	public FileListingTableModel(File root, boolean isRoot) {
		TFile tRoot = new TFile(root);
    	
		File[] files = tRoot.listFiles();
		Object[][] data = new Object[files.length + (isRoot ? 0 : 1)][COLUMN_NAMES.length];
		if (!isRoot) {
			data[0][INDEX_NAME_COLUMN] = "..";
			data[0][INDEX_SIZE_COLUMN] = "";
		}
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			data[i + (isRoot ? 0 : 1)][INDEX_NAME_COLUMN] = f.getName();
			data[i + (isRoot ? 0 : 1)][INDEX_SIZE_COLUMN] = f.length();
		}
		
		setDataVector(data, COLUMN_NAMES);
	}
}