package com.avyay.express;

public class Test {
    public static void main(String[] args) {
        Express.App app = new Express.App();

        app.get("/index", (req, res) -> {
            res.sendFile("index.html", "text/html");
        });

        app.get("/image", (req, res) -> {
            res.sendFile("test.gif", "image/gif");
        });

        app.get("/", (req, res) -> {
            res.redirect("/index");
        });

        app.listen(8080);
    }
}
