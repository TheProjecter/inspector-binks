package be.planetsizebrain.inspectorbinks;

import info.growl.Growl;
import info.growl.GrowlException;
import info.growl.GrowlUtils;

import java.awt.EventQueue;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.DefaultApplication;

import be.planetsizebrain.inspectorbinks.swing.ApplicationFrame;
import be.planetsizebrain.inspectorbinks.swing.osx.ApplicationAdapter;
import ch.randelshofer.quaqua.QuaquaManager;

public class InspectorBinks {
	
	public static final String NAME = "Inspector Binks";
	
	private static final boolean IS_MAC;
	
	private static final Growl GROWL;
	private static final RenderedImage JARJAR;
	
	static {
		String osName = System.getProperty("os.name").toLowerCase();
		IS_MAC = osName.startsWith("mac os x");
		
		try {
			JARJAR = ImageIO.read(InspectorBinks.class.getResourceAsStream("/jarjar-icon.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new Error();
		}
		GROWL = GrowlUtils.getGrowlInstance(NAME);
		GROWL.addNotification(NAME, true);
		try {
			GROWL.register();
		} catch (GrowlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (IS_MAC) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", NAME);
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
			System.setProperty("com.apple.mrj.application.live-resize", "true");
			System.setProperty("com.apple.macos.smallTabs", "true");
			System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
			
			Application application = new DefaultApplication();
			application.addApplicationListener(new ApplicationAdapter());
			
			try { 
				UIManager.setLookAndFeel(QuaquaManager.getLookAndFeel());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ApplicationFrame().setVisible(true);
			}
		});
	}
	
	public static final void growl(String message) {
		try {
			GROWL.sendNotification(NAME, NAME, message, JARJAR);
		} catch (GrowlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}