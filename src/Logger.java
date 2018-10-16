import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.PriorityQueue;


public class Logger{
    private static final String logFilePath= "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerLog.txt";
    private static PrintWriter printWriter;
    private static File file;
    private static Object lock=new Object();
    private static volatile Logger instance;
    //use it when running from terminal
    //private static final String logFilePath= "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerLog.txt";
    private Logger(){

    }


    public static Logger getInstance()
    {
        Logger inst = instance;
        if (inst == null) {
            synchronized (lock) {
                inst = instance;
                if (inst == null)
                {
                    instance = inst = new Logger();
                    init();
                }
            }
        }
        return inst;
    }

    private static void init()
    {
        file = new File(logFilePath);
        try {
            if (file.createNewFile()) {
                printWriter = new PrintWriter(logFilePath);

                printWriter.println("Log creation started at ");
                printWriter.close();
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }


    private void writeMessage(String message) {
        synchronized (lock) {
            if (file.exists()) {
                try {
                    File file = new File(logFilePath).getAbsoluteFile();
                    FileReader fReader = new FileReader(file);
                    BufferedReader breader = new BufferedReader(new FileReader(file));
                    String thisLine = "";
                    String data = "";
                    while ((thisLine = breader.readLine()) != null) {
                        data += thisLine + "\n";
                    }
                    Calendar dt = Calendar.getInstance();
                    printWriter = new PrintWriter(logFilePath);
                    printWriter.println(data + dt.getTime() + " -- " + message);
                    printWriter.close();
                } catch (Exception ex) {
                    System.out.println("Something wrong with Logger");
                }
            }
        }
    }

    public void logTcpConnectionTo(String peerFrom, String peerTo) {
        writeMessage(getTime() + "Peer " + peerFrom + " makes a connection to Peer " + peerTo + ".");
    }

    // [Time]: Peer [peer_ID 1] is connected from Peer [peer_ID 2].
    public void logTcpConnectionFrom(String peerFrom, String peerTo) {
        writeMessage(getTime() + "Peer " + peerFrom + " is connected from Peer " + peerTo + ".");
    }

    // [Time]: Peer [peer_ID] has the preferred neighbors [preferred neighbor ID
    // list].
    public void logChangePreferredNeighbors() {
        /*
        * Need to implement.
        * */
    }


    public void logOptimisticallyUnchokeNeighbor(String timestamp, String source, String unchokedNeighbor) {
        writeMessage(
                timestamp + "Peer " + source + " has the optimistically unchoked neighbor " + unchokedNeighbor + ".");
    }

    public void logUnchokingNeighbor(String timestamp, String peerId1, String peerId2) {
        writeMessage(timestamp + "Peer " + peerId1 + " is unchoked by " + peerId2 + ".");
    }

    public void logChokingNeighbor(String timestamp, String peerId1, String peerId2) {
        writeMessage(timestamp + "Peer " + peerId1 + " is choked by " + peerId2 + ".");
    }

    public void logHaveMessage(String timestamp, String to, String from, int pieceIndex) {
        writeMessage(timestamp + "Peer " + to + " received : 'have' ; from " + from + " for piece : "
                + pieceIndex + ".");
    }

    public void logInterested(String timestamp, String to, String from) {
        writeMessage(timestamp + "Peer " + to + " received : 'interested' ; from " + from + ".");
    }

    public void logRejected(String timestamp, String to, String from) {
        writeMessage(timestamp + "Peer " + to + " received : 'not interested'; from " + from + ".");
    }

    public void logDownloadPieceComplete(String timestamp, String to, String from, int pieceIndex, int numberOfPieces) {
        String message = timestamp + "Peer " + to + " completed downloading piece " + pieceIndex + " from peer " + from + ".";
        message += "Total Number of pieces now is " + numberOfPieces;
        writeMessage(message);

    }

    public void logDownloadFullComplete(String timestamp, String peerId) {
        writeMessage(timestamp + "Peer:" + peerId + " completed download");
    }

    public void logDebug(String message) {
        writeMessage(message);
    }

    public String getTime() {
        return Calendar.getInstance().getTime() + ": ";
    }
}
