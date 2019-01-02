import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class HTTPServer implements Runnable {
    protected Logger debug;
    private int port;
    private ServerSocket socket;

    private boolean ready;

    public HTTPServer(int PORT) {
        this.port = PORT;
    }

    @Override
    public void run() {
        while (ready) {
            try {
                Socket client = socket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                ArrayList<String> request = new ArrayList<>();
                String currentLine;
                do {
                    currentLine = in.readLine();
                    if (!currentLine.isEmpty())
                        request.add(currentLine);
                } while (!currentLine.isEmpty());
                in.close();
            } catch (IOException e) {
                debug.error("Server error: ", e.getMessage());
            }
        }
    }

    public void handle(HttpRequest request, HttpResponse response) {

    }

    public void start() {
        try {
            socket = new ServerSocket(this.port);
            new Thread(this).start();
        } catch (IOException e) {
            debug.error("Connection error: ", e.getMessage());
        }
    }

    public static void main(String[] args) {
        
    }
}
