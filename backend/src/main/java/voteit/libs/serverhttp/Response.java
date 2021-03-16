package voteit.libs.serverhttp;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;

public class Response {

    int status;
    String response;
    String contentType;

    public Response(int status, String response) {
        this.status = status;
        this.response = response;
        this.contentType = "text/plain";
    }

    public Response(String stringResponse) {
        this.status = 200;
        this.response = stringResponse;
        this.contentType = "text/plain";
    }

    public Response(JsonObject jsonResponse) {
        this.status = 200;
        this.response = jsonResponse.toString();
        this.contentType = "application/json";
    }

    public Response(JsonArray jsonArray) {
        this.status = 200;
        this.response = jsonArray.toString();
        this.contentType = "application/json";
    }

    public String build() {
        StringBuilder out = new StringBuilder();
        out.append("HTTP/1.0 " + status + "\n");
        out.append("Content-Type: " + contentType + "; charset=UTF-8" + "\n");
        out.append("\n");
        out.append(response);
        out.append("\n");
        return out.toString();
    }
}
