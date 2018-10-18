import java.io.IOException;
import  java.io.File;

public class peerProcess {
    private static String peerId;
    public static String getPeerId(){
        return peerId;
    }

    public static void main(String args[]) throws IOException {
        peerId = "1002";
        init();
        Boolean success = (new File("Peer_"+peerId)).mkdirs();
        if(success)
            System.out.println("FolderCreated");
        Node current = Node.getInstance();
        current.createTCPConnections();
       // current.listenForConnections();
    }

    private static void init() {
        // Updates Log Configuration at run time so that peerId is appended to
        // the filename

        new CommonProperties();
        PeerConfigHelper peerConfig = new PeerConfigHelper();
        HandShakeHelper.setId(peerId);
        if (peerConfig.hasFile(peerId)) {
            //SharedFile.getInstance().splitFile();
        }
    }

}
