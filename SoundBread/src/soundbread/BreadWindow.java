package soundbread;

import java.awt.*;
import java.awt.event.*;

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
	// auto mode to load first x files in dir
	// save config in a file to load later
	// get it to play sounds
	// try to get different file formats in the works
	
	// Settings / parameters for window
//	private boolean autoMode = false;
	private int rows = 4;
	private int cols = 4;
	private int buttonGridOffsetX = 0;
	private int buttonGridOffsetY = 1;
	private String windowName = "SoundBread ver. 0.2";
	private String currentDirPath = "";
	private File currentDir;
	private GridBagLayout layout;
	private GridBagConstraints c;
	private GridBagConstraints topBarC;
	private GridBagConstraints gridC;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	// Components
	private Container panel;
	private Container topBar;
	private Container grid;
	private TextPrompt directoryBox;
	private JTextField directoryField;
	private JButton loadDirectory;
	private JLabel dirLabel;
	private BreadBox[][] fields;

	public BreadWindow() throws HeadlessException {
		
		// Initialize window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 800);
		this.setResizable(true);
		this.setTitle(windowName);

		// Set up members
		panel = this.getContentPane();
		fields = new BreadBox[rows][cols];
		layout = new GridBagLayout();
		c = new GridBagConstraints();
		panel.setLayout(layout);
		
		// top bar container
		topBar = new Container();
		topBar.setLayout(new GridBagLayout());
		topBarC = new GridBagConstraints();
		topBarC.fill = GridBagConstraints.HORIZONTAL;
		topBarC.weightx = 1.0;
		topBarC.weighty = .5;

		// Set up the directory box and label, and button in topBar
		dirLabel = new JLabel("No current dir");
		directoryField = new JTextField();
		directoryBox = new TextPrompt("Enter Dir", directoryField);
		directoryBox.changeAlpha(0.50f);
		directoryBox.setShow(Show.FOCUS_LOST);
		loadDirectory = new JButton("Load dir");
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
				} else {
					System.out.println("Bad directory, retry please");
				}
				
			}
		} );

		// Format and add components to top bar
		topBarC.gridx = 0;
		topBarC.gridy = 0;
		topBarC.weightx = 0.4;
		topBar.add(dirLabel, topBarC);

		topBarC.gridx = 1;
		topBarC.weightx = 1.0;
		topBar.add(directoryField, topBarC);

		topBarC.gridx = 2;
		topBarC.weightx = 0.2;
		topBar.add(loadDirectory, topBarC);

		// Add top bar to panel
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.1;
		panel.add(topBar, c);
		
		// Format and add components to the grid
		grid = new Container();
		grid.setLayout(new GridBagLayout());
		gridC = new GridBagConstraints();	
		gridC.fill = GridBagConstraints.BOTH;
		gridC.weightx = .7;
		gridC.weighty = .5;
		
		// Looping to set up an arbitrary amount of boxes in grid
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				// Fill the window with BreadBoxes
				fields[i][j] = new BreadBox(j, i, this);
				gridC.gridx = j + buttonGridOffsetX;
				gridC.gridy = 2 * i + buttonGridOffsetY;
				gridC.fill = GridBagConstraints.BOTH;
				grid.add(fields[i][j], gridC);
				
			}// for j, cols
		} // for i, rows
		
		// Add grid to panel
		c.gridy = 1;
		c.weighty = 1.0;
		panel.add(grid, c);
		
		// Go live
		panel.setVisible(true);
		//this.pack();
		this.setLocation(dim.width/2 - this.getSize().width/2, (int)(dim.width/2.5) - this.getSize().width/2);
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
	
	public void playSound(String path) {
		File theSound = null;
		theSound = new File(path);
		System.out.println(theSound.getAbsolutePath());
		
	}
	// End non-default methods
}
