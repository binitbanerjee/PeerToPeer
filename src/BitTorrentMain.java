import java.io.IOException;

public class BitTorrentMain {
    private static String peerId;

    public static void main(String args[]) throws IOException {
        peerId = args[0];
        init();
        //System.out.println(CommonProperties.print());
        Node current = Node.getInstance();
        current.createTCPConnections();
        current.listenForConnections();
    }

    private static void init() {
        // Updates Log Configuration at run time so that peerId is appended to
        // the filename

        new CommonProperties();
        new LoadPeerList();
        HandShakeHelper.setId(peerId);
        if (LoadPeerList.getPeer(peerId).hasSharedFile()) {
            SharedFile.getInstance().splitFile();
        }
    }

    public static String getId() {
        return peerId;
    }
}
