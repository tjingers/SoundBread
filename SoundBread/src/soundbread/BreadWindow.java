package soundbread;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer;


//import javax.sound.sampled.Clip;
import javax.swing.*;

import soundbread.TextPrompt.Show;

import java.io.*;

/**
 * BreadWindow - The heart and soul of the sound board
 * 
 * @author Tanner Ingersoll
 *
 */
public class BreadWindow extends JFrame {

	/**
	 * Whatever man
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO:
	// save config in a file to load later 
	// DONE - JavaFX supports more types - try to get different file formats in the works
	// Pages of buttons
	// real time grid size change maybe
	// Maybe make it so you can sample from the mic to make clips yourself?
	// DONE - add the STOP button
	// Add drag and drop to buttons
	
	// Settings / parameters for window
	private boolean autoMode = true;
	private int rows = 8;
	private int cols = 12;
	// No longer needed since we have a separate panel for the BreadBoxes
//	private int buttonGridOffsetX = 0;
//	private int buttonGridOffsetY = 1;
	private String windowName = "SoundBread ver. 0.2";
	private String currentDirPath = "";
	private File currentDir;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	private GridBagConstraints topBarC;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Components
	private JFXPanel panel;
	private Container topBar;
	private Container grid;
	private TextPrompt directoryBox;
	private JTextField directoryField;
	private JButton loadDirectory;
	private JButton openChooser;
	private JButton stopAll;
	private JButton clearAll;
	private JLabel dirLabel;
	private JCheckBox autoBox;
	private JFileChooser dirChooser;
	private ArrayList<MediaPlayer> clips;
	private BreadBox[][] fields;

	public BreadWindow() throws HeadlessException {
		
		// Initialize window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 800);
		this.setResizable(true);
		this.setTitle(windowName);

		// Set up members
		panel = new JFXPanel();
		fields = new BreadBox[rows][cols];
		clips = new ArrayList<MediaPlayer>();
		layout = new GridBagLayout();
		gbc = new GridBagConstraints();
		panel.setLayout(layout);
		dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		// top bar container
		topBar = new Container();
		topBar.setLayout(new GridBagLayout());
		topBarC = new GridBagConstraints();
		topBarC.fill = GridBagConstraints.HORIZONTAL;
		topBarC.weightx = 1.0;
		topBarC.weighty = .5;

		// Set up the directory box, label, checkbox, open choooser, clear all, and load dir button in topBar
		dirLabel = new JLabel("No current dir");
		directoryField = new JTextField();
		
		directoryBox = new TextPrompt("Enter Dir", directoryField);
		directoryBox.changeAlpha(0.50f);
		directoryBox.setShow(Show.FOCUS_LOST);
		
		loadDirectory = new JButton("Load entered dir");
		// Gross disgusting code for this button, maybe it will work idk
		loadDirectory.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Pressed the directory button");
				currentDirPath = directoryField.getText();
				currentDir = new File(currentDirPath);
				if (currentDir.exists()) {
					System.out.println("Real directory at: " + currentDirPath);
					dirLabel.setText(currentDirPath + "\\");
					
					// Handle auto mode here, if enabled
					if (autoMode) {
						loadDirToGrid(currentDir);
					}
					
				} else {
					System.out.println("Bad directory, retry please");
				}
				
			}
		} );

		openChooser = new JButton("Pick a dir...");
		openChooser.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Show the file chooser dialog for a directory only
				int retVal = dirChooser.showOpenDialog(panel);
				
				if (retVal == JFileChooser.APPROVE_OPTION) {
					currentDir = dirChooser.getSelectedFile();
					dirLabel.setText(currentDir.getAbsolutePath());
					directoryField.setText(currentDir.getAbsolutePath());
					if (autoMode) {
						loadDirToGrid(currentDir);
					}
					System.out.println("Opened a directory with the chooser at: " + currentDir.getAbsolutePath());
					if (currentDir.exists()) {
						System.out.println("And it's a real directory");
					}
				} 
			}
		});
		
		stopAll = new JButton("Stop all media");
		stopAll.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stop all of the clips that exist
				if (!clips.isEmpty()) {
					System.out.println("Stopping all clips...");
					stopAllClips();
					System.out.println("Stopped.");
				} else {
					System.out.println("There are no clips to stop.");
				}
				
			}
		});
		
		clearAll = new JButton("Clear all media");
		clearAll.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// invoke the loadDir with null to clear grid
				System.out.println("Clearing all media from grid...");
				loadDirToGrid(null);
				//stopAllClips();
			}
		});
		
		autoBox = new JCheckBox("Auto load files");
		autoBox.setSelected(autoMode);
		autoBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Read off the state of the check box into autoMode when action'd
				autoMode = autoBox.isSelected();
				if (autoMode) {
					System.out.println("Auto mode enabled");
				} else {
					System.out.println("Auto mode disabled");
				}
			}
		});
		
		// Format and add components to top bar
		topBarC.gridx = 0;
		topBarC.gridy = 1;
		topBarC.weightx = 0.4;
		topBar.add(dirLabel, topBarC);

		topBarC.gridx = 1;
		topBarC.weightx = 1.0;
		topBar.add(directoryField, topBarC);

		topBarC.gridx = 2;
		topBarC.weightx = 0.2;
		topBar.add(stopAll, topBarC);
		
		topBarC.gridx = 3;
		topBar.add(clearAll, topBarC);
		
		topBarC.gridx = 2;
		topBarC.gridy = 0;
		topBarC.weightx = 0.2;
		topBar.add(loadDirectory, topBarC);
		
		topBarC.gridx = 3;
		topBarC.weightx = 0.2;
		topBar.add(openChooser, topBarC);
		
		topBarC.gridx = 4;
		topBarC.weightx = 0.05;
		topBar.add(autoBox, topBarC);

		// Add top bar to panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0.05;
		panel.add(topBar, gbc);
		
		// Format and add components to the grid
		grid = new Container();
		grid.setLayout(new GridLayout(rows, cols));
		
		// Looping to set up an arbitrary amount of boxes in grid
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				// Fill the window with BreadBoxes
				fields[i][j] = new BreadBox(j, i, this);
				grid.add(fields[i][j]);
				
			}// for j, cols
		} // for i, rows
		
		// Add grid to panel
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		panel.add(grid, gbc);
		
		// Go live
		this.add(panel);
		panel.setVisible(true);
		//this.pack();
		this.setLocation(dim.width/2 - this.getSize().width/2, (int)(dim.width/3.5) - this.getSize().width/2);
		this.setVisible(true);
		
		// Welcome message
		//JOptionPane.showMessageDialog(new JFrame(), "Welcome to the SoundBread Version 0.1");

	}

	public BreadWindow(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public BreadWindow(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public BreadWindow(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	// Begin non-default methods
	public String getCurrentDirPath() {
		return currentDirPath;
	}
	
	private void loadDirToGrid(File dir) {
		// Load files from the directory that are either .mp3 or .wav into BreadBoxes
		File[] inDir;
		// Made it null-safe, so also able to clear the whole grid (null argument)
		if (dir != null) {
			inDir = dir.listFiles();
		} else {
			inDir = new File[0];
		}
		int k = 0;
		for (BreadBox[] bRow:fields) {
			for (BreadBox b:bRow) {
				if (inDir.length > k) {
					if (inDir[k].getName().endsWith(".mp3") || inDir[k].getName().endsWith(".wav")) {
						System.out.println("Loaded a file: " + inDir[k].getName());
						b.setFile(inDir[k]);
					} 
					b.getButton().setBackground(null);
					k++;
				} else {
					b.getButton().setText("None");
					b.getButton().setBackground(null);
					b.setFile(null);
				}
				
			}
		}
	}
	
	// Allows the media to self-regulate if they so decide to behave
	public void addMedia(MediaPlayer m) {
		clips.add(m);
	}
	
	public void removeMedia(MediaPlayer m) {
		clips.remove(m);
		System.out.println("Media removed from media array");
	}
	
	public void stopAllClips() {
		for (MediaPlayer c:clips) {
			c.stop();
		}
		clips.clear();
		// To make sure the colors go back to normal, since it's difficult to think of 
		// 	how I could get a handle on what BreadBox played that media.
		for (BreadBox[] bRow:fields) {
			for (BreadBox b:bRow) {
				b.getButton().setBackground(null);
			}
		}
	}
	// End non-default methods
}
