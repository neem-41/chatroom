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

import javax.lang.model.util.Elements.Origin;

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
			String messageFromClient = fromClient.readLine();
			Message mFromClient = new Message(messageFromClient);

			System.out.println(mFromClient.getPayloadQuantity());
			if (mFromClient.getControlType() == 1) {
				if (mFromClient.getPayloadQuantity() != 1) {
					System.out.println("Protocal voilated, a user cannot join as multiple person.");
				}
				else{ 
					//TODO: Return a broadcast to all the clients with teh appropriate format.
					Server.addClient(client);
					System.out.println(mFromClient.getPayload()[0] + " has joined the chatroom and got the userID: " + Server.getClient(client));
				}
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
