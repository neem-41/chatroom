import java.util.Scanner;

public class Message {
    private int controlType;
    private int payloadQuant;
    private int[] userID;
    private int[] payloadLength;
    private String[] payloadMessage;

    private final String del = "%@&";
    // This is called by the server to parse the Message.
    public Message(String message) {
        Scanner scan = new Scanner(message).useDelimiter(del);

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

        this.userID = new int[1];
        this.payloadLength = new int[1];
        this.payloadMessage = new String[1];

    }

    public void addPayload(int uid, String payload) {
        if (this.userID.length == this.payloadQuant) {
            this.userID = this.increase(this.userID);
            this.payloadLength = this.increase(this.payloadLength);
            this.payloadMessage = this.increase(this.payloadMessage);
        }

        this.userID[this.payloadQuant] = uid;
        this.payloadMessage[this.payloadQuant] = payload;
        this.payloadLength[this.payloadQuant] = this.payloadMessage[this.payloadQuant].length();
        this.payloadQuant += 1;
    }

    public String createMessageString() {
        String toreturn = "";
        toreturn = toreturn + this.controlType + del + this.payloadQuant + del;
        for (int i=0; i < this.payloadQuant; i++) {
            toreturn = toreturn + this.userID[i] + del + this.payloadLength[i] + del + this.payloadMessage[i] + del;
        }
        toreturn = toreturn.substring(0, toreturn.lastIndexOf(del)) + "\n";

        return toreturn;
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
    
    public void setControlType(int control) {
        this.controlType = control;
    }

    private int[] increase(int[] arr) {
        int[] newarr = new int[arr.length + 1];
        for (int i = 0; i< arr.length; i++) {
            newarr[i] = arr[i];
        }

        return newarr;
    }

    private String[] increase(String[] arr) {
        String[] newarr = new String[arr.length + 1];
        for (int i = 0; i< arr.length; i++) {
            newarr[i] = arr[i];
        }

        return newarr;
    }
}
