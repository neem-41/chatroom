/**
 * An echo server listening on port 6007. 
 * This server reads from the client
 * and echoes back the result. 
 *
 * This services each request in a separate thread.
 *
 * This conforms to RFC 862 for echo servers.
 *
 * @author - Greg Gagne.
 * 
 * Edited by: Swornim Chhetri
 */

import java.net.*;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.*;

public class  Server
{
	// set the port to 4200
	public static final int DEFAULT_PORT = 4200;

    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();

	private static HashMap<Socket, Integer> map = new HashMap<>();
	private static int userID_avail = 2;


	public static void addClient(Socket client) {
		map.put(client, userID_avail++);
	}

	public static void removeClient(Socket client) {
		map.remove(client);
	}
	public static Integer getClient(Socket client) {
		return map.get(client);
	}

	public static Collection<Integer> getAllUsers() {
		return map.values();
	}

	public static int getAvailId() {
		return userID_avail;
	}

	public static Collection<Socket> getClientSocket() {
		return map.keySet();
	}
	
	public static void main(String[] args) throws IOException {
		ServerSocket sock = null;
		
		try {
			// establish the socket
			sock = new ServerSocket(DEFAULT_PORT);
			
			while (true) {
				/**
				 * now listen for connections
				 * and service the connection in a separate thread.
				 */
				Runnable task = new Connection(sock.accept());
				exec.execute(task);
			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
		finally {
			if (sock != null)
				sock.close();
		}
	}
}
