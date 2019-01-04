package com.avyay.http.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

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

    private void writeHead(ArrayList<String> fields) {
        if (!headWritten && ready) {
            String head = "";
            for (String field : fields) {
                head += field + "\r\n";
            }
            head += "\r\n\r\n";
            try {
                out.write(head.getBytes());
                headWritten = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDefaultHeaderFields(ArrayList<String> fields) {
        fields.add(Logger.concat("Date: ", new Date().toString()));
        fields.add(Logger.concat("Server: ", "http.java/" + Http.VERSION,
            "(" + System.getProperty("os.name") + ")", ""
        ));
    }

    public void writeHead(int HttpResponseCode, String ResponseMIMEType) {
        if (!headWritten) {
            ArrayList<String> output = new ArrayList<>();
            output.add(Logger.concat("HTTP/1.1", String.valueOf(HttpResponseCode), HttpResponseCodes.get(HttpResponseCode)));
            writeDefaultHeaderFields(output);
            output.add(Logger.concat("Content-Type: ", ResponseMIMEType));
            this.writeHead(output);
        }
    }

    public void writeHead(int HttpResponseCode, String ResponseMIMEType, String EncodingCharacterSet) {
        if (!headWritten) {
            ArrayList<String> output = new ArrayList<>();
            output.add(Logger.concat("HTTP/1.1", String.valueOf(HttpResponseCode), HttpResponseCodes.get(HttpResponseCode)));
            writeDefaultHeaderFields(output);
            String encoding = (EncodingCharacterSet == null || EncodingCharacterSet.isEmpty()) ? System.getProperty("file.encoding").toLowerCase() : EncodingCharacterSet;
            output.add(Logger.concat("Content-Type: ", ResponseMIMEType + ";",
                "charset=" + encoding
            ));
            this.writeHead(output);
        }
    }

    public void writeLine(String... args) {
        this.write(Logger.concat(args), "\r\n");
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
