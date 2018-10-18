import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HandShakeHelper {
    private static final String HANDSHAKE_HEADER = "P2PFILESHARINGPROJ0000000000";
    private static String _message = "";

    public static synchronized String getRemotePeerId(byte[] b)
    {
        int to = b.length;
        int from = to - 4;
        byte[] bytes = Arrays.copyOfRange(b, from, to);
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }

    public static synchronized byte[] getMessage() {
        byte[] handshake = new byte[32];
        ByteBuffer bb = ByteBuffer.wrap(_message.getBytes());
        bb.get(handshake);
        return handshake;
    }

    public static synchronized void setId(String id)
    {
        _message += HANDSHAKE_HEADER + id;
    }

    public static synchronized boolean verify(byte[] message, String peerId) {
        String recvdMessage = new String(message);
        return recvdMessage.indexOf(peerId) != -1 && recvdMessage.contains(HANDSHAKE_HEADER);
    }

    public static synchronized String getId(byte[] message) {
        byte[] remotePeerId = Arrays.copyOfRange(message, message.length - 4, message.length);
        return new String(remotePeerId);
    }
}
