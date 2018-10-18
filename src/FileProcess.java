import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileProcess {

    private static ConcurrentHashMap<Integer, byte[]> piece = new ConcurrentHashMap<Integer, byte[]>();

    public static byte[] getPiece(int index) {
        return piece.get(index);
    }

    public static void splitFile() throws IOException {
        File f = new File("C:\\Users\\Shreya's Laptop\\Documents\\CN\\Proj_bin_code\\PeerToPeer\\peer_xyz\\COT5615_HW1.docx");
        int pieceCounter = 0;
        int sizeOfPieces = CommonProperties.getPieceSize(); //1024 * 1024;// 1MB
        long fileSize = f.length();//CommonProperties.getFileSize();
        byte[] buffer = new byte[sizeOfPieces];

        //try-with-resources to ensure closing stream

        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount;
            while (fileSize>0){
                bytesAmount = bis.read(buffer);
                fileSize-=bytesAmount;
                piece.put(pieceCounter++,buffer);
                if(fileSize<sizeOfPieces){
                    sizeOfPieces=(int)fileSize;
                    buffer = new byte[sizeOfPieces];
                }
            }
        }

    }

   // public static void main(String[] args) throws IOException {
        //splitFile(new File("C:\\Users\\Shreya's Laptop\\Documents\\CN\\Proj_bin_code\\PeerToPeer\\peer_xyz\\COT5615_HW1.docx"));
        /*code to collate file chunks and save in local dir.
        byte[] pcs;
        File newFile = new File("C:\\Users\\Shreya's Laptop\\Documents\\CN\\Proj_bin_code\\PeerToPeer\\peer_xyz\\recreated.docx");
        int pcs_size = 0;
        int sizeOfPieces=1024*1024;
        try (FileOutputStream out = new FileOutputStream(newFile)) {
            for(int i=0; i<7; i++){
                pcs = getPiece(i);
                out.write(pcs, 0, pcs.length);
                //out.write(pcs, pcs_size, pcs.length);
                pcs_size+=pcs.length;
                //sizeOfPieces+=sizeOfPieces;
            }
        }
        pcs = getPiece(5);
        System.out.println(pcs.length);*/
    //}
}

