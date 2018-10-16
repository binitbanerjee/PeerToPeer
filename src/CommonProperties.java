import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.text.ParseException;

public class CommonProperties {

	public static final String CONFIG_FILE_NAME = "Common.cfg";
	private static int numberOfPreferredNeighbors;
	private static int unchokingInterval;
	private static int optimisticUnchokingInterval;
	private static String fileName;
	private static int fileSize;
    private static int pieceSize;
    private static int numberOfPieces;

    public void readCommonFileInfo(Reader reader) throws Exception {
     try {
          
         BufferedReader br = new BufferedReader(new FileReader("Common.cfg"));
     

         String st;
         while((st=br.readLine())!=null)
         {
            String[] config = st.split(" ");
            String name = config[0];
            String value = config[1];

            if (name.equals("NumberOfPreferredNeighbors")) {
                numberOfPreferredNeighbors = Integer.parseInt(value) + 1;
            } else if (name.equals("UnchokingInterval")) {
                unchokingInterval = Integer.parseInt(value);
            } else if (name.equals("OptimisticUnchokingInterval")) {
                optimisticUnchokingInterval = Integer.parseInt(value);
            } else if (name.equals("FileName")) {
                fileName = value;
            } else if (name.equals("FileSize")) {
                fileSize = Integer.parseInt(value);
            } else if (name.equals("PieceSize")) {
                pieceSize = Integer.parseInt(value);
            }
          }
          br.close();
     }
       catch (Exception e) {
        e.printStackTrace();
       }
     
     
}
public static int getNumberOfPreferredNeighbors() {
    return numberOfPreferredNeighbors;
}

public static int getUnchokingInterval() {
    return unchokingInterval;
}

public static int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
    }
 public static String getFileName() {
		return fileName;
}
public static int getFileSize() {
    return fileSize;
}
public static int getPieceSize() {
    return pieceSize;
}
public static int getNumberOfPieces() {
    numberOfPieces = (int) (fileSize % pieceSize) == 0 ? (int) (fileSize / pieceSize) : (int) (fileSize / pieceSize) + 1;
    return numberOfPieces;
}

public static void setNumberOfPreferredNeighbors(int n) {
    numberOfPreferredNeighbors=n;
}

public static void setUnchokingInterval(int ui) {
    unchokingInterval=ui;
}

public static void getOptimisticUnchokingInterval(int oui) {
		optimisticUnchokingInterval=oui;
    }
 public static void setFileName(String fname) {
		fileName=fname;
}
public static void setFileSize(int fsize) {
    fileSize=fsize;
}
public static void setPieceSize(int psize) {
    pieceSize=psize;
}

}