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

public class client
{
	// Port set to 4200
	public static final int DEFAULT_PORT = 4200;
	
	public static void main(String[] args) throws IOException {	
		InputStream fromServer = null;
		OutputStream toServer = null;		// the writer to the network
		Socket server = null;			// the socket
		
		try {
			server = new Socket(args[0], DEFAULT_PORT);	
			Message m = new Message(Message.MessageType.JOIN, 1);
			m.addPayload(new Message.Payload(0, "Neem"), 0);

			ByteBuffer send = (new MessageHandler(m)).getBuffer();
			byte[] bsend = new byte[send.capacity()];

			for(int i =0; i < bsend.length; i++) {
				bsend[i] = send.get(i);
				System.out.print(bsend[i]);
			}

			System.out.println();

			// to the server
			toServer = server.getOutputStream();
			toServer.write(bsend);
			toServer.flush();

			// from the server
			fromServer = server.getInputStream();
			System.out.println("From server: "+ fromServer.readAllBytes());

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
