import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
public class PeerConfigHelper{

     private static final String CONFIG_FILE_NAME = "PeerInfo.cfg";
     public static HashMap<String,PeerInfo> peers=null;
     private String _hostName;
     private int _portNumber;
     private Boolean _hasFile;

     public PeerConfigHelper(){
         readPeerInfo();
     }
     

     public static Boolean hasFile(String peerId)
     {
         PeerInfo p = peers.get(peerId);
         return p.hasFile;
     }

     public PeerInfo getPeerDetails(String peerId)
     {
         return peers.get(peerId);
     }

     private synchronized void readPeerInfo() {
         try {
             PeerInfo p = null;
             String[] config = readConfig();
             if(peers==null && config!=null) {
                 peers = new HashMap<>();
                 for (String peerData : config) {
                     String[] peerDatas = peerData.split(" ");
                   
                        p = new PeerInfo();
                        p.peerId= peerDatas[0];
                        p.hostName = peerDatas[1];
                        p.portNumber = Integer.parseInt(peerDatas[2]);
                        p.hasFile = Boolean.parseBoolean(peerDatas[3]);
                        
                       peers.put(p.peerId, p);
                   
                 }
             }
         }
         catch (Exception ex)
         {
             ex.printStackTrace();
         }
     }

     private String[] readConfig() throws Exception
     {
         BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME));
        try
        {     String key="";
              String line = br.readLine();

             while (line != null) {
                key += line;
                key += "\n";
              line = br.readLine();
            }
             
           
            String[] config= key.split("\n");
           
            return config;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        finally
        {
            br.close();
        }
        return null;

    }

}
