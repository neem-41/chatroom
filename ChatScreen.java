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
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.Hashtable;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

public class ChatScreen extends JFrame implements ActionListener, KeyListener
{
	private JButton sendButton;
	private JButton exitButton;
	private JTextField sendText;
	private JTextArea displayArea;
	private JTextArea onlineUsers;
	private JLabel ousers;
	private JComboBox<String> jbox;

	private Socket server;
	private String username;
	private Runnable rt;

	public HashMap<Integer, String> map;

        
	public ChatScreen(Socket ser, String un, HashMap<Integer, String> m)  {
		/**
		 * a panel used for placing components
		 */
		this.server = ser;
		this.username = un;
		this.map = m;

		JPanel p = new JPanel();
		JPanel ou = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
		p.setBorder(titled);

		Border lined = BorderFactory.createLineBorder(Color.blue);
		ou.setBorder(lined);
		ousers = new JLabel("");
		ou.add(ousers);
		p.add(ou);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(30);
		sendButton = new JButton("Send");
		exitButton = new JButton("Exit");

		/**
		 * register the listeners for the different button clicks
		 */
        sendText.addKeyListener(this);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);


		
		int count = 1;
		String[] options = new String[map.size() + 1];
		options[0] = "Everyone";
		for(String name: map.values()) {
			options[count++] = name;
		}


		// To show the options to choose private messaging or all message
		jbox = new JComboBox<>(options);
		/**
		 * add the components to the panel
		 */
		p.add(sendText);
		p.add(sendButton);
		p.add(exitButton);
		p.add(jbox); 
		

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");
		getContentPane().add(ou, "East");
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
		setTitle("ChatScreen - " + this.username);
		pack();
 
		setVisible(true);
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				leave();
				System.exit(0);
			}
		} );

		displayOnlineUsers();

		
	}
	

	/**
	 * This gets the text the user entered and outputs it
	 * in the display area.
	 */
	public void displayText() {
		String message;
		if (jbox.getSelectedItem().equals("Everyone")) {
			message = this.username + ": " + sendText.getText().trim();
		}
		else {
			message = "(To " + jbox.getSelectedItem() + ") " + this.username + ": " + sendText.getText().trim();
		}
		StringBuffer buffer = new StringBuffer(message.length());
		
		for (int i = 0; i <= message.length()-1; i++)
			buffer.append(message.charAt(i));

		displayArea.append(buffer.toString() + "\n");

		sendText.setText("");
		sendText.requestFocus();	
	}

	// method to display the recieved text from the server.
	public void displaygotText(Message mfs) {
		if (mfs.getControlType() == 255) {
			if (!map.get(mfs.getUserID()[0]).equals(username)) {
				String message = map.get(mfs.getUserID()[0]) + ": " + mfs.getPayload()[0];
				StringBuffer buffer = new StringBuffer(message.length());
			
				for (int i = 0; i <= message.length()-1; i++)
					buffer.append(message.charAt(i));

				displayArea.append(buffer.toString() + "\n");
			}
		}
		else {
			String message = "(private) " + map.get(mfs.getUserID()[0]) + ": " + mfs.getPayload()[0];
			StringBuffer buffer = new StringBuffer(message.length());
			
				for (int i = 0; i <= message.length()-1; i++)
					buffer.append(message.charAt(i));

				displayArea.append(buffer.toString() + "\n");
		}
	}

	// method to display online users.
	public void displayOnlineUsers() {
		String message = "<html>Online users:<br/>";
		for(String name: map.values()) {
			message = message + name + "<br/>";
		}
		message = message + "</html>";
		ousers.setText(message);
		
		
		jbox.removeAllItems();
		jbox.addItem("Everyone");
		
		for(String name: map.values()) {
			if (!name.equals(username))
				jbox.addItem(name);
		}

	}


	/**
	 * This method responds to action events .... i.e. button clicks
         * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		
		if (source == sendButton) {
			broadcast();
			displayText();		
		}
		else if (source == exitButton) {
			leave();
			System.exit(0);
		}
	}
        
        /**
         * These methods responds to keystroke events and fulfills
         * the contract of the KeyListener interface.
         */
        
        /**
         * This is invoked when the user presses
         * the ENTER key.
         */
        public void keyPressed(KeyEvent e) { 
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				broadcast();
                displayText();
			}
        }
        
        /** Not implemented */
        public void keyReleased(KeyEvent e) { }
         
        /** Not implemented */
        public void keyTyped(KeyEvent e) {  }
     
	// this is for broadcast from the server.
	public void broadcast() {
		try {
			DataOutputStream toServer = new DataOutputStream(server.getOutputStream());
			Message messageToserver;
			if (jbox.getSelectedItem() == "Everyone") {
				messageToserver = new Message(255);
				messageToserver.addPayload(0, sendText.getText());
			}
			else {
				messageToserver = new Message(254);
				for (int i: map.keySet()) {
					if (map.get(i).equals(jbox.getSelectedItem()))
						messageToserver.addPayload(i, sendText.getText());
				}
			}
			String toSend = messageToserver.createMessageString();
			toServer.writeBytes(toSend);
			toServer.flush();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// this is for when people leave.
	public void leave() {
		try {
			DataOutputStream toServer = new DataOutputStream(server.getOutputStream());
			Message messageToserver = new Message(2);
			messageToserver.addPayload(0, this.username);
			String toSend = messageToserver.createMessageString();
			toServer.writeBytes(toSend);
			toServer.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
