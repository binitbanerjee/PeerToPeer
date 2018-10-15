import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class ChunkFiles {
    public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        int sizeOfFiles = 1024 * 1024;// 1MB
        byte[] buffer = new byte[sizeOfFiles];
        byte[] contentInByte = Files.readAllBytes(f.toPath());

        String fNameWithExt = f.getName();
        String fileName = fNameWithExt.substring(0,(fNameWithExt.lastIndexOf(".")));
        String ext = fNameWithExt.substring(fNameWithExt.lastIndexOf(".")+1);

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s_%03d.%s", fileName, partCounter++, ext);
                //String filePartName = "test.docx";
                File newFile = new File(f.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                    //byte[] chunkInByte = fis.read(5);
                    //out.write(Arrays.copyOfRange(contentInByte, startByte, sizeOfFiles));
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        splitFile(new File("C:\\Users\\Shreya's Laptop\\Documents\\CN\\Proj_bin_code\\PeerToPeer\\peer_xyz\\COT5615_HW1.docx"));
    }
}

