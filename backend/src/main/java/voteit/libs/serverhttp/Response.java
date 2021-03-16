package voteit.libs.serverhttp;

import java.util.HashMap;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;

public class Response {

    int status;
    String response;
    HashMap<String, String> headers = new HashMap<>();
    String contentType;

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

    public void setStatus(int status) {
        this.status = status;
    }

    public void addCookie(String key, String value) {
        headers.put(key, value);
    }

    public String build() {
        StringBuilder out = new StringBuilder();
        out.append("HTTP/1.0 " + status + "\n");
        out.append("Content-Type: " + contentType + "; charset=UTF-8" + "\n");
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> {
                out.append(String.format("Set-Cookie: %s=%s;\n", k, v));
            });
        }
        out.append("\n");
        out.append(response);
        out.append("\n");
        return out.toString();
    }
}
