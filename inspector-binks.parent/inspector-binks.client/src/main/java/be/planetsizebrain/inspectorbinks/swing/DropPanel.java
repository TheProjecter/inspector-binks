package be.planetsizebrain.inspectorbinks.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class DropPanel extends JPanel implements DropTargetListener {

	private static final ImageIcon NORMAL = new ImageIcon(ApplicationToolbar.class.getResource("/airdrop-os-x-lion-icon-normal.png"));
	private static final ImageIcon LARGE = new ImageIcon(ApplicationToolbar.class.getResource("/airdrop-os-x-lion-icon.png"));
	
	private JLabel target;
	private JTable table;
	
	public DropPanel(JTable table) {
		super(new BorderLayout());
		this.table = table;
		this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true));
		
		target = new JLabel(NORMAL);
		this.add(target, BorderLayout.CENTER);
		
//		table = new JTable(new Object[][]{{"test"}},new Object[][]{{"test"}});
		table.putClientProperty("Quaqua.Table.style", "striped");
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		target.setIcon(LARGE);
		System.out.println("ENTER");
	}

	public void dragExit(DropTargetEvent dte) {
		target.setIcon(NORMAL);
		System.out.println("EXIT");
	}

	public void drop(DropTargetDropEvent dtde) {
		target.setIcon(NORMAL);
		System.out.println("DROP");
		
		this.remove(target);
		this.add(table, BorderLayout.CENTER);
	}

	public void dragOver(DropTargetDragEvent dtde) {}
	public void dropActionChanged(DropTargetDragEvent dtde) {}
}