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
					if (mFromClient.getPayloadQuantity() != 1) {
						toClient.writeBytes("ERROR");
					}
					else{ 
						Server.addClient(client);
						Server.addName(Server.getAvailId()-1, mFromClient.getPayload()[0]);
						
						Message newtoclient = new Message(0);
						for (int uid: Server.getAllUsers()) {
							newtoclient.addPayload(uid, Server.getName(uid));
						}

						for(Socket user: Server.getClientSocket()) {
							//System.out.println(newtoclient.createMessageString());
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
						//System.out.println("here.");
						
						Message newtoclient = new Message(2);
						newtoclient.addPayload(Server.getClient(client), mFromClient.getPayload()[0]);
						
						for(Socket user: Server.getClientSocket()) {
							//System.out.println(newtoclient.createMessageString());
							DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
							toUser.writeBytes(newtoclient.createMessageString());
							toUser.flush();
						}
						
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
						Message newtoclient = new Message(255);
						newtoclient.addPayload(Server.getClient(client), mFromClient.getPayload()[0]);
						
						for(Socket user: Server.getClientSocket()) {
							//System.out.println(newtoclient.createMessageString());
							DataOutputStream toUser = new DataOutputStream(user.getOutputStream());
							toUser.writeBytes(newtoclient.createMessageString());
							toUser.flush();
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
