/**
 * Handler class containing the logic for echoing results back
 * to the client. 
 *
 * @author Greg Gagne 
 * 
 * Edited by: Swornim Chhetri
 */

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;

import javax.lang.model.util.Elements.Origin;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class Handler 
{

	public static final int BUFFER_SIZE = 256;
	
	/**
	 * this method is invoked by a separate thread
	 */
	public void process(Socket client) throws java.io.IOException, UnknownHostException {	

		BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

		
		try {
			/**
			 * get the input and output streams associated with the client.
			 */
			String messageFromClient;
			
			while( (messageFromClient = fromClient.readLine()) != null) {
				System.out.println(messageFromClient);
				Message mFromClient = new Message(messageFromClient);

				// this is for joining.
				if (mFromClient.getControlType() == 1) {
					// some error checking not in protocal but is here for testing purpose.
					if (mFromClient.getPayloadQuantity() != 1) {
						toClient.writeBytes("ERROR");
					}
					else{ 
						// add client to the hashmaps.
						Server.addClient(client, mFromClient.getPayload()[0]);
						
						// Create a message to send to the client by adding connected users id and name.
						Message newtoclient = new Message(0);
						newtoclient.addPayload(Server.getAvailId()-1, mFromClient.getPayload()[0]);
						System.out.println(Server.getAvailId());

						for (int uid: Server.getAllUsers()) {
							if (uid != Server.getAvailId()-1)
								newtoclient.addPayload(uid, Server.getName(uid));
						}

						// Send the data to the all the users.
						for(Socket user: Server.getClientSocket()) {
							DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
							toUser.writeBytes(newtoclient.createMessageString());
							toUser.flush();
						}
					}
				}

				// this is for leaving
				if (mFromClient.getControlType() == 2) {
					if (mFromClient.getPayloadQuantity() != 1) {
						toClient.writeBytes("ERROR");
					}
					else {
						// create a message to send to the client
						Message newtoclient = new Message(2);
						newtoclient.addPayload(Server.getClient(client), mFromClient.getPayload()[0]);
						
						// Send the leaving message to all clients.
						for(Socket user: Server.getClientSocket()) {
							DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
							toUser.writeBytes(newtoclient.createMessageString());
							toUser.flush();
						}
						
						// remove the client and close that conenction.
						Server.removeClient(client);
						client.close();
					}
				}

				//this is for broadcast.
				if (mFromClient.getControlType() == 255) {
					if (mFromClient.getPayloadQuantity() != 1) {
						toClient.writeBytes("ERROR");
					}
					else {
						// create a message to send to the clients.
						Message newtoclient = new Message(255);
						newtoclient.addPayload(Server.getClient(client), mFromClient.getPayload()[0]);
						
						// Send the broadcast message to all clients.
						for(Socket user: Server.getClientSocket()) {
							DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
							toUser.writeBytes(newtoclient.createMessageString());
							toUser.flush();
						}
					}
				}
				
				// this is for private message.
				if (mFromClient.getControlType() == 254) {
					if (mFromClient.getPayloadQuantity() != 1) {
						toClient.writeBytes("ERROR");
					}
					else {
						// create a message to send to the clients.
						Message newtoclient = new Message(254);
						newtoclient.addPayload(Server.getClient(client), mFromClient.getPayload()[0]);
						
						// Send the direct message to the specific client.
						for(Socket user: Server.getClientSocket()) {
							if (Server.getClient(user) == mFromClient.getUserID()[0]) {
								DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
								toUser.writeBytes(newtoclient.createMessageString());
								toUser.flush();
							}
						}
					}
				}

				toClient.flush();
			}

			
   		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			if (fromClient != null)
				fromClient.close();
			if (toClient != null)
				toClient.close();
		}
	}
}
