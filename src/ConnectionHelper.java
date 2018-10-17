import java.io.File;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionHelper {

    private static ConnectionHelper connectionManager;
    private HashSet<Connection> allConnections;
    // private HashMap<String, Connection> interested; // interested but choked
    private HashSet<Connection> notInterested;
    // private ArrayList<String> interestedPeerIds;
    private PriorityQueue<Connection> preferredNeighbors;
    // Banka
    public HashSet<String> peersWithFullFile = new HashSet<String>();
    private int k = CommonProperties.getNumberOfPreferredNeighbors();
    private int m = CommonProperties.getOptimisticUnchokingInterval();
    private int p = CommonProperties.getUnchokingInterval();
    private int n = PeerConfigHelper.peers.size();
    private FileHandler fileHandler;


    public int getPeersWithFile() {
        return peersWithFullFile.size();
    }

    private ConnectionHelper() {
        notInterested = new HashSet<>();
        preferredNeighbors = new PriorityQueue<>(k + 1,
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
