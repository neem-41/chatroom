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

	private Hashtable<Integer, Socket> clients = new Hashtable<Integer, Socket>();
	private static int userId = 2;
	private static HashMap<Integer, Socket> map = new HashMap<>();

	public static final int BUFFER_SIZE = 256;


	
	/**
	 * this method is invoked by a separate thread
	 */
	public void process(Socket client) throws java.io.IOException, UnknownHostException {	

		InputStream fromClient = client.getInputStream();
		OutputStream toClient = client.getOutputStream();

		
		try {
			/**
			 * get the input and output streams associated with the client.
			 */
			MessageHandler handlerFromClient = new MessageHandler(ByteBuffer.wrap(fromClient.readAllBytes()));
			
			Message messageFromClient = handlerFromClient.getMessage();
			Message.MessageType controlFromClient = messageFromClient.getMessageType();
			int payloadFromClientNum = messageFromClient.getPayloadQuantity();

			// Control to join the server
			if (controlFromClient.equals(Message.MessageType.JOIN)) {
				int usernum = userId;
				map.put(userId++, client);
				
				String name = messageFromClient.getPayload(0).getPayloadString();
				Message messageToClient = new Message(Message.MessageType.USER_LIST, 1);
				messageFromClient.addPayload(new Message.Payload(
					usernum, name.getBytes("UTF-8") 
				), 0);

				ByteBuffer bufferToClient = (new MessageHandler(messageToClient)).getBuffer();
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
