import java.util.Scanner;

public class Message {
    private int controlType;
    private int payloadQuant;
    private int[] userID;
    private int[] payloadLength;
    private String[] payloadMessage;

    // This is called by the server to parse the Message.
    public Message(String message) {
        Scanner scan = new Scanner(message).useDelimiter("%@&");

        this.controlType = scan.nextInt();
        this.payloadQuant = scan.nextInt();
        
        this.userID = new int[this.payloadQuant];
        this.payloadLength = new int[this.payloadQuant];
        this.payloadMessage = new String[this.payloadQuant];

        for(int i = 0; i < this.payloadQuant; i++) {
            this.userID[i] = scan.nextInt();
            this.payloadLength[i] = scan.nextInt();
            this.payloadMessage[i] = scan.next();
        }
        scan.close();
    }

    // Used by client or server to create a new message.
    public Message(int cType) {
        this.controlType = cType;
        this.payloadQuant = 0; 
    }

    public void addPayload(int uid, String payload) {
        this.userID[this.payloadQuant] = uid;
        this.payloadMessage[this.payloadQuant] = payload;
        this.payloadLength[this.payloadQuant] = this.payloadMessage[this.payloadQuant].length();
        this.payloadQuant += 1;
    }

    public void printMessage() {
        System.out.println("ControlType: " + this.controlType);
        System.out.println("payload quantity: " + this.payloadQuant);

        for(int i =0; i < this.payloadQuant; i++) {  
            System.out.println("userID: " + this.userID[i]);
            System.out.println("payload length: " + this.payloadLength[i]);
            System.out.println("Message: "+ this.payloadMessage[i]);
        }
    }

    public int getControlType() {
        return this.controlType;
    }

    public int getPayloadQuantity() {
        return this.payloadQuant;
    }

    public int[] getUserID() {
        return this.userID;
    }

    public int[] getPayloadLength() {
        return this.payloadLength;
    }

    public String[] getPayload() {
        return this.payloadMessage;
    }
}
