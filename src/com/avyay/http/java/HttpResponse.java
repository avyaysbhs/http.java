package com.avyay.http.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class HttpResponse {
    private boolean ready;
    private boolean headWritten = false;
    private OutputStream out;
    private InputStream in;
    private static HashMap<Integer, String> HttpResponseCodes = new HashMap<Integer, String>();

    static {
        HttpResponseCodes.put(100, "Continue");
        HttpResponseCodes.put(101, "Switching Protocols");
        HttpResponseCodes.put(200, "OK");
        HttpResponseCodes.put(201, "Created");
        HttpResponseCodes.put(202, "Accepted");
        HttpResponseCodes.put(400, "Bad Request");
        HttpResponseCodes.put(401, "Unauthorized");
        HttpResponseCodes.put(403, "Forbidden");
        HttpResponseCodes.put(404, "Not Found");
        HttpResponseCodes.put(500, "Internal Server Error");
    }

    public HttpResponse(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        ready = true;
    }

    public void write(String... args) {
        if (ready) {
            try {
                out.write(Logger.concat(args).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeHead(int HttpResponseCode, String ResponseMIMEType) {
        if (!headWritten && ready) {
            try {
                String output = Logger.concatNoSpaces(
                    "HTTP/1.1 ", String.valueOf(HttpResponseCode), " ", HttpResponseCodes.get(HttpResponseCode), "\r\n",
                    "Content-Type: ", ResponseMIMEType, "\r\n"
                );
                System.out.println(output);
                out.write(output.getBytes());
                headWritten = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeLine(String... args) {
        this.write(Logger.concat(args), "\n");
    }

    public void end(String... args) {
        try {
            this.write(args);
            ready = false;
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
