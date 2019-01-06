package com.avyay.express;

import java.util.HashMap;
import java.util.ArrayList;

public class Express {
    private static HashMap<String, ExpressHandler> RequestHandlers = new HashMap<>();
    private static HashMap<Express.Router, Boolean> UsedRouters = new HashMap<>();

    public static final com.avyay.http.java.Logger debug = new com.avyay.http.java.Logger(true);

    private static String formatURL(String url) {
        return url;
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
                String srcURL = req.field("url");
                String formattedURL = formatURL(srcURL); // Later implementation of url parameters and query args
                ExpressRequest request = new ExpressRequest(req);
                ExpressHandler appHandler = RequestHandlers.get(formattedURL);
                if (appHandler == null) {
                    for (Express.Router router : UsedRouters.keySet()) {
                        ExpressHandler handler = router.RequestHandlers.get(formattedURL);
                        if (handler != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.handle(request, new ExpressResponse(res));
                                }
                            }).start();
                        }
                    }
                } else {
                    appHandler.handle(request, new ExpressResponse(res));
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