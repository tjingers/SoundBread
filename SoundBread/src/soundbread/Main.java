package soundbread;

//import java.awt.Container;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.*;
//
//import javax.swing.JButton;
//import javax.swing.JTextField;
import javax.swing.*;

public class Main {

	// Window
	private static BreadWindow window;

	
	public Main() {
		// TODO: constructor, not really anything important to do here except maybe follow 

		
	}
	
	public static void main(String[] args) {
		// TODO: Here's the beginning
		try {
			window = new BreadWindow();
		} catch (HeadlessException e) {
			e.printStackTrace();
			System.out.println("Headless exception, this is a GUI application.");
		}
	}

}
