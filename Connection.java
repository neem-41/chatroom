/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

import java.net.*;
import java.io.*;

public class Connection implements Runnable
{
	private Socket	client;
	private static Handler handler = new Handler();
	private static int available_id = 2;

	
	public Connection(Socket client) {
		this.client = client;
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		try {
			handler.process(client);
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
	}
}

