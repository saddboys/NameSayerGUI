package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import other.WindowClosedListener;

public class MainGUI extends JFrame{
	
	public static MainGUI _mainGUI;
	
	//singleton
	public static void makeMainGUI() {
		if (_mainGUI == null) {
			_mainGUI = new MainGUI();
			_mainGUI.addWindowListener(new WindowClosedListener());
			guiMethods.addFrame(_mainGUI);
		}
	}
	
	private MainGUI() {
		super("Name Sayer");
		buildGUI();
		
	}
	
	//builds the gui for the menu
	private void buildGUI() {
		//initial setup
		setSize(400, 400);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//setup button for list
		JButton btnList = new JButton("LIST/PLAY/DELETE");
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guiMethods.resetLocation(_mainGUI);
				guiMethods.displayOnlyThisGUI(ListGUI._listGUI);
			}
		});
		panel2.add(btnList);

		//setup intro label
		panel1.add(new JLabel("Welcome to name sayer!") {
			Font font = new Font("Calibri",Font.BOLD, 10);
		}, BorderLayout.NORTH);
		
		
		//setup creation button
		JButton btnCreate = new JButton("CREATE");
		btnCreate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiMethods.resetLocation(_mainGUI);
				guiMethods.displayOnlyThisGUI(CreateGUI._createGUI);
			}
			
		});
		
		//layout
		panel2.setLayout(new FlowLayout());
		panel2.add(btnCreate);
	}


}
