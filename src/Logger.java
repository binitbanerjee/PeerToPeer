import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger{
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
