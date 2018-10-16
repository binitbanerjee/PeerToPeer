import java.io.*;
import java.net.Socket;

public class Client implements Runnable{
    private Socket _socket;           //socket connect to the server
    private ObjectOutputStream out;         //stream write to the socket
    private ObjectInputStream in;          //stream read from the socket
    private String message;                //message send to the server
    private String MESSAGE;                //capitalized message read from the server
    private Logger _logger;
    private yte[] _payload;

    public Client(socket socket, byte[] payload) {
        this._logger = Logger.getInstance();
        this._payload = payload;
        this._socket = socket;
        
    }

    @Override
    public void run()
    {
        try{
            //create a socket to connect to the server
            requestSocket = _socket;
            System.out.println("Connected to localhost in port 8000");
            //initialize inputStream and outputStream
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            //get Input from standard input
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                sendMessage(_payload);
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
    void sendMessage(Byte[] msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}

