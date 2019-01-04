package com.avyay.http.java;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

abstract class HTTPServer implements Runnable {
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
                ArrayList<String> requestLines = new ArrayList<>();
                String currentLine = in.readLine();
                while (!(currentLine.isEmpty())) {
                    requestLines.add(currentLine);
                    currentLine = in.readLine();
                }
                HttpRequest req = new HttpRequest(HttpRequest.parse(requestLines));
                HttpResponse res = new HttpResponse(client.getInputStream(), client.getOutputStream());
                handle(req, res);
            } catch (IOException e) {
                debug.error("Server error:", e.getMessage());
            }
        }
    }

    public abstract void handle(HttpRequest request, HttpResponse response);

    public ServerSocket getSocket() {
        return socket;
    }

    public void start() {
        try {
            socket = new ServerSocket(this.port);
            ready = true;
            new Thread(this).start();
        } catch (IOException e) {
            debug.error("Connection error:", e.getMessage());
        }
    }
}
