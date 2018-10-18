import java.io.File;
import java.net.Socket;
import java.util.*;

public class ConnectionHelper {

    private static ConnectionHelper connectionManager;
    private HashSet<Connection> allConnections;
    private HashMap<String, Connection> interestedPeers; // interested but choked
    private HashSet<Connection> notInterested;
    private PriorityQueue<Connection> preferredNeighbours;
    // Banka
    public HashSet<String> peersWithFullFile = new HashSet<String>();
    private int numberOfPreferredNeighbours = CommonProperties.getNumberOfPreferredNeighbors();
    private int optimisticUnchokingInterval = CommonProperties.getOptimisticUnchokingInterval();
    private int unchokingInterval = CommonProperties.getUnchokingInterval();
    private int peerCount = PeerConfigHelper.peers.size();
    private FileHandler fileHandler;


    public int getPeersWithFile() {
        return peersWithFullFile.size();
    }

    private ConnectionHelper() {
        notInterested = new HashSet<>();
        preferredNeighbours = new PriorityQueue<>(numberOfPreferredNeighbours + 1,
                (a, b) -> (int) a.getBytesDownloaded() - (int) b.getBytesDownloaded());

        fileHandler = new FileHandler();
        allConnections = new HashSet<>();
        //monitor();
    }

    protected synchronized void createConnection(Socket socket, String peerId) {
        new Connection(socket, peerId);
    }

    protected synchronized void createConnection(Socket socket) {
        new Connection(socket);
    }


    public static synchronized ConnectionHelper getInstance() {
        if (connectionManager == null) {
            connectionManager = new ConnectionHelper();
        }
        return connectionManager;
    }

    protected synchronized void tellAllNeighbors(int pieceIndex) {
        for (Connection conn : allConnections) {
            //broadcaster.addMessage(new Object[] { conn, MessageHelper.Type.HAVE, pieceIndex });
        }
    }

    public synchronized void addAllConnections(Connection connection) {
        // TODO Auto-generated method stub
        allConnections.add(connection);
    }


}
