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
import java.io.BufferedReader;
import java.io.DataOutputStream;
//import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.print.DocFlavor.INPUT_STREAM;
//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class JoinScreen extends JFrame implements ActionListener, KeyListener
{
	private JButton exitButton;
	private JButton joinButton;
	private JTextField sendText;
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
		sendText = new JTextField(30);
		joinButton = new JButton("Join");
        exitButton = new JButton("Exit");
		

		/**
		 * register the listeners for the different button clicks
		 */
		sendText.addKeyListener(this);
		joinButton.addActionListener(this);
        exitButton.addActionListener(this);
        

		/**
		 * add the components to the panel
		 */
		p.add(sendText);
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
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

        // Image image = ImageIO.read(new File("/Users/alexassante/Desktop/CMPT Networks/Chatroom/image.png"));
        // setIconImage(image);

	}

	private String message = "";

	/**
	 * This gets the text the user entered and outputs it
	 * in the display area.
	 */
	public void displayText() {
		String username = "Username: ";
		String message = sendText.getText().trim();
		this.message = message;
		username += message;
		StringBuffer buffer = new StringBuffer(username.length());
		// // now reverse it
		// for (int i = message.length()-1; i >= 0; i--)
		//     buffer.append(message.charAt(i));
		
		for (int i = 0; i <= username.length()-1; i++)
			buffer.append(username.charAt(i));

		displayArea.append(buffer.toString() + "\n");

		sendText.setText("");
		sendText.requestFocus();
	
	}

	public String getMessage() {
		return this.message;
	}

	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == joinButton) {
			try {
				join("146.86.115.249");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
            	System.exit(0);
			}
		}
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
        public void keyPressed(KeyEvent e) { 
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
                displayText();
		}
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
        public void keyTyped(KeyEvent e) {  }
        

	public void join(String ip) throws IOException {
		int DEFAULT_PORT = 4200;
		
		BufferedReader fromServer = null;
		DataOutputStream toServer = null;		// the writer to the network
		Socket server = null;			// the socket
		
		try {
			server = new Socket(ip, DEFAULT_PORT);
			
			

			// joining the server
			toServer = new DataOutputStream(server.getOutputStream());
			Message messageToserver = new Message(1);
			messageToserver.addPayload(0, getMessage());
			String toSend = messageToserver.createMessageString();
			System.out.println(toSend);
			toServer.writeBytes(toSend);
			toServer.flush();
			
		}
		catch (IOException ioe) {
			System.err.println("There was an unexpected intteruption!");
		}
		finally {
			if (fromServer != null)
				fromServer.close();
			if (toServer != null)
				toServer.close();
			if (server != null)
				server.close();
		}
	}

	public static void main(String[] args) throws IOException {
		JFrame joinChatRoom = new JoinScreen();
	}
}
