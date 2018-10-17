import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PeerInfo{
    private static final String filePath = "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerInfo.cfg";
    public int number;
    public String peerId;
    public String hostName;
    public int portNumber;
    public boolean hasFile;

    @Override
    public String toString() {
        return "Peer [peerId=" + peerId + ", hostName=" + hostName + ", port=" + portNumber + ", hasSharedFile="
                + hasFile + "]";
    }
}

