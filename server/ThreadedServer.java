import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadedServer {

    private SharedData serverData = null;
    private String host;
    private int port;
    private ServerSocket server = null;
    private Vector<ServerThread> threads = new Vector<ServerThread>();

    public static void main(String[] args) {
        SharedData data = new SharedData(new Vector<String>(), new ConcurrentHashMap<String, Integer>());
        ThreadedServer tserver = new ThreadedServer(25000, data);
        tserver.start();
        try {
            tserver.close();
        }
        catch (IOException ioex) {

        }
    }

    public ThreadedServer(int port, SharedData data) {
        this.host = host;
        this.port = port;
        this.serverData = data;
    }

    public SharedData getSharedData() {
        return this.serverData;
    }

    public void start() {
        try {
            server = new ServerSocket(port);
            while (true) {
                try {
                    Socket connection = server.accept();
                    ServerThread cl = new ServerThread(connection, getSharedData(), threads);
                    if (cl != null) {
                        threads.add(cl);
                    }
                    Thread task = new Thread(cl);
                    task.start();
                }
                catch (IOException ioex) {
                    System.out.println("Error in inner server start.");
                }
            }
        }
        catch (IOException ioex) {
            System.out.println("Error in outer server start.");
        }
    }

    public void close() throws IOException {
        server.close();
        System.out.println("Server stopped");
    }
}