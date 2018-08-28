package guis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//pops up to confirm overwriting a creation
public class CreationPopupGUI extends JFrame{
	JButton _yes = new JButton("YES");
	JButton _no = new JButton("BACK");
	JLabel _label = new JLabel("THIS CREATION ALREADY EXISTS, OVERWRITE?");
	
	private static String _name;
	public static CreationPopupGUI _popup;
	//create popup over gui
	public static void makePopupGUI(String name){
		//delete already existing gui
		if (_popup!=null) {
			_popup.dispose();
		}
		
		//create popup over current gui
		guiMethods.resetLocation(CreateGUI._createGUI);
		_popup=new CreationPopupGUI();
		_popup.setLocation(guiMethods.getPos());
		_popup.setVisible(true);
		_name = name;
		
	}
	
	private CreationPopupGUI() {
		//initial setup
		setSize(400, 200);
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		setLayout(new BorderLayout());
		add(panel1, BorderLayout.NORTH);
		add(panel2,BorderLayout.SOUTH);
		
		//add yes button
		_yes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListGUI.creationsRemove(_name);
				CreateGUI._createGUI.setCreationName(_name);
				guiMethods.resetLocation(CreateGUI._createGUI);
				guiMethods.displayOnlyThisGUI(RecordGUI._recordGUI);
				
				_popup.dispose();
				_popup=null;
			}

		});
		panel2.add(_yes );
		
		//add no button
		_no.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_popup.dispose();
				_popup=null;
			}

		});
		panel2.add(_no );
		panel1.add(_label);
	}
}
