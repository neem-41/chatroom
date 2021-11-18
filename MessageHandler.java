import java.nio.ByteBuffer;

public class MessageHandler {
	private final ByteBuffer buffer;
	private int byteOffset = 0;

	/**
	 * This constructor is used for receiving a message from an existing ByteBuffer
	 * @param buffer
	 */
	public MessageHandler(final ByteBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * This constructor is used for creating a new ByteBuffer and sending a message.
	 * @param message
	 */
	public MessageHandler(final Message message) {
		int neededCapacity = 5; // Control byte (1 byte) + quantity int (4 bytes)
		final int payloadQuantity = message.getPayloadQuantity();
		for(int payloadIndex = 0; payloadIndex < payloadQuantity; payloadIndex++)      // For each payload:
			neededCapacity += 8 + message.getPayload(payloadIndex).getPayloadLength(); // User id int (4 bytes) + payload length int (4 bytes) + payload
		this.buffer = ByteBuffer.allocate(neededCapacity);
		this.buffer.put(this.byteOffset, (byte) message.getMessageType().controlCode);
		this.byteOffset++;
		this.buffer.putInt(this.byteOffset,payloadQuantity);
		this.byteOffset += 4;
		for(int payloadIndex = 0; payloadIndex < payloadQuantity; payloadIndex++)
			this.writePayload(message.getPayload(payloadIndex));
	}

	public Message getMessage() {
		final int controlCode = this.buffer.get(this.byteOffset);
		this.byteOffset++;
		final int payloadQuantity = this.buffer.getInt(this.byteOffset);
		this.byteOffset += 4;
		final Message message = new Message(controlCode, payloadQuantity);
		for(int payloadIndex = 0; payloadIndex < payloadQuantity; payloadIndex++) {
			final int userId = this.buffer.getInt(this.byteOffset);
			this.byteOffset += 4;
			message.addPayload(new Message.Payload(userId, this.assemblePayloadBytes()), payloadIndex);
		}
		return message;
	}

	private byte[] assemblePayloadBytes() {
		final int payloadLength = this.buffer.getInt(this.byteOffset);
		this.byteOffset += 4;
		final byte[] chars = new byte[payloadLength];
		for(int charIndex = 0; charIndex < payloadLength; charIndex++) {
			chars[charIndex] = this.buffer.get(this.byteOffset);
			this.byteOffset++;
		}
		return chars;
	}

	private void writePayload(final Message.Payload payload) {
		this.buffer.putInt(payload.getUserId());
		this.byteOffset += 4;
		final int payloadLength = payload.getPayloadLength();
		this.buffer.putInt(this.byteOffset, payloadLength);
		this.byteOffset += 4;
		for(int charIndex = 0; charIndex < payloadLength; charIndex++) {
			this.buffer.put(this.byteOffset, payload.getPayloadByteAtIndex(charIndex));
			this.byteOffset++;
		}
	}

	public ByteBuffer getBuffer() {
		return this.buffer;
	}
}