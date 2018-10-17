import java.io.*;
import java.net.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Node {

    private static Node host = new Node();
    private PeerInfo peerInfo;
    ConnectionHelper connectionHelper;
    public static boolean allPeersReceivedFiles = false;
    private Node() {
        PeerConfigHelper peerConfig = new PeerConfigHelper();
        peerInfo = peerConfig.getPeerDetails(peerProcess.getPeerId());
        //connectionHelper = ConnectionHelper.getInstance();
    }

    public static Node getInstance() {
        return host;
    }

    // TODO: Use filePieces of filehandler instead of network
    public boolean hasFile() {
        return PeerConfigHelper.hasFile(peerInfo.peerId);
    }
    // TODO: Optimize by maintaining index upto which all files have been received

    /*public NetworkInfo getNetwork() {
        return network;
    }

    public void setNetwork(NetworkInfo network) {
        this.network = network;
    }*/

    public void listenForConnections() throws IOException {

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(peerInfo.portNumber);
            // TODO: End connection when all peers have received files
            while (false == allPeersReceivedFiles) {
                Socket peerSocket = socket.accept();
                connectionHelper.createConnection(peerSocket);
            }
        } catch (Exception e) {
            System.out.println("Closed exception");
        } finally {
            socket.close();
        }
    }

    public void createTCPConnections() {
        HashMap<String, PeerInfo> map = PeerConfigHelper.peers;
        int myNumber = peerInfo.number;
        for (String peerId : map.keySet()) {
            PeerInfo peerInfo = map.get(peerId);
            if (peerInfo.portNumber < myNumber) {
                new Thread() {
                    @Override
                    public void run() {
                        createConnection(peerInfo);
                    }
                }.start();

            }
        }
    }

    private void createConnection(PeerInfo peerInfo) {
        int peerPort = peerInfo.portNumber;
        String peerHost = peerInfo.hostName;
        try {
            Socket clientSocket = new Socket(peerHost, peerPort);
            connectionHelper.createConnection(clientSocket, peerInfo.peerId);
            Thread.sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}