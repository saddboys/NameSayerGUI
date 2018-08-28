package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import other.BashWorker;
import other.VideoWorker;
import other.WindowClosedListener;

public class ReplayGUI extends JFrame{
	
	public static ReplayGUI _replayGUI;
	private PlayerGUI _player;
	
	JButton _btnReplay;
	JButton _btnConfirm;
	JButton _reRecord;

	//singleton
	public static void makeReplayGUI(){
		if (_replayGUI == null) {
			_replayGUI = new ReplayGUI();
			_replayGUI.addWindowListener(new WindowClosedListener());
			guiMethods.addFrame(_replayGUI);
		}
	}
	
	public ReplayGUI() {
		buildGUI();
	}
	
	private void buildGUI(){
		setSize(400, 400);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		panel2.setLayout(new FlowLayout());
		
		//replay button
		_btnReplay = new JButton("REPLAY AUDIO");
		_btnReplay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiMethods.resetLocation(_replayGUI);

				new SwingWorker<Void, Void>(){
					@Override
					protected Void doInBackground() throws Exception {
						new PlayerGUI(System.getProperty("user.dir")+"/temp/tempAud.wav");
						return null;
					}
				}.execute();
			}
			
		});

		panel2.add(_btnReplay);
		
		//confirm button
		_btnConfirm = new JButton("CONFIRM");
		_btnConfirm.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				String newCreation = CreateGUI._createGUI.getNewCreation();
				String tempVidcmd = "ffmpeg -y -f lavfi -i color=c=white:s=320x240:d=5 -vf \"drawtext=fontfile=/path/to/font.ttf:fontsize=30:fontcolor=black:x=(w-text_w)/2:y=(h-text_h)/2:text="+newCreation+"\" temp/tempVid.mp4 &> /dev/null\n";
				String combinedVidcmd = "ffmpeg -y -i ./temp/tempVid.mp4 -i ./temp/tempAud.wav -map 0:v -map 1:a \"./creations/"+newCreation+"\".mkv &> /dev/null";
				new BashWorker(tempVidcmd+"\n"+combinedVidcmd){}.execute();

				ListGUI.newCreationsAdded(newCreation);
				guiMethods.resetLocation(_replayGUI);
				guiMethods.displayOnlyThisGUI(MainGUI._mainGUI);
			}
		});
		panel2.add(_btnConfirm);
		
		//record again
		_reRecord = new JButton("RECORD AGAIN");
		_reRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guiMethods.resetLocation(_replayGUI);
				guiMethods.displayOnlyThisGUI(RecordGUI._recordGUI);
			}
		});
		
		panel2.add(_reRecord);

	}
	
}
