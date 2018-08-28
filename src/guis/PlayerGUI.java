package guis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import other.DisposePlayerEventListener;
import other.WindowClosedListener;
import other.WindowPlayerStopListener;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerGUI extends JFrame{
	
	private EmbeddedMediaListPlayerComponent _mediaPlayer;
	private JButton _pause = new JButton ("PAUSE");
	EmbeddedMediaPlayer _player;
	JPanel panelN = new JPanel();
	JPanel panelS = new JPanel();
	private boolean _paused = false;
	
	public PlayerGUI(String toplay) {
		//stop player when window is closed
		this.addWindowListener(new WindowPlayerStopListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				_player.stop();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				_player.stop();
			}
		});
		
		//video player
		_mediaPlayer = new EmbeddedMediaListPlayerComponent() {

			public void finished(MediaPlayer arg0) {
				_player.stop();
				disposeFrame();
				removeAll();
			}

		};
		
		
		this.add(_mediaPlayer);
		this.setBounds(100, 100, 600, 500);
		
		//ensure pause button works
		_pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!_paused) {
					_player.pause();
					_paused = true;
					_pause.setText("PLAY");
				} else {
					_player.play();
					_paused = false;
					_pause.setText("PAUSE");
				}
			}
			
		});
		
		panelS.add(_pause);

		this.add(panelN,BorderLayout.NORTH);
		this.add(panelS,BorderLayout.SOUTH);
		
		//play for 5.5 seconds where guis are located
		setLocation(guiMethods.getPos());
		setVisible(true);
		play(toplay);
		

	}
	
	//plays file input
	public void play(String file) {
		_player = _mediaPlayer.getMediaPlayer();
		panelN.add(_mediaPlayer);
		_player.playMedia(file);
		
	}
	
	public void disposeFrame() {
		this.dispose();
	}
}
