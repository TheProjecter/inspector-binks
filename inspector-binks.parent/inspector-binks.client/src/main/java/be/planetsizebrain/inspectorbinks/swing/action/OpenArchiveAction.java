package be.planetsizebrain.inspectorbinks.swing.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import be.planetsizebrain.inspectorbinks.swing.FileListing;

public class OpenArchiveAction extends AbstractAction {

	private JFrame parent;
	private FileListing listing;
	
	public OpenArchiveAction(JFrame parent, FileListing listing) {
		this.parent = parent;
		this.listing = listing;
	}
	
	public void actionPerformed(ActionEvent e) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "JAR|WAR|EAR files";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || (f.isFile() && f.getName().endsWith(".jar"));
			}
		});
		
		int result = fileChooser.showDialog(parent, "Open");
		if (JFileChooser.APPROVE_OPTION == result) {
			listing.loadArchive(fileChooser.getSelectedFile());
		}
	}
}