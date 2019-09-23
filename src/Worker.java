import java.io.*;
import java.net.*;


public class Worker implements Runnable {
    protected Socket cs = null;
    protected String serverText = null;

    public Worker(Socket cs, String serverText) {
        this.cs = cs;
        this.serverText = serverText;
    }

    public void run() {
        try {
            InputStream input = cs.getInputStream();
            OutputStream output = cs.getOutputStream();
            long time = System.currentTimeMillis();
            output.write(("HTTP/1.1 200 OK\n\nWorker: " +
                    this.serverText + " - " +
                    time +
                    "").getBytes());
            output.close();
            input.close();
            System.out.println("Request processed: " + time);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
