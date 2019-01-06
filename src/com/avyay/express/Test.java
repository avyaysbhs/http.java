package com.avyay.express;

public class Test {
    public static void main(String[] args) {
        Express.App app = new Express.App();
        app.get("/", (req, res) -> {
            res.send("Hello world!");
        });

        Express.Router router = new Express.Router();
        app.use(router);

        router.get("/bob", (req, res) -> {
            res.send("Bob");
        });
        app.listen(8080);
    }
}
