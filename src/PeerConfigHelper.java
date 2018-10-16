import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class PeerConfigHelper{
   
    public static final String CONFIG_FILE_NAME = "PeerInfo.cfg";
     ArrayList peers=new ArrayList();
     private String _hostName;
     private int _portNumber;
     private Boolean _hasFile;

     public void readPeerInfo(int peerID){
           PeerConfigHelper p=new PeerConfigHelper();
            String[] config = readConfig(peerID);
            p. _hostName = config[1];
            p._portNumber = Integer.parseInt(config[2]);
            p._hasFile = Boolean.parseBoolean(config[3]);
            peers.add(p);
           
     }

      private String[] readConfig(int peerID) throws Exception  {
        try{
        
        BufferedReader br = new BufferedReader(new FileReader("PeerInfo.cfg"));

        while(true){
           String line= br.readLine();
           String[] config= line.split(" ");

           if(Integer.parseInt(config[0])==peerID)
           {    br.close();
               return line.split(" ");
            }
        
           }
   
}
    catch(Exception ex){
        System.out.println(ex);
       }
    }

       public String getHostName(){
        return _hostName;
      }

    
    public int getPortNumber(){
        return _portNumber;
     }

    
     public boolean getHasFile(){
        return _hasFile;
     }
}
