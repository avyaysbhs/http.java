package com.avyay.http.java;

import java.io.IOException;
import java.net.InetAddress;

public class Http {
    public static final String VERSION = "1.0.0";

    public static abstract class Server {
        protected HttpRequestHandler requestHandler;
        protected Logger console;
        private String LocalAddress;

        public Server(HttpRequestHandler handler, boolean enableLogs) {
            requestHandler = handler;
            console = new Logger(enableLogs);
            try {
                LocalAddress = InetAddress.getLocalHost().getHostAddress().toString();
            } catch (IOException e) {
                console.error(e.getMessage());
            }
        }

        public void listen(int port) {
            (new HTTPServer(port) {
                @Override
                public void handle(HttpRequest req, HttpResponse res) {
                    requestHandler.handle(req, res);
                }

                @Override
                public void start() {
                    super.start();
                    console.log("avyay.http.java listening on HOST: ", LocalAddress, "PORT:", port);
                }
            }).start();
        }

        public String getLocalAddress() {
            return LocalAddress;
        }
    }

    public static Http.Server createServer(HttpRequestHandler handler) {
        return new Http.Server(handler, true) {};
    }

    public static Http.Server createServer(HttpRequestHandler handler, boolean enableLogs) {
        return new Http.Server(handler, enableLogs) {};
    }
}
