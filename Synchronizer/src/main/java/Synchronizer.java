/**
 * Created by lsh on 18/09/2017.
 */
public class Synchronizer {

    static public void main(String[] args) {
        HTTPRequest http = new HTTPRequest();
        try {
            http.sendGET("http://localhost:4908/Synchronizer");
        } catch (Exception e) {
        }
    }
}
