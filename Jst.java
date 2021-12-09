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
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.print.DocFlavor.INPUT_STREAM;
//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class Jst extends JFrame implements ActionListener, KeyListener
{
	private JButton exitButton;
	private JButton joinButton;
	private JTextField sendText;
	private JTextField ipAddress;
	private JTextArea displayArea;

	private String ip;
	private Socket server;

	private HashMap<Integer, String> map;
        
	public Jst() {
		/**
		 * a panel used for placing components
		 */
		map = new HashMap<>();

		JPanel p = new JPanel();
		JLabel name = new JLabel("Username:");
		JLabel ip = new JLabel("Host IPAddress:");
		

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Please enter the username that you want to join with.");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(10);
		ipAddress = new JTextField(10);
		
		joinButton = new JButton("Join");
        exitButton = new JButton("Exit");
		

		/**
		 * register the listeners for the different button clicks
		 */
		sendText.addKeyListener(this);
		ipAddress.addKeyListener(this);
		joinButton.addActionListener(this);
        exitButton.addActionListener(this);
        

		/**
		 * add the components to the panel
		 */
		p.add(name, BorderLayout.NORTH);
		p.add(sendText);
		p.add(ip, BorderLayout.NORTH);
		p.add(ipAddress);
		p.add(joinButton);
        p.add(exitButton);
		

		/**
		 * add the panel to the "center" end of the container
		 */
		getContentPane().add(p,"Center");


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


	}

	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == joinButton) {
			try {
				join();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				ChatScreen chatroom = new ChatScreen(server, sendText.getText(), map);
				chatroom.setVisible(true);
				this.dispose();

				Thread ReaderThread = new Thread(new ReaderThread(server, chatroom));
				ReaderThread.start();
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
        // //  */
        public void keyPressed(KeyEvent e) { 
		 	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					join();
				}
				catch (IOException ioe) {
					System.out.println("There was an unexpected intteruption!");
				}
				finally {
					ChatScreen chatroom = new ChatScreen(server, sendText.getText(), map);
					chatroom.setVisible(true);
					this.dispose();
	
					Thread ReaderThread = new Thread(new ReaderThread(server, chatroom));
					ReaderThread.start();
				}
			 }
		}
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
        public void keyTyped(KeyEvent e) {  }
        

	public void join() throws IOException {
		int DEFAULT_PORT = 4200;
		
		BufferedReader fromServer = null;
		DataOutputStream toServer = null;		// the writer to the network
		
		try {
			server = new Socket(ipAddress.getText(), DEFAULT_PORT);

			// joining the server
			toServer = new DataOutputStream(server.getOutputStream());
			Message messageToserver = new Message(1);
			messageToserver.addPayload(0, sendText.getText());
			String toSend = messageToserver.createMessageString();
			toServer.writeBytes(toSend);
			toServer.flush();

			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			String check = fromServer.readLine();

			System.out.println(check);

			Message mfs = new Message(check);
			for(int i = 0; i < mfs.getPayloadQuantity(); i++) {
				map.put(mfs.getUserID()[i], mfs.getPayload()[i]);
			}

			System.out.println(map);
			
		}
		catch (IOException ioe) {
			System.err.println("There was an unexpected intteruption!....");
		}
	}

	public static void main(String[] args) throws IOException {
		Jst joinChatRoom = new Jst();
	}
}
