import com.avyay.express.Express;

public class Test {
    public static void main(String[] args) {
        Express.App app = new Express.App();

        app.get("/index", (req, res) -> {
            res.sendFile("index.html", "text/html");
        });

        app.get("/favicon.ico", (req, res) -> {
            res.sendFile("test.gif", "image/gif");
        });

        app.get("/image", (req, res) -> {
            res.sendFile("test.gif", "image/gif");
        });

        app.get("/", (req, res) -> {
            res.redirect("/index");
        });

        Express.Router rt = new Express.Router();

        rt.get("/hello/:name/:lname", (req, res) -> {
            for (String q: req.query()) {
                Express.debug.log(q, req.query(q));
            }
            res.send("Hello " + req.param("name") + " " + req.param("lname") + "!");
        });

        app.use(rt);

        app.listen(8080);
    }
}
