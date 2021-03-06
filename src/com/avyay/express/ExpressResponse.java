package com.avyay.express;

import com.avyay.http.java.HttpResponse;

import java.io.*;
import java.nio.file.Files;

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

    private void sendImage(File file, String MIME) throws IOException {
        response.setHeader("Content-Length", file.length());
        response.disableDefaultHeaders();
        response.writeHead(200, MIME);
        Files.copy(file.toPath(), response.getOutputStream());
        response.end();
    }

    private void sendFile(File file, String MIME) throws IOException {
        if (file.exists() && file.canRead()) {
            BufferedInputStream in = new BufferedInputStream(
                new FileInputStream(file)
            );
            if (MIME.contains("image"))
                this.sendImage(file, MIME);
            else {
                response.setHeader("Content-Length", file.length());
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                response.writeHead(200, MIME);
                response.pipe(buffer);
                response.end();
            }
        } else {
            throw new FileNotFoundException("Could not find file \""+ file.getName() +"\"");
        }
    }

    public void sendFile(String path, String MIMEType) {
        try {
            this.sendFile(new File(path), MIMEType);
        } catch (IOException e) {
            e.printStackTrace();
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
