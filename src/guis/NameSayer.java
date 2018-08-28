
package guis;

import javax.swing.SwingUtilities;

import other.Initialise;

public class NameSayer {
	
	public static void main(String[] args){
		Initialise.makefolders();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiMethods.makeGUIs();
				MainGUI._mainGUI.setVisible(true);
			}
		});
		
		
		

	}
}
