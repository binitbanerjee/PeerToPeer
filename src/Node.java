import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class PeerInfo{
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
        File file = new File("PeerInfo.cfg");
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader breader = new BufferedReader(new FileReader(file));
            while ((thisLine = breader.readLine()) != null) {
                System.out.println(thisLine);
            }
        }
        catch (Exception ex)
        {

        }
    }
}



public class Node {
    private ServerSocket server;
    public Node(String ipAddress) throws Exception {
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