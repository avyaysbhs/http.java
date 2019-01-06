package com.avyay.http.java;

import java.io.IOException;
import java.net.InetAddress;

public class Http {
    public static final String VERSION = "1.0.0";

    private static class Server {
        protected HttpRequestHandler requestHandler;
        protected Logger console;
        public String LocalAddress;

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

    private static Http.Server createServer(HttpRequestHandler handler) {
        return new Http.Server(handler, true);
    }

    private static Http.Server createServer(HttpRequestHandler handler, boolean enableLogs) {
        return new Http.Server(handler, enableLogs);
    }

    public static void main(String[] args) {
        Logger debug = new Logger();
        Http.createServer((req, res) -> {
            res.writeHead(200, "text/html");
            res.writeLine("\r\n");
            res.end("<form action=\"/\" method=\"post\"><button>POST!</button></form>");
        }).listen(8080);
    }
}
