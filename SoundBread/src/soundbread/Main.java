package soundbread;

//import java.awt.Container;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.*;

//
//import javax.swing.JButton;
//import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Main - Pretty basic launcher class for the BreadWindow
 * 
 * @author Tanner Ingersoll
 *
 */
public class Main {

	// Window
	private static BreadWindow window;

	
	public Main() {
		// TODO: constructor, not really anything important to do here except maybe follow 

		
	}
	
	public static void main(String[] args) {
		// TODO: Here's the beginning
		try {
			//UIManager.setLookAndFeel(UIManager.);
			int i = 0;
			for (LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()) {
				System.out.println("Look and feel " + i + ": " + info.getName());
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
				}
				i++;
			}
			
			window = new BreadWindow();
		} catch (HeadlessException e) {
			e.printStackTrace();
			System.out.println("Headless exception, this is a GUI application.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
