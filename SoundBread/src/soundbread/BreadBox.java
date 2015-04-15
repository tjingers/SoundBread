package soundbread;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
import java.net.URI;

//import javafx.scene.*;
import javafx.scene.media.*;

//import javax.sound.sampled.*;
import javax.swing.*;

import soundbread.TextPrompt.Show;

/**
 * BreadBox - Convenient Container for the JButton and JTextfield needed for each element to the sound board.
 * 
 * Stores the file associated with this button, and handles the button presses and text entering stuff.
 * 
 * 4/15/2015 *New* - Has a JFXPanel to allow for the new usage of JFX
 * 
 * @author Tanner Ingersoll
 *
 */
public class BreadBox extends Container {

	/**
	 * I don't even know
	 */
	private static final long serialVersionUID = 1L;
	
	// Members
	private int x = -1;
	private int y = -1;
	private String myFilePath = "";
	private File myFile = null;
	private BreadWindow myWindow = null;
	private JButton myButton = null;
	private TextPrompt myFieldBox = null;
	private JTextField myField = null;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	
	// i don't know if these are ok or not but we have them to solve problems
	BreadBox thisBox = this;
	
	public BreadBox(int X, int Y, BreadWindow window) {
		// Constructor
		
		// Initialize Fields
		x = X;
		y = Y;
		myWindow = window;
		
		// Button setup
		myButton = new JButton("None");
		
		myButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: redo this to the proper media player (javafx)
				// This should be listening for a click, really. 
				System.out.println("Button clicked or action'd at X: " + x + " Y: " + y);
				
				// Find the file for the button and make sure it's real
				if (myFile == null) {
					System.out.println("File not initialized for this button");
					return;
				}
				if (!myFile.exists()) {
					System.out.println("File not found to play for this button");
					return;
				}
				
				// Actual playing audio part
				playFile(myFile);
				thisBox.myButton.setBackground(Color.GREEN);
				
			}
		});
		
		// Textfield setup
		myField = new JTextField();
		myField.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// I guess enter is an action key, so that will have to do
				System.out.println("Action in a text box with X: " + x + " and Y: " + y);
				// Now we load the file from the box into the breadbox if possible
				myFilePath = myField.getText();
				myFile = new File(myWindow.getCurrentDirPath() + "\\" + myFilePath);
				if (myFile.exists()) {
					System.out.println("Real file found for the name in the box");
					myField.setText("");
					myButton.setText(myFilePath);
				} else {
					System.out.println("No real file found for the name in the box");
					System.out.println(myWindow.getCurrentDirPath() + "\\" + myFilePath);
					myField.setText("");
					myButton.setText("File Invalid");
				}
			}
		});
		myFieldBox = new TextPrompt("Enter filename", myField);
		myFieldBox.changeAlpha(0.50f);
		myFieldBox.setShow(Show.FOCUS_LOST);
		
		layout = new GridBagLayout();
		gbc = new GridBagConstraints();
		
		// Add the button and field
		this.setLayout(layout);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(myButton, gbc);
		gbc.weighty = 0.1;
		gbc.gridy = 1;
		this.add(myField, gbc);
		
	}
	
	public void setFile(File newFile) {
		// Method to allow manually setting the file in the box without having to enter path
		// Mostly for the auto-mode
		if (newFile == null) {
			return;
		}
		if (newFile.exists()) {
			myFile = newFile;
			myField.setText("");
			myButton.setText(newFile.getName());
			myButton.setToolTipText(newFile.getName());
		}
		
	}

	public JButton getButton() {
		return myButton;
	}
	
	public void playFile(File f) {
		Media media = null;
		MediaPlayer mPlayer;
		URI uri;
		
		// Generate a URI and create Media for the file in question
		try {
			uri = f.toURI();
			media = new Media(uri.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Give playing it a shot
		// TODO: try to handle incorrect formats here, instead of having it throw an exception
		if (media != null) {
			mPlayer = new MediaPlayer(media);
			System.out.println("Playing media...");
			System.out.println("Volume: " + mPlayer.getVolume());
			mPlayer.setOnEndOfMedia( new Runnable() {
				@Override
				public void run() {
					// Remove this media from the window's thoughts and memories
					System.out.println("Media finished playing");
					myWindow.removeMedia(mPlayer);
					thisBox.myButton.setBackground(null);
				}
			});
			mPlayer.play();
			myWindow.addMedia(mPlayer);
		}
		
	}
	
}
