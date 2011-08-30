package be.planetsizebrain.inspectorbinks.swing.osx;

import javax.swing.JOptionPane;

import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;

public class ApplicationAdapter implements ApplicationListener {
	
	public void handleReOpenApplication(ApplicationEvent event) {}
	
	public void handleQuit(ApplicationEvent event) {
		System.exit(0);
	}
	
	public void handlePrintFile(ApplicationEvent event) {}
	
	public void handlePreferences(ApplicationEvent event) {
		JOptionPane.showMessageDialog(null, "Show Preferences dialog here");
	}
	
	public void handleOpenFile(ApplicationEvent event) {}
	
	public void handleOpenApplication(ApplicationEvent event) {}
	
	public void handleAbout(ApplicationEvent event) {
		JOptionPane.showMessageDialog(null, "Show About dialog here");
		event.setHandled(true);
	}
}