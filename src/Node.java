import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private int _peerId;
    public int getColumnId(){
        return _peerId;
    }

    private String _hostName;
    public String getHostName(){
        return _hostName;
    }

    private int _portNumber;
    public int getPortNumber(){
        return _portNumber;
    }

    private boolean _hasFile;
    public boolean getHasFile(){
        return _hasFile;
    }
    List peers = new ArrayList();


    public PeerInfo(){


    }
    public void init()
    {
        String thisLine = null;
        //to be used while running from terminal
        //String filePath = "PeerInfo.cfg";
        File file = new File(filePath).getAbsoluteFile();
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader breader = new BufferedReader(new FileReader(file));
            logger.Message("Started reading from PeerInfo");
            while ((thisLine = breader.readLine()) != null) {
                String[] data = thisLine.split(" ");
                PeerInfo p = new PeerInfo();
                p._peerId = Integer.parseInt(data[0]);
                p._hostName = data[1];
                p._portNumber =Integer.parseInt(data[2]);
                p._hasFile = Boolean.parseBoolean(data[3]);
                peers.add(p);
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
        peerInfo.init();
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