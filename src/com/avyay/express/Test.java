package com.avyay.express;

public class Test {
    public Test() {

    }

    public static void main(String[] args) {
        Express.App app = new Express.App();

        app.get("/index", (req, res) -> {
            res.sendFile("index.html", "text/html");
        });

        app.get("/", (req, res) -> {
            res.redirect("/index");
        });

        Express.Router rt = new Express.Router();

        rt.get("/image/:name", (req, res) -> {
            System.out.println(req.param("name"));
            res.sendFile("test.gif", "image/gif");
        });

        app.use(rt);

        app.listen(3000);
        new Test();
    }
}
