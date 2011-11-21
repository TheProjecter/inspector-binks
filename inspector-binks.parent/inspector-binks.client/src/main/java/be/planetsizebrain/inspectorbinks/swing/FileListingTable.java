package be.planetsizebrain.inspectorbinks.swing;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class FileListingTable extends JTable {

	public FileListingTable(FileListingTableModel model) {
		super(model);
		
		this.putClientProperty("Quaqua.Table.style", "striped");
		
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		setDragEnabled(true);
		
		// Let's use this "component drag handler" thingNo archive loaded
//		ComponentDragHandler transferHandler = new ComponentDragHandler(this, DataFlavor.javaFileListFlavor) {
//			private static final long serialVersionUID = 1L;
//
//			// Now here comes the magic 
//			@Override
//			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
//				ArrayList<File> files = new ArrayList<File>(); 
//				
//				return files; 
//			}
//		}; 
//		
		// Don't forget this one! 
//		setTransferHandler(transferHandler);  
	}
	
	public boolean isCellEditable(int row, int col) {  
		return false;  
    }
	
	public Dimension getPreferredScrollableViewportSize() {
	    Dimension size = super.getPreferredScrollableViewportSize();
	    return new Dimension(Math.min(getPreferredSize().width, size.width), size.height);
	}
}