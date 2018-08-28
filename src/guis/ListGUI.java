package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import other.BashWorker;
import other.WindowClosedListener;

public class ListGUI extends JFrame {

	public static ListGUI _listGUI;

	private int _elementIndex = -1;
	protected boolean _click = false;
	private static DefaultListModel<String> _listModel = new DefaultListModel<String>();
	protected static List<String> _creations = new ArrayList<>();

	private static JList<String> _list;

	private JPanel panelS = new JPanel();
	private JPanel panelNW = new JPanel();
	private JPanel panelNE = new JPanel();
	private JPanel panelN = new JPanel();
	private JLabel _imageLabel;


	// singleton
	public static void makeListGUI() {
		if (_listGUI == null) {
			_listGUI = new ListGUI();
			_listGUI.addWindowListener(new WindowClosedListener());
			guiMethods.addFrame(_listGUI);
		}
		updateList();
	}

	private ListGUI() {
		super("Listing Creations");
		buildGUI();
	}

	private void buildGUI() {

		// initial setup
		setSize(400, 400);

		add(panelN, BorderLayout.NORTH);
		panelN.add(panelNW, BorderLayout.WEST);
		panelN.add(panelNE, BorderLayout.EAST);
		add(panelS, BorderLayout.SOUTH);
		panelS.setLayout(new FlowLayout());

		// build list
		_list = new JList<String>(_listModel);
		_list.setLayoutOrientation(JList.VERTICAL);
		_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (_creations.size() > 0) {

					JList<String> list = (JList<String>) event.getSource();
					_elementIndex = list.locationToIndex(event.getPoint());
					_click = true;
					generateThumbnail(_creations.get(_elementIndex));
				}
			}
		});
		panelNW.add(_list);

		// build scroll panel
		JScrollPane listScroller = new JScrollPane(_list);
		listScroller.setPreferredSize(new Dimension(200, 300));
		panelNW.add(listScroller);

		// setup thumbnail image label
		_imageLabel = new JLabel();
		_imageLabel.setPreferredSize(new Dimension(130, 90));
		panelNE.add(_imageLabel);

		// setup buttons
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_click) {
					guiMethods.resetLocation(_listGUI);
					if (_creations.size() > 0) {
					DeletePopUpGUI.makePopupGUI(_creations.get(_elementIndex));
					}
				}
			}

		});
		panelS.add(btnDelete);

		// play button
		JButton btnPlay = new JButton("PLAY");
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (_click) {
					guiMethods.resetLocation(_listGUI);
					// swingworker to play file
					new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							String play = System.getProperty("user.dir") + "/creations/" + _creations.get(_elementIndex)
									+ ".mkv";
							new PlayerGUI(play);
							return null;
						}
					}.execute();
				}
			}
		});
		panelS.add(btnPlay);

		// back to menu button
		JButton btnBack = new JButton("BACK TO MENU");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiMethods.resetLocation(_listGUI);
				guiMethods.displayOnlyThisGUI(MainGUI._mainGUI);
				_click = false;

			}

		});
		panelS.add(btnBack);
	}

	// update the list contents
	public static void updateList() {
		_listModel.removeAllElements();
		_creations.clear();

		File creationFile = new File(System.getProperty("user.dir") + "/creations");
		ArrayList<File> creations = new ArrayList<File>(Arrays.asList(creationFile.listFiles()));

		//add list contents to swing list
		for (File creation : creations) {
			String creationName = creation.getName().replaceFirst("[.][^.]+$", "");

			_listModel.addElement(creationName);
			_creations.add(creationName);
		}
	}

	// puts the thumbnail into the gui
	public static void getImage() {
		BufferedImage myImage;
		ImageIcon icon;
		try {
			while (!new File("./thumb/thumb.jpg").exists()) {
			}
			myImage = ImageIO.read(new File("./thumb/thumb.jpg"));
			icon = new ImageIcon(myImage);
			Image scaleImage = icon.getImage().getScaledInstance(130, 90, Image.SCALE_DEFAULT);

			_listGUI._imageLabel.setIcon(new ImageIcon(scaleImage));
			_listGUI._imageLabel.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			//the only way to get here is clicking too fast
		}
	}

	// generates the thumbnail using bash
	public static void generateThumbnail(String creation) {

		//create thumbnail
		String cmd = "ffmpeg -y -i ./creations/\"" + creation
				+ ".mkv\" -vcodec mjpeg -vframes 1 -an -f rawvideo -ss `ffmpeg -i ./creations/\"" + creation
				+ ".mkv\" 2>&1 | grep Duration | awk '{print $2}' | tr -d , | awk -F ':' '{print ($3+$2*60+$1*3600)/2}'` ./thumb/\""
				+ "thumb" + "\".jpg";
		new BashWorker(cmd) {
			@Override
			public Void doInBackground() {
				try {
					if (new File("./thumb/thumb.jpg").exists()) {
						new File("./thumb/thumb.jpg").delete();
					}
					ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
					Process process = builder.start();
					// wait for the thumbnail to be generated
					while (!new File("./thumb/thumb.jpg").exists()) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void done() {
				// put image into gui
				getImage();

			}
		}.execute();

	}

	// add a creation to the model
	public static void newCreationsAdded(String creation) {
		_listModel.addElement(creation);
		_creations.add(creation);
	}

	// remove a creation from the model
	public static void creationsRemove(String creation) {
		_listModel.removeElement(creation);
		_creations.remove(creation);
	}

	// check if creation exists
	public static boolean creationAlreadyExists(String creation) {
		for (String s : _creations) {
			if (creation.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	//delete image from thumbnail
	public void removeThumbnail() {
		_imageLabel.setIcon(null);
	}
}
