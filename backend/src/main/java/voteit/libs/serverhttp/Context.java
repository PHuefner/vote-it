package voteit.libs.serverhttp;

import java.util.HashMap;

public class Context {
    public METHOD method;
    public String route;
    public HashMap<String, String> header;
    public HashMap<String, String> cookies;
    public String requestData;

    public Context(METHOD method, String route, HashMap<String, String> header, HashMap<String, String> cookies) {
        this.method = method;
        this.route = route;
        this.header = header;
        this.cookies = cookies;
        this.requestData = "";
    } 

    public Context(METHOD method, String route, HashMap<String, String> header, HashMap<String, String> cookies,String requestData) {
        this.method = method;
        this.route = route;
        this.header = header;
        this.cookies = cookies;
        this.requestData = requestData;
    }
}
