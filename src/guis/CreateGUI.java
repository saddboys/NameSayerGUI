package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import other.WindowClosedListener;

//gui for new creations
public class CreateGUI extends JFrame{
	
	public static CreateGUI _createGUI;
	protected JPanel _panelS = new JPanel();
	protected JPanel _panelN = new JPanel();
	protected JPanel _panelM = new JPanel();
	protected JTextField _textIn;
	JButton _btnConfirm;
	String _newCreation;
	JLabel _inputInstruct;
	JLabel _inputError;
	
	//singleton
	public static void makeCreateGUI() {
		if (_createGUI == null) {
			_createGUI = new CreateGUI();
			_createGUI.addWindowListener(new WindowClosedListener());
			guiMethods.addFrame(_createGUI);
		}
	}
	
	private CreateGUI() {
		super("New Creation");
		buildGUI();
	}

	//build the gui
	private void buildGUI() {
		
		setSize(400,400);
		add(_panelN, BorderLayout.NORTH);
		add(_panelM, BorderLayout.CENTER);
		add(_panelS, BorderLayout.SOUTH);
		_panelS.setLayout(new FlowLayout());
		
		//back to menu button
		JButton btnBack = new JButton("BACK TO MENU");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiMethods.resetLocation(_createGUI);
				guiMethods.displayOnlyThisGUI(MainGUI._mainGUI);
			}
			
		});
		
		//confirm name button
		_btnConfirm = new JButton("CONFIRM AND CONTINUE");
		_btnConfirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String stringsAllowed = "[a-zA-z0-9_ -]+";
				String creationName = _textIn.getText();
				boolean exists = ListGUI.creationAlreadyExists(creationName);
				guiMethods.resetLocation(_createGUI);
				//check if name doesnt contain "." or is not too long
				if(!creationName.matches(stringsAllowed)|creationName.contains("[")|creationName.contains("]")){
					_inputError.setText("The name cannot have some characters in the name");
				}else if (creationName.length() > 20){
					_inputError.setText("The name cannot be more than 20 characters");
				}else if (creationName.length() == 0){
					_inputError.setText("The name cannot be empty");
				//check if name already used
				}else if (exists){
					CreationPopupGUI.makePopupGUI(creationName);
				//make new creation
				}else{
					_newCreation = creationName;
					guiMethods.resetLocation(_createGUI);
					guiMethods.displayOnlyThisGUI(RecordGUI._recordGUI);
				}
			}
			
		});
		_panelS.add(_btnConfirm);
		
		_panelN.setLayout(new BoxLayout(_panelN, BoxLayout.Y_AXIS));
		String creation = _newCreation;
		_newCreation = null;
		
		//instructions label
		_inputInstruct = new JLabel("<html>Please enter your desired name for the creation<br/>with only letters, digits, underscores and hyphens and spaces in the name chosen, 20 characters max</html>");
		_inputInstruct.setAlignmentX(0);
		_panelN.add(_inputInstruct);
		
		//error label
		_inputError = new JLabel();
		_panelN.add(_inputError);
	
		//enter creation name text area
		_textIn = new JTextField();
		_textIn.setColumns(25);
		_panelM.add(_textIn);
		
		_panelS.setLayout(new FlowLayout());
		_panelS.add(btnBack);
		
	}
	public String getNewCreation() {
		return _newCreation;
	}
	public void setCreationName(String str) {
		_newCreation = str;
	}
	
}
