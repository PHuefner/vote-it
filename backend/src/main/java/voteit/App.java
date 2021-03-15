package voteit;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.METHOD;
import voteit.libs.serverhttp.Response;
import voteit.libs.serverhttp.ServerHttp;

public class App {
    public static void main(String[] args) throws Exception {

        ServerHttp server = new ServerHttp(3000);
        server.addRoute("/", new TestHandler());
        server.start();


    }
}

class TestHandler implements Handler {

    @Override
    public Response handle(Context context) {
        JsonObject testObject = new JsonObject();
        testObject.put("test" , 123);
        testObject.put("noice", "this is a test");
        if (context.method == METHOD.POST){
            testObject.put("requestData", context.requestData);
        }
        return new Response(testObject);
    }

}
