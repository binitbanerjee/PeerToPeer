
import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

public class Connection {
    public Client client;
    public Server server;
    FileHandler filehandler;
    double bytesDownloaded;
    Socket peerSocket;
    String remotePeerId;
    boolean choked;
    private ConnectionHelper connectionHelper = ConnectionHelper.getInstance();

    public double getBytesDownloaded() {
        return bytesDownloaded;
    }



    public synchronized void addBytesDownloaded(long value) {
        bytesDownloaded += value;
    }

    public synchronized boolean isChoked() {
        return choked;
    }

    public Connection(Socket peerSocket) {
        this.peerSocket = peerSocket;
        filehandler = new FileHandler(this);
        client= new Client(peerSocket, filehandler);
        server = new Server(peerSocket, filehandler);
        createThreads(client, server);
        filehandler.setUpload(client);
        filehandler.start();
    }

    public Connection(Socket peerSocket,String peerId) {
        this.peerSocket = peerSocket;
        filehandler = new FileHandler(this);
        client= new Client(peerSocket, filehandler);
        server = new Server(peerSocket, filehandler);
        createThreads(client, server);
        filehandler.setUpload(client);
        filehandler.start();
    }



    public void createThreads(Client client, Server server) {
        Thread uploadThread = new Thread(client);
        Thread downloadThread = new Thread(server);
        uploadThread.start();
        downloadThread.start();
    }

    public synchronized void sendMessage(int messageLength, byte[] payload) {
        client.addMessage(messageLength, payload);
    }

    public void close() {
        try {
            peerSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized String getRemotePeerId() {
        return remotePeerId;
    }

    public synchronized void tellAllNeighbors(int pieceIndex) {
        connectionHelper.tellAllNeighbors(pieceIndex);
    }


    public synchronized void addAllConnections() {
        // TODO Auto-generated method stub
        connectionHelper.addAllConnections(this);
    }

    public void receiveMessage() {
        server.receiveMessage();
    }

    public void setPeerId(String value) {
        remotePeerId = value;
    }



}