import java.io.*;
import java.net.Socket;

public class Node {

    public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        int sizeOfFiles = 1024 * 1024;// 1MB
        byte[] buffer = new byte[sizeOfFiles];

        String fileName = f.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(f.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        splitFile(new File("D:\\destination\\myFile.mp4"));
        Logger log = new Logger();
        Server tcpServer = new Server(log);
        //tcpServer.init();
        Thread tServer = new Thread(tcpServer,"Server");
        tServer.start();
        //Thread.sleep(5000);
        //System.out.println("Now Client is being Started");
        //Thread tClient = new Thread(tcpServer,"Server");
        Client tcpClient = new Client();
        Thread tClient = new Thread(tcpClient,"Client");
        tClient.run();

    }
}