import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
class Logger{
    private static final String logFilePath= "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerLog.txt";
    PrintWriter printWriter;
    private static File file;
    //use it when running from terminal
    //private static final String logFilePath= "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerLog.txt";
    public Logger(){
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

    public void Message(String message)
    {
        if(file.exists())
        {
            try {
                printWriter = new PrintWriter(logFilePath);
                printWriter.println(LocalDateTime.now() +" -- "+ message);
                printWriter.close();
            }
            catch (Exception ex){
                System.out.println("Something wrong with Logger");
            }
        }
    }
}

class PeerInfo{
    private Logger logger = new Logger();
    private static final String filePath = "/home/dell/Documents/MS/CN/Project/BitTorrentP/src/PeerInfo.cfg";
    private int _columnId;
    public int getColumnId(){
        return _columnId;
    }

    private String _hostName;
    public int getHostName(){
        return _columnId;
    }

    private int _portNumber;
    public int getPortNumber(){
        return _columnId;
    }

    private boolean _hasFile;
    public int getHasFile(){
        return _columnId;
    }

    public PeerInfo(){
        String thisLine = null;
        //to be used while running from terminal
        //String filePath = "PeerInfo.cfg";
        File file = new File(filePath).getAbsoluteFile();
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader breader = new BufferedReader(new FileReader(file));
            logger.Message("Started reading from PeerInfo");
            while ((thisLine = breader.readLine()) != null) {
                System.out.println(thisLine);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
}



public class Node {
    private ServerSocket server;
    private PeerInfo peerInfo;
    private Logger logger = new Logger();
    public Node(String ipAddress) throws Exception {
        peerInfo = new PeerInfo();
        InetAddress lint =  InetAddress.getLocalHost();
        InetAddress loopBack =  InetAddress.getLoopbackAddress();
        if (ipAddress != null && !ipAddress.isEmpty())
            this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
        else
            this.server = new ServerSocket(4201, 1, InetAddress.getLocalHost());
    }
    private void listen() throws Exception {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        while ( (data = in.readLine()) != null ) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
        }
    }
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }
    public static void main(String[] args) throws Exception {
        Node app = new Node("");
        System.out.println("\r\nRunning Server: " +
                "Host=" + app.getSocketAddress().getHostAddress() +
                " Port=" + app.getPort());

        app.listen();
    }
}