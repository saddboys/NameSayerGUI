package guis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import other.BashWorker;

//pops up to confirm overwriting a creation
public class DeletePopUpGUI extends JFrame{
	JButton _yes = new JButton("YES");
	JButton _no = new JButton("BACK");
	JLabel _label = new JLabel();
	
	private static String _name;
	public static DeletePopUpGUI _popup;
	
	public static void makePopupGUI(String name){
		//delete already existing gui
		if (_popup!=null) {
			_popup.dispose();
		}
		
		//create popup over gui
		guiMethods.resetLocation(CreateGUI._createGUI);
		_name = name;
		_popup=new DeletePopUpGUI();
		_popup.setLocation(guiMethods.getPos());
		_popup.setVisible(true);
		
	}
	
	private DeletePopUpGUI() {
		//initial setup
		setSize(400, 200);
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		setLayout(new BorderLayout());
		add(panel1, BorderLayout.NORTH);
		add(panel2,BorderLayout.SOUTH);
		
		_label.setText("CONFIRM DELETE: " + _name);
		//add yes button
		_yes.addActionListener(new ActionListener() {
			@Override
			//deletes file in case of yes
			public void actionPerformed(ActionEvent e) {
				if (ListGUI._listGUI._click&(ListGUI._listGUI._creations.size()!=0)) {
					ListGUI._listGUI._click = false;
					new BashWorker("rm -f creations/\""+_name+"\".mkv").execute();
					ListGUI.creationsRemove(_name);
					ListGUI._listGUI.removeThumbnail();
				}
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