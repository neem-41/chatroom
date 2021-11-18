import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Message {

	public enum MessageType {
		USER_LIST(0),
		JOIN(1),
		LEAVE(2),
		DIRECT_MESSAGE(254),
		BROADCAST_MESSAGE(255);
		public final int controlCode;
		MessageType(final int controlCode) {
			this.controlCode = controlCode;
		}
	}

	public static final Map<Integer, MessageType> mappedControlCodes;
	static {
		final Map<Integer, MessageType> _mappedControlCodes = new HashMap<>();
		for(final MessageType value : MessageType.values())
			_mappedControlCodes.put(value.controlCode, value);
		mappedControlCodes = Collections.unmodifiableMap(_mappedControlCodes);
	}

	public static class Payload {
		private int userId;
		private String payloadString = "";
		private byte[] payloadBytes = new byte[0];

		public Payload(final int userId) {
			this.userId = userId;
		}
		public Payload(final int userId, final String payloadString) {
			this.userId = userId;
			this.setPayloadString(payloadString);
		}
		public Payload(final int userId, final byte[] payloadBytes) {
			this.userId = userId;
			this.setPayloadBytes(payloadBytes);
		}
		public int getUserId() {
			return this.userId;
		}
		public String getPayloadString() {
			return this.payloadString;
		}
		public int getPayloadLength() {
			return this.payloadBytes.length;
		}
		public byte getPayloadByteAtIndex(final int byteIndex) {
			return this.payloadBytes[byteIndex];
		}
		public void setPayloadString(final String payloadString) {
			this.payloadString = payloadString;
			this.payloadBytes = payloadString.getBytes(StandardCharsets.UTF_8);
		}
		public void setPayloadBytes(final byte[] payloadBytes) {
			this.payloadString = new String(payloadBytes, StandardCharsets.UTF_8);
			this.payloadBytes = payloadBytes;
		}
	}

	private MessageType messageType;
	private int payloadQuantity;
	private Payload[] payloads;

	public Message(final int controlCode, final int payloadQuantity) {
		this.messageType = mappedControlCodes.get(controlCode);
		this.payloadQuantity = payloadQuantity;
		this.payloads = new Payload[payloadQuantity];
	}

	public Message(final MessageType messageType, final int payloadQuantity) {
		this.messageType = messageType;
		this.payloadQuantity = payloadQuantity;
		this.payloads = new Payload[payloadQuantity];
	}

	public void addPayload(final Payload payload, final int payloadIndex) {
		this.payloads[payloadIndex] = payload;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}
	public int getPayloadQuantity() {
		return this.payloadQuantity;
	}
	public Payload getPayload(final int payloadIndex) {
		return this.payloads[payloadIndex];
	}
}