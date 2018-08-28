package guis;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class guiMethods {
	
	private static Point _pos;
	private static List<JFrame> frameList= new ArrayList<>();
	
	//adds a new frame to list of frames
	public static void addFrame(JFrame frame) {
		frameList.add(frame);
	}
	
	//changes location of all frames
	public static void resetLocation(JFrame frame) {
		_pos = frame.getLocation();
		
		for(JFrame f: frameList) {
			f.setLocation(_pos);
		}
	}
	
	//displays only one frame
	public static void displayOnlyThisGUI(JFrame frame) {

		for(JFrame f: frameList) {
			if (!frame.equals(f)) {
				f.setVisible(false);
			}
		}
		frame.setVisible(true);
	}
	
	//makes all the frames
	public static void makeGUIs() {
		MainGUI.makeMainGUI();
		ListGUI.makeListGUI();
		CreateGUI.makeCreateGUI();
		RecordGUI.makeRecordGUI();
		ReplayGUI.makeReplayGUI();
		for(JFrame f: frameList) {
				f.setResizable(false);
		}
	}
	
	//returns position
	public static Point getPos() {
		return _pos;
	}
}
