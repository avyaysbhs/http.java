package com.avyay.express;

public class Test {
    public static void main(String[] args) {
        Express.App app = new Express.App();

        app.get("/index", (req, res) -> {
            res.send("Hello world!");
        });

        app.get("/", (req, res) -> {
            res.redirect("/index");
        });

        app.listen(8080);
    }
}
