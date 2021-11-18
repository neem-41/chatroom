/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards 
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
//import java.io.File;
import java.io.IOException;

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class JoinScreen extends JFrame implements ActionListener, KeyListener
{
	private JButton exitButton;
	private JButton joinButton;
	private JTextArea displayArea;
    
        
	public JoinScreen() {
		/**
		 * a panel used for placing components
		 */
		JPanel p = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "What would you like to do? ");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		joinButton = new JButton("Join");
        exitButton = new JButton("Exit");
		

		/**
		 * register the listeners for the different button clicks
		 */
		joinButton.addActionListener(this);
        exitButton.addActionListener(this);
        

		/**
		 * add the components to the panel
		 */
		p.add(joinButton);
        p.add(exitButton);
		

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");

		/**
		 * add the text area for displaying output. Associate
		 * a scrollbar with this text area. Note we add the scrollpane
		 * to the container, not the text area
		 */
		displayArea = new JTextArea(15,40);
		displayArea.setEditable(false);
		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(displayArea);
		getContentPane().add(scrollPane,"Center");

		/**
		 * set the title and size of the frame
		 */
		setTitle("Chatroom");
		pack();
 
		setVisible(true);

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

        // Image image = ImageIO.read(new File("/Users/alexassante/Desktop/CMPT Networks/Chatroom/image.png"));
        // setIconImage(image);

	}


	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == joinButton) 
            // Enter the join message command
            System.exit(0);
		else if (source == exitButton)
			System.exit(0);
	}
        
        /**
         * These methods responds to keystroke events and fulfills
         * the contract of the KeyListener interface.
         */
        
        // /**
        //  * This is invoked when the user presses
        //  * a key.
        //  */
        /** Not Implemented */
        public void keyPressed(KeyEvent e) { }
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
        public void keyTyped(KeyEvent e) {  }
        

	public static void main(String[] args) {
		JFrame joinChatRoom = new JoinScreen();
	}
}