package be.planetsizebrain.inspectorbinks.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import be.planetsizebrain.inspectorbinks.InspectorBinks;
import be.planetsizebrain.inspectorbinks.swing.action.OpenArchiveAction;

import com.explodingpixels.macwidgets.BottomBar;
import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.MacWidgetFactory;

public class ApplicationFrame extends JFrame {
	
	private static final Dimension FRAME_SIZE = new Dimension(800, 600);
	
	private FileListing listing = new FileListing(this);
	
	public ApplicationFrame() {
		setTitle(InspectorBinks.NAME);
		setSize(FRAME_SIZE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		getRootPane().setOpaque(false);
		getRootPane().setBackground(Color.WHITE);
		getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
			
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new ApplicationToolbar(), BorderLayout.NORTH);
//		contentPane.add(new DropPanel(), BorderLayout.CENTER);
		contentPane.add(listing, BorderLayout.CENTER);
		
		BottomBar bottomBar = new BottomBar(BottomBarSize.LARGE);
		bottomBar.addComponentToCenter(MacWidgetFactory.createEmphasizedLabel("No archive opened yet."));
		contentPane.add(bottomBar.getComponent(), BorderLayout.SOUTH);

		setJMenuBar(createMenuBar());
	}
	
	private JMenuBar createMenuBar() {
		int commandModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		JMenuBar menubar = new JMenuBar();
		 
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem(new OpenArchiveAction(this, listing));
		open.setText("Open");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, commandModifier));
		open.setMnemonic(KeyEvent.VK_O);
		file.add(open);
		
		menubar.add(file);
		
		return menubar;
	}
}