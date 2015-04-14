package soundbread;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.*;
import javax.swing.*;

/**
 * BreadBox - Convenient Container for the JButton and JTextfield needed for each element to the sound board.
 * 
 * Stores the file associated with this button, and handles the button presses and text entering stuff.
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
	private JTextField myField = null;
	private GridBagLayout layout;
	private GridBagConstraints gbc;
	
	public BreadBox(int X, int Y, BreadWindow window) {
		// Constructor
		
		// Initialize Fields
		x = X;
		y = Y;
		myWindow = window;
		
		// Button setup
		myButton = new JButton("Click me");
		myButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// This should be listening for a click, really. 
				System.out.println("Button clicked or action'd at X: " + x + " Y: " + y);
				// Implement the thing where we play a clip
				// Works right now, only tested, and i believe will only work for a .wav
				if (myFile == null) {
					System.out.println("File not initialized for this button");
					return;
				}
				if (!myFile.exists()) {
					System.out.println("File not found to play for this button");
					return;
				}
				try {
					Clip clip = AudioSystem.getClip();
					System.out.println("Trying to load the file at: " + myFile.getAbsolutePath());
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(myFile);
					clip.open(inputStream);
					clip.start();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
		gbc.weighty = 0.3;
		gbc.gridy = 1;
		this.add(myField, gbc);
		
	}

}
