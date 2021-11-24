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

			if (mFromClient.getControlType() == 1) {
				Server.addClient(client);

				System.out.println(mFromClient.getPayload()[0] + " has joined the chatroom");
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
