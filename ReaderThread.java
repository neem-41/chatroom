import java.io.BufferedReader;
import java.net.Socket;

import javax.swing.JFrame;

import java.io.InputStreamReader;

public class ReaderThread implements Runnable
{
    private Socket server;
    private JFrame current;
    
    public ReaderThread(Socket ser, JFrame cur) {
        this.server = ser;
        this.current = cur;
    }


    public void run() {
        try {
            // get the input stream from the socket
            BufferedReader fromServer;
        
            while(true) {
                // read from the socket
                fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
                Message mfromServer = new Message(fromServer.readLine());

                /**
                 * ok, data has now arrived. Display it in the text area,
                * and resume listening from the socket.
                */
                ((ChatScreen) current).displaygotText(mfromServer);
            }
        }
        catch (java.io.IOException ioe) { }
    }
}