package be.planetsizebrain.inspectorbinks.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class ApplicationToolbar extends JToolBar {
	
	public ApplicationToolbar() {
		setLayout(new BorderLayout());
		// Needed for unified toolbar look
		putClientProperty("Quaqua.ToolBar.style", "title");
		setFloatable(false);
		
		addOpenButton();
		addSearchField();
	}
	
	private void addOpenButton() {
		ImageIcon icon = new ImageIcon(ApplicationToolbar.class.getResource("/folder32-black.png"));
		JButton open = new JButton(icon);
//		open.setAction(action);
		this.add(open, BorderLayout.WEST);
		open.setIcon(new ImageIcon(ApplicationToolbar.class.getResource("/folder32-black.png")));
//		open.setRolloverIcon(new ImageIcon(ApplicationToolbar.class.getResource("/folder32-white.png")));
		open.setToolTipText("Open archive");
	}
	
	private void addSearchField() {
		JPanel panel = new JPanel();
		this.add(panel, BorderLayout.EAST);
		
		JTextField search = new JTextField();
		search.putClientProperty("Quaqua.TextField.style","search");
		search.setPreferredSize(new Dimension(150, 25));
		search.setMaximumSize(new Dimension(150, 25));
		panel.add(search);
	}
}