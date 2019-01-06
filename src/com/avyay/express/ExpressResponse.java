package com.avyay.express;

import com.avyay.http.java.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
            fr.read(buffer);
            response.writeHead(200, MIME);
            response.write(buffer);
            response.end();
        } else {
            throw new FileNotFoundException("Could not find file \""+ file.getName() +"\"");
        }
    }

    public void sendFile(String path, String HttpMIMEType) {
        try {
            this.sendFile(new File(path), HttpMIMEType);
        } catch (IOException e) {
            Express.debug.error(e.getMessage());
        }
    }
}
