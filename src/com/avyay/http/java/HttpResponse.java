package com.avyay.http.java;

import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;

import java.io.*;
import java.util.*;

public class HttpResponse {
    private boolean ready;
    private boolean headWritten = false;
    private OutputStream out;
    private InputStream in;
    private static HashMap<Integer, String> HttpResponseCodes = new HashMap<>();
    private HashMap<String, String> UserSetHeaderFields = new HashMap<>();
    private boolean defaultsEnabled = true;

    static {
        HttpResponseCodes.put(100, "Continue");
        HttpResponseCodes.put(101, "Switching Protocols");
        HttpResponseCodes.put(200, "OK");
        HttpResponseCodes.put(201, "Created");
        HttpResponseCodes.put(202, "Accepted");
        HttpResponseCodes.put(302, "Found");
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

    public void write(char[] buffer) throws IOException {
        for (char c: buffer) {
            out.write(c);
        }
    }

    public void pipe(byte[] buffer) throws IOException {
        out.write(buffer);
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
            head += "\r\n";
            System.out.println(head);
            try {
                out.write(head.getBytes());
                headWritten = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDefaultHeaderFields(ArrayList<String> fields) {
        if (defaultsEnabled) {
            fields.add(Logger.concat("Date:", new Date().toString()));
            fields.add(Logger.concat("Server:", "http.java/" + Http.VERSION,
                    "(" + System.getProperty("os.name") + ")", ""
            ));
        }
        for (String key: UserSetHeaderFields.keySet()) {
            fields.add(Logger.concatNoSpaces(key, ": ", UserSetHeaderFields.get(key)));
        }
    }

    public void writeHead(int HttpResponseCode, String ResponseMIMEType) {
        if (!headWritten) {
            ArrayList<String> output = new ArrayList<>();
            output.add(Logger.concat("HTTP/1.1", String.valueOf(HttpResponseCode), HttpResponseCodes.get(HttpResponseCode)));
            writeDefaultHeaderFields(output);
            output.add(Logger.concat("Content-Type:", ResponseMIMEType));
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

    public void writeHead(int HttpResponseCode, String[] lines) {
        ArrayList<String> output = new ArrayList<>();
        output.add(Logger.concat("HTTP/1.1", String.valueOf(HttpResponseCode), HttpResponseCodes.get(HttpResponseCode)));
        for (String line: lines) {
            output.add(line);
        }
        this.writeHead(output);
    }

    public void setHeader(String key, String value) {
        UserSetHeaderFields.put(key, value);
    }

    public void setHeader(String key, Object value) {
        this.setHeader(key, String.valueOf(value));
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

    public void disableDefaultHeaders() {
        defaultsEnabled = false;
    }

    public OutputStream getOutputStream() {
        return out;
    }
}
