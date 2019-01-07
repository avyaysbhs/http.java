package com.avyay.express;

import javafx.util.Pair;

import java.util.HashMap;

public class Express {
    private static HashMap<String, ExpressHandler> RequestHandlers = new HashMap<>();
    private static HashMap<Express.Router, Boolean> UsedRouters = new HashMap<>();

    public static final com.avyay.http.java.Logger debug = new com.avyay.http.java.Logger(true);

    private static Pair<String, HashMap<String, String>> parseURLParams(HashMap<String, ExpressHandler> list, String url) {

        String[] src = url.split("/");

        if (src.length == 0) return null;

        for (String key: list.keySet()) {

            String[] parts = key.split("/");

            if (parts.length == src.length) {
                HashMap<String, String> out = new HashMap<>();
                for (int i = 0; i < src.length; i++) {
                    if (!src[i].equals(parts[i])) {
                        if (parts[i].startsWith(":")) {
                            out.put(
                                parts[i].replace(":", ""),
                                src[i]
                            );
                        } else {
                            return null;
                        }
                    }
                }
                return new Pair<>(key, out);
            } else {
                return null;
            }
        }
        return null;
    }

    public static class App {

        public App() {

        }

        public void get(String url, ExpressHandler handler) {
            RequestHandlers.put(url, handler);
        }

        public void use(Express.Router router) {
            UsedRouters.put(router, true);
            router.use();
        }

        public void listen(int port) {
            com.avyay.http.java.Http.createServer((req, res) -> {
                String URL = req.field("url");
                String srcURL = URL.substring(0,
                    URL.contains("?") ? URL.indexOf("?") : URL.length()
                );
                Pair<String, HashMap<String, String>> params = Express.parseURLParams(RequestHandlers, srcURL);
                if (params == null) {
                    for (Express.Router router: UsedRouters.keySet()) {
                        Pair<String, HashMap<String, String>> rParams = Express.parseURLParams(router.RequestHandlers, srcURL);
                        if (rParams != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    router.RequestHandlers.get(rParams.getKey()).handle(
                                        new ExpressRequest(req, rParams.getValue(), URL), new ExpressResponse(res)
                                    );
                                }
                            }).start();
                        }
                    }
                } else {
                    RequestHandlers.get(params.getKey()).handle(
                        new ExpressRequest(req, params.getValue(), URL), new ExpressResponse(res)
                    );
                }
            }).listen(port);
        }
    }

    public static class Router {
        protected HashMap<String, ExpressHandler> RequestHandlers = new HashMap<>();
        private boolean using = false;

        public Router() {}

        public void get(String url, ExpressHandler handler) {
            RequestHandlers.put(url, handler);
        }

        public void use() {
            if (UsedRouters.get(this)) {
                using = true;
            }
        }
    }
}