package com.avyay.express;

import com.avyay.http.java.HttpResponse;
import java.io.*;

public class ExpressResponse {
    private HttpResponse response;

    public ExpressResponse(HttpResponse response) {
        this.response = response;
    }

    public void send(String output) {
        response.writeHead(200, "text/html");
        response.write(output);
        response.end();
    }

    private void sendFile(File file, String MIME) throws IOException {
        if (file.exists() && file.canRead()) {
            FileReader fr = new FileReader(file);
            char[] buffer = new char[(int) file.length()];
            if (MIME.contains("octet-stream"))
                response.setHeader("Content-Length", file.length());
            fr.read(buffer);
            response.writeHead(200, MIME);
            response.write(buffer);
            response.end();
        } else {
            throw new FileNotFoundException("Could not find file \""+ file.getName() +"\"");
        }
    }

    public void sendFile(String path, String MIMEType) {
        try {
            this.sendFile(new File(path), MIMEType);
        } catch (IOException e) {
            Express.debug.error(e.getMessage());
        }
    }

    public void sendFile(String path) {
        this.sendFile(path, "application/octet-stream");
    }

    public void redirect(String url) {
        response.writeHead(302, new String[] {
            "Location: " + url
        });
    }
}
