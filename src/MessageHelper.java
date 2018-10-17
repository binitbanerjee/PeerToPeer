import java.nio.ByteBuffer;


public class MessageHelper {
    protected ByteBuffer bytebuffer;
    protected byte type;
    protected byte[] content;
    protected byte[] messageLength = new byte[4];
    protected byte[] payload;

    public static enum Type {
        CHOKE, UNCHOKE, INTERESTED, NOTINTERESTED, HAVE, BITFIELD, REQUEST, PIECE, HANDSHAKE;
    }

    private static MessageHelper _messageHelper;
    private FileInfo sharedFile;

    private MessageHelper() {
        sharedFile = new FileInfo();
    }

    public static synchronized MessageHelper getInstance() {
        if (_messageHelper == null) {
            _messageHelper = new MessageHelper();
        }
        return _messageHelper;
    }

    public synchronized static Type getType(byte type) {
        switch (type) {
            case 0:
                return Type.CHOKE;
            case 1:
                return Type.UNCHOKE;
            case 2:
                return Type.INTERESTED;
            case 3:
                return Type.NOTINTERESTED;
            case 4:
                return Type.HAVE;
            case 5:
                return Type.BITFIELD;
            case 6:
                return Type.REQUEST;
            case 7:
                return Type.PIECE;
        }
        return null;
    }

    public synchronized int processLength(byte[] messageLength) {
        return ByteBuffer.wrap(messageLength).getInt();
    }

    public synchronized int getMessageLength(Type messageType, int pieceIndex) {
        switch (messageType) {

            case INTERESTED:
            case NOTINTERESTED:
                return 1;
            case HANDSHAKE:
                return 32;
        }
        return -1;
    }

    public synchronized byte[] getMessagePayload(Type messageType, int pieceIndex) {
        byte[] payload = new byte[5];

        switch (messageType) {
            case CHOKE:
                return new byte[] { 0 };
            case UNCHOKE:
                return new byte[] { 1 };
            case INTERESTED:
                return new byte[] { 2 };
            case NOTINTERESTED:
                return new byte[] { 3 };
            case HAVE:
                payload[0] = 4;
                byte[] havePieceIndex = ByteBuffer.allocate(4).putInt(pieceIndex).array();
                System.arraycopy(havePieceIndex, 0, payload, 1, 4);
                break;
            case BITFIELD:

                break;
            case REQUEST:

                break;
            case PIECE:

                break;
            case HANDSHAKE:
                return HandShakeHelper.getMessage();
        }
        return payload;
    }
}