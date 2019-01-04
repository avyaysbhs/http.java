package com.avyay.http.java;

public class Http {
    private static class Server {
        protected HttpRequestHandler requestHandler;

        public Server(HttpRequestHandler handler) {
            requestHandler = handler;
        }

        public void listen(int port) {
            (new HTTPServer(port) {
                @Override
                public void handle(HttpRequest req, HttpResponse res) {
                    requestHandler.handle(req, res);
                }
            }).start();
        }
    }

    public static Http.Server createServer(HttpRequestHandler handler) {
        return new Http.Server(handler);
    }

    public static void main(String[] args) {
        Http.createServer((req, res) -> {
            res.writeHead(200, "text/html");
            res.writeLine("<p>Hello world!</p>");
            res.end();
        }).listen(8080);
    }
}
