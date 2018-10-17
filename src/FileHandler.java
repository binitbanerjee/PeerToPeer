import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

public class FileHandler extends Thread {
    private volatile boolean bitfieldSent;
    private BitSet peerBitset;
    private String remotePeerId;
    private Connection conn;
    private volatile boolean uploadHandshake;
    private volatile boolean isHandshakeDownloaded;
    //private SharedFile sharedFile;
    //private BroadcastThread broadcaster;
    private boolean peerHasFile;
    private Node host = Node.getInstance();
    int i = 0;
    private LinkedBlockingQueue<byte[]> payloadQueue;
    private boolean isAlive;
    Client client;

    public FileHandler() {

    }
    public FileHandler(Connection connection) {
        conn = connection;
        payloadQueue = new LinkedBlockingQueue<>();
        isAlive = true;
        //sharedFile = SharedFile.getInstance();
        //broadcaster = BroadcastThread.getInstance();
        peerBitset = new BitSet(CommonProperties.getNumberOfPieces());
    }

    public void setUpload(Client value) {
        client = value;
        if (getUploadHandshake()) {

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

    public void addPayload(byte[] payload) {
        try {
            payloadQueue.put(payload);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized BitSet getPeerBitSet() {
        return peerBitset;
    }

    public synchronized void sendHandshake() {
        setUploadHandshake();
    }

    public synchronized void setUploadHandshake() {
        uploadHandshake = true;
    }

    public synchronized boolean getUploadHandshake() {
        return uploadHandshake;
    }

    public void updatePeerId(String peerId) {
        remotePeerId = peerId;
    }

    public synchronized String getRemotePeerId() {
        return remotePeerId;
    }

    public synchronized void setRemotePeerId(String remotePeerId) {
        this.remotePeerId = remotePeerId;
    }

    public synchronized void setBitfieldSent() {
        bitfieldSent = true;
    }



    protected void processPayload(byte[] payload) {
        MessageHelper.Type messageType = MessageHelper.getType(payload[0]);
        MessageHelper.Type responseMessageType = null;
        int pieceIndex = Integer.MIN_VALUE;
        System.out.println("Received message: " + messageType);
        Logger logger = Logger.getInstance();
        // LoggerUtil.getInstance().logDebug("Received message: " + messageType);
        switch (messageType) {

            case INTERESTED:
                // add to interested connections
                logger.getInstance().logInterested(getTime(), peerProcess.getPeerId(),
                        conn.getRemotePeerId());
                //conn.addInterestedConnection();
                responseMessageType = null;
                break;
            case NOTINTERESTED:
                // add to not interested connections
                logger.getInstance().logRejected(getTime(), peerProcess.getPeerId(),
                        conn.getRemotePeerId());
                //conn.addNotInterestedConnection();
                responseMessageType = null;
                break;
            case HANDSHAKE:
                remotePeerId = HandShakeHelper.getId(payload);
                conn.setPeerId(remotePeerId);
                conn.addAllConnections();
                // System.out.println("Handshake: " + responseMessageType);
                if (!getUploadHandshake()) {
                    setUploadHandshake();

                    // System.out.println("Added " + messageType + " to broadcaster");
                }

                // System.out.println("Response Message Type: " + responseMessageType);
                break;
        }

        if (null != responseMessageType) {
            // System.out.println("Shared data if: Added " + responseMessageType + " to
            // broadcaster");
            //broadcaster.addMessage(new Object[] { conn, responseMessageType, pieceIndex });
        }
    }

    private boolean isInterested() {
        /*for (int i = 0; i < CommonProperties.getNumberOfPieces(); i++) {
            if (peerBitset.get(i) && !sharedFile.isPieceAvailable(i)) {
                return true;
            }
        }*/
        return false;
    }

    public boolean hasFile() {
        return peerHasFile;
    }

    private MessageHelper.Type getInterestedNotInterested() {
        if (isInterested()) {
            return MessageHelper.Type.INTERESTED;
        }
        return MessageHelper.Type.NOTINTERESTED;
    }

    private MessageHelper.Type getMessageType(byte type) {
        /*MessageManager messageManager = MessageManager.getInstance();
        if (!isHandshakeDownloaded()) {
            setHandshakeDownloaded();
            return Message.Type.HANDSHAKE;
        }
        return messageManager.getType(type);*/
        return null;
    }

    private boolean isHandshakeDownloaded() {
        return isHandshakeDownloaded;
    }

    private void setHandshakeDownloaded() {
        isHandshakeDownloaded = true;
    }

    public String getTime() {
        return Calendar.getInstance().getTime() + ": ";
    }
}