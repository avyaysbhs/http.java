package com.avyay.express;

import java.util.HashMap;

public class Express {
    private static HashMap<String, ExpressHandler> EXPRESS_HANDLERS = new HashMap<>();
    private static HashMap<String, Express.Router> EXPRESS_ROUTERS = new HashMap<>();

    public static final com.avyay.http.java.Logger debug = new com.avyay.http.java.Logger(true);

    public static class App {
        public App() {

        }

        public void get(String url, ExpressHandler handler) {
            EXPRESS_HANDLERS.put(url, handler);
        }

        public void listen(int port) {
            com.avyay.http.java.Http.createServer((req, res) -> {

            });
        }
    }

    public static class Router {
        private HashMap<String, ExpressHandler> EXPRESS_HANDLERS = new HashMap<>();

        public Router() {

        }

        public void get(String url, ExpressHandler handler) {
            EXPRESS_ROUTERS.put(url, this);
            this.EXPRESS_HANDLERS.put(url, handler);
        }

        public void handle(String url, ExpressRequest request, ExpressResponse response) {

        }
    }
}