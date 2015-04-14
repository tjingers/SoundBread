package soundbread;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

public class BreadBox extends Container {

	/**
	 * 
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
		myButton = new JButton("Click me fool");
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
