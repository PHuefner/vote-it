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

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public Response addCookie(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder out = new StringBuilder();
        out.append("HTTP/1.0 " + status + "\n");
        out.append("Content-Type: " + contentType + "; charset=UTF-8" + "\n");
        out.append("Access-Control-Allow-Origin: *\n");
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
