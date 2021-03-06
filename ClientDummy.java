/**
 * This is the DNSClinet program that sends server a ipName and prints out the ipAddress sent
 * by the server.
 * @author -Neem Chhetri
 */

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.HashMap;

import javax.lang.model.util.Elements.Origin;


public class ClientDummy
{
	// Port set to 4200
	public static final int DEFAULT_PORT = 4200;
	
	public static void main(String[] args) throws IOException {	
		BufferedReader fromServer = null;
		DataOutputStream toServer = null;		// the writer to the network
		Socket server = null;			// the socket
		
		try {
			server = new Socket(args[0], DEFAULT_PORT);
			
			// joining the server
			toServer = new DataOutputStream(server.getOutputStream());
			Message messageToserver = new Message(1);
			messageToserver.addPayload(0, "Reagan");
			String toSend = messageToserver.createMessageString();
			System.out.println(toSend);
			toServer.writeBytes(toSend);
			toServer.flush();

			messageToserver = new Message(254);
			messageToserver.addPayload(2, "This is R");
			toServer.writeBytes(messageToserver.createMessageString());
			toServer.flush();

			while ( (fromServer = new BufferedReader(new InputStreamReader(server.getInputStream())))  != null) {
				System.out.println(fromServer.readLine());
			}
			// leaving the server
			// messageToserver.setControlType(2);
			// toSend = messageToserver.createMessageString();
			// System.out.println(toSend);
			// toServer.writeBytes(toSend);
			// toServer.flush();
			
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
}
