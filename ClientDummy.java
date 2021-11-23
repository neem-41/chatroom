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
			
			toServer = new DataOutputStream(server.getOutputStream());
			toServer.writeBytes("1%@&1%@&0%@&4%@&Neem");
			
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
