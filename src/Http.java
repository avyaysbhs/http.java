public class Http {
  private abstract class Server {
    public HTTPServer listen(int port) {
      (new HTTPServer(port) { 
        @Override
        public void handle(HttpRequest req, HttpResponse res) {

        }
      }).start();
    }
  }
  
  public Http.Server createServer(HttpRequestHandler handler) {
    
  }
}
