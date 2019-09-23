import java.io.*;
import java.net.*;

public class ChatServer implements Runnable {

    protected int serverPort = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public ChatServer(int port) {
        this.serverPort = port;
    }

    public static void main(String []args) throws IOException {
        ChatServer server = new ChatServer(8080);
        new Thread(server).start();
        try {
            Thread.sleep(20 * 1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping server.");
        server.stop();
    }
    public void run() {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()) {
            Socket cs = null;
            try {
                cs = this.serverSocket.accept();
            }
            catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection.", e);
            }
            new Thread(new Worker(cs, "Multithreaded Server")).start();
        }
        System.out.println("Server stopped.");
    }

    private synchronized  boolean isStopped() {
        return this.isStopped;
    }

    private synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error closing server.", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        }
        catch (IOException e) {
            throw new RuntimeException("Can't open port 8080", e);
        }
    }


}




