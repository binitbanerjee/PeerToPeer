import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
                File file = new File(logFilePath).getAbsoluteFile();
                    FileReader fReader = new FileReader(file);
                    BufferedReader breader = new BufferedReader(new FileReader(file));
                    String thisLine = "";
                    String data = "";
                    while ((thisLine = breader.readLine()) != null) {
                        data +=thisLine +"\n";
                    }
                Calendar dt = Calendar.getInstance();
                printWriter = new PrintWriter(logFilePath);
                printWriter.println(data + dt.getTime() +" -- "+ message);
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

class Client{
    Socket requestSocket;           //socket connect to the server
    ObjectOutputStream out;         //stream write to the socket
    ObjectInputStream in;          //stream read from the socket
    String message;                //message send to the server
    String MESSAGE;                //capitalized message read from the server
    Logger _logger;
    public void Client(Logger log) {
        _logger = log;
    }

    void run()
    {
        try{
            //create a socket to connect to the server
            requestSocket = new Socket("localhost", 4201);
            System.out.println("Connected to localhost in port 8000");
            //initialize inputStream and outputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            //get Input from standard input
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                System.out.print("Hello, please input a sentence: ");
                //read a sentence from the standard input
                message = bufferedReader.readLine();
                //Send the sentence to the server
                sendMessage(message);
                //Receive the upperCase sentence from the server
                MESSAGE = (String)in.readObject();
                //show the message to the user
                System.out.println("Receive message: " + MESSAGE);
            }
        }
        /*catch (ConnectException e) {
            System.err.println("Connection refused. You need to initiate a server first.");
        }
        catch ( ClassNotFoundException e ) {
            System.err.println("Class not found");
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }*/
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        catch (Exception ex){

        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    //send a message to the output stream
    void sendMessage(String msg)
    {
        try{
            //stream write the message
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}

class Server  {

    private static final int sPort = 4201;   //The server will be listening on this port number
    static Logger _logger ;
    public Server(Logger log){
        _logger = log;
    }

    public void init() throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(sPort);
        int clientNum = 1;
        try {
            while (true) {
                new Handler(listener.accept(), clientNum,_logger).start();
                System.out.println("Client " + clientNum + " is connected!");
                clientNum++;
            }
        } finally {
            listener.close();
        }

    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private class Handler extends Thread {
        private String message;    //message received from the client
        private String MESSAGE;    //uppercase message send to the client
        private Socket connection;
        private ObjectInputStream in;    //stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int no;        //The index number of the client
        private Logger _logger;
        public Handler(Socket connection, int no,Logger log) {
            this.connection = connection;
            this.no = no;
            _logger = log;
        }

        public void run() {
            try {
                //initialize Input and Output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                try {
                    while (true) {
                        //receive the message sent from the client
                        message = (String) in.readObject();
                        //show the message to the user
                        System.out.println("Receive message: " + message + " from client " + no);
                        //Capitalize all letters in the message
                        MESSAGE = message.toUpperCase();
                        //send MESSAGE back to the client
                        sendMessage(MESSAGE);
                    }
                } catch (ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }
            } catch (IOException ioException) {
                System.out.println("Disconnect with Client " + no);
            } finally {
                //Close connections
                try {
                    in.close();
                    out.close();
                    connection.close();
                } catch (IOException ioException) {
                    System.out.println("Disconnect with Client " + no);
                }
            }
        }

        public void sendMessage(String msg) {
            try {
                out.writeObject(msg);
                out.flush();
                System.out.println("Send message: " + msg + " to Client " + no);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

public class Node {

    public static void main(String[] args) throws Exception {
        Logger log = new Logger();
        Server tcpServer = new Server(log);
        tcpServer.init();
        //Thread tServer = new Thread(tcpServer,"Server");
        //tServer.start();
        //Thread.sleep(5000);
        //System.out.println("Now Client is being Started");
        //Client tcpClient = new Client();
        //Thread tClient = new Thread(tcpClient,"Client");
        //tClient.start();

        /*System.out.println("\r\nRunning Server: " +
                "Host=" + app.getSocketAddress().getHostAddress() +
                " Port=" + app.getPort());*/
    }
}