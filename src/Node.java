import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Calendar;



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
                Calendar dt = Calendar.getInstance();
                printWriter = new PrintWriter(logFilePath);
                printWriter.println(dt.getTime() +" -- "+ message);
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


class MyClient implements Runnable {
    private Logger _logger;
    public MyClient(Logger log) {
        _logger = log;
    }

    //@Override
    public void run(){
        try {
            System.out.println("Running Client on port 4201");
            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("10.192.165.196", 4201);
            System.out.println("Running Client on port 4201");
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + 'n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            _logger.Message(ex.getMessage());
        }
    }
}

class MyServer implements Runnable{
    private Logger _logger;
    public MyServer(Logger log) {
        _logger = log;

    }

    //@Override
    public void run() {
        try {
            String clientSentence;
            String capitalizedSentence;
            ServerSocket welcomeSocket = new ServerSocket(4201);
            System.out.println("Server started at 4201");
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                capitalizedSentence = clientSentence.toUpperCase() + 'n';
                outToClient.writeBytes(capitalizedSentence);
            }
        } catch (Exception ex) {
            _logger.Message(ex.getMessage());
        }
    }
}

public class Node {

    public static void main(String[] args) throws Exception {
        Logger log = new Logger();
        MyServer tcpServer = new MyServer(log);
        Thread tServer = new Thread(tcpServer,"Server");
        tServer.start();
        Thread.sleep(5000);
        System.out.println("Now Client is being Started");
        MyClient tcpClient = new MyClient(log);
        Thread tClient = new Thread(tcpClient,"Client");
        tClient.start();

        /*System.out.println("\r\nRunning Server: " +
                "Host=" + app.getSocketAddress().getHostAddress() +
                " Port=" + app.getPort());*/
    }
}