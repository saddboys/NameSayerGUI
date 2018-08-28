package guis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import other.BashWorker;
import other.WindowClosedListener;

public class RecordGUI extends JFrame {
	
	public static final int RECORDING_TIME = 5000;
	
	private JButton _btnRecord;
	private JLabel lblProgress;
	
	public static RecordGUI _recordGUI;
	
	private boolean _recorded;
	private boolean _recording;
	
	//singleton
	public static void makeRecordGUI(){
		if (_recordGUI == null) {
			_recordGUI = new RecordGUI();
			_recordGUI.addWindowListener(new WindowClosedListener());
			guiMethods.addFrame(_recordGUI);
		}
	}
	
	public RecordGUI() {
		_recorded = false;
		buildGUI();
	}
	
	
	private void buildGUI() {
		//initial setup
		setSize(400, 400);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		panel2.setLayout(new FlowLayout());
		
		lblProgress = new JLabel();
		
		//recording button
		_btnRecord = new JButton("BEGIN RECORDING");
		_btnRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				
				if(!_recorded&!_recording) {
					_recording = true;
					new SwingWorker<Void, Integer>(){
						@Override
						protected Void doInBackground() throws Exception {
							//rename button
							_btnRecord.setText("RECORDING....");
							//start recording
							new BashWorker("ffmpeg -y -f alsa -i hw:0 -t 00:00:05 temp/tempAud.wav &> /dev/null").execute();
							
							try {
								for(int seconds = (RECORDING_TIME/1000); seconds >0; seconds--) {
									publish(seconds);
									Thread.sleep(1000);
								}

							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							//rename button
							_btnRecord.setText("DONE, CONTINUE");
							return null;
						}
						//update countdown
						public void process(List<Integer> seconds) {
							lblProgress.setText(seconds.get(seconds.size()-1)+"");
						}
						//change state to finish recording
						protected void done() {
							_recorded = true;
							_recording = false;
							lblProgress.setText("0");
						}
					}.execute();
				}
				//reset to initial state
				if (_recorded&!_recording) {
					_recorded = false;
					guiMethods.resetLocation(_recordGUI);
					guiMethods.displayOnlyThisGUI(ReplayGUI._replayGUI);
					_btnRecord.setText("BEGIN RECORDING");
				}
			}
		});
		
		panel2.add(_btnRecord);
		panel2.add(lblProgress);
	}
}
