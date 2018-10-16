
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

public class UtilityHelper extends Thread {
    private volatile boolean bitfieldSent;
    private BitSet peerBitset;
    private String remotePeerId;
    private Connection conn;
    private volatile boolean uploadHandshake;
    private volatile boolean isHandshakeDownloaded;
    private SharedFile sharedFile;
    private BroadcastThread broadcaster;
    private boolean peerHasFile;
    private Peer host = Peer.getInstance();
    int i = 0;
    private LinkedBlockingQueue<byte[]> payloadQueue;
    private boolean isAlive;
    Upload upload;

    public UtilityHelper(Connection connection) {
        conn = connection;
        payloadQueue = new LinkedBlockingQueue<>();
        isAlive = true;
        sharedFile = SharedFile.getInstance();
        broadcaster = BroadcastThread.getInstance();
        peerBitset = new BitSet(CommonProperties.getNumberOfPieces());
    }

    public void setUpload(Upload value) {
        upload = value;
        if (getUploadHandshake()) {
            broadcaster.addMessage(new Object[] { conn, Message.Type.HANDSHAKE, Integer.MIN_VALUE });
        }
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                byte[] p = payloadQueue.take();
                processPayload(p);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

