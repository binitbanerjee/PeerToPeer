import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PeerInfo{
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

