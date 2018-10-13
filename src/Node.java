
public class Node {

    public static void main(String[] args) throws Exception {
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

        /*System.out.println("\r\nRunning Server: " +
                "Host=" + app.getSocketAddress().getHostAddress() +
                " Port=" + app.getPort());*/
    }
}